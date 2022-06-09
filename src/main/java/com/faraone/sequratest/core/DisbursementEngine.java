package com.faraone.sequratest.core;

import com.faraone.sequratest.model.Disbursement;
import com.faraone.sequratest.model.Merchant;
import com.faraone.sequratest.model.Order;
import com.faraone.sequratest.repository.DisbursementRepository;
import com.faraone.sequratest.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DisbursementEngine {

    private static final Logger LOG = LoggerFactory.getLogger(DisbursementEngine.class);
    public static final int PAGE_SIZE = 1000;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DisbursementRepository disbursementRepository;

    @Autowired
    FeeEngine feeEngine;

    @Value("${scheduled.disbursement.enabled:false}")
    private boolean disbursementScheduledProcessingEnabled;

    @Scheduled(cron = "${scheduled.disbursement.cron:0 0 0 * * MON}") //we can configure via properties too
    public void processDisbursements() {
        if (!disbursementScheduledProcessingEnabled) {
            LOG.info("scheduled disbursement processing disabled, skipping job.");
            return;
        }
        LOG.info("Processing disbursements");
        final Instant from = Instant.now().minus(1, ChronoUnit.WEEKS);
        final Instant to = Instant.now();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Page<Order> orderPage = orderRepository.findOrdersCompletedInTimeFrameAndStatusPaged(from, to, Order.Status.COMPLETED, pageable);
        final Map<Merchant, Disbursement> merchantDisbursementMap = new HashMap<>();
        while (!orderPage.isEmpty()) {
            orderPage.getContent().forEach(o -> {
                Merchant merchant = o.getMerchant();
                Disbursement disbursement = merchantDisbursementMap.get(merchant);
                if (disbursement == null) {
                    disbursement = new Disbursement();
                    disbursement.setMerchant(merchant);
                    disbursement.setNetAmount(o.getAmount());
                    disbursement.setFeeAmount(feeEngine.calculateFee(o.getAmount()));
                    disbursement.setPeriodStart(from);
                    disbursement.setPeriodEnd(to);
                    merchantDisbursementMap.put(merchant, disbursement);
                } else {
                    disbursement.setNetAmount(disbursement.getNetAmount().add(o.getAmount()));
                    disbursement.setFeeAmount(disbursement.getFeeAmount().add(feeEngine.calculateFee(o.getAmount())));
                }
            });
            pageable = pageable.next();
            orderPage = orderRepository.findOrdersCompletedInTimeFrameAndStatusPaged(from, to, Order.Status.COMPLETED, pageable);
        }

        final List<Disbursement> disbursements = disbursementRepository.saveAll(merchantDisbursementMap.values());
        sendMoneyToMerchant(disbursements);
        LOG.info("Processed {} disbursements", disbursements.size());
    }

    public Disbursement calculateByMerchantAndTimeFrame(final Merchant merchant, final Instant from, final Instant to) {

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Page<Order> orderPage = orderRepository.findOrdersByMerchantAndCompletedInTimeFrameAndStatusPaged(merchant, from, to, Order.Status.COMPLETED, pageable);

        final AtomicReference<BigDecimal> grossAmount = new AtomicReference<>(BigDecimal.ZERO);
        final AtomicReference<BigDecimal> fee = new AtomicReference<>(BigDecimal.ZERO);
        final AtomicReference<BigDecimal> netAmount = new AtomicReference<>(BigDecimal.ZERO);

        while (!orderPage.isEmpty()) {
            orderPage.getContent().forEach(o -> {
                grossAmount.set(grossAmount.get().add(o.getAmount()));
                BigDecimal orderFee = feeEngine.calculateFee(o.getAmount());
                fee.set(fee.get().add(orderFee));
                BigDecimal orderNetAmount = o.getAmount().subtract(orderFee);
                netAmount.set(netAmount.get().add(orderNetAmount));
            });
            pageable = pageable.next();
            orderPage = orderRepository.findOrdersCompletedInTimeFrameAndStatusPaged(from, to, Order.Status.COMPLETED, pageable);
        }

        Disbursement disbursement = new Disbursement();
        disbursement.setMerchant(merchant);
        disbursement.setPeriodStart(from);
        disbursement.setPeriodEnd(to);
        disbursement.setFeeAmount(fee.get());
        disbursement.setNetAmount(netAmount.get());
        disbursement.setGrossAmount(grossAmount.get());

        return disbursementRepository.save(disbursement);
    }

    public void sendMoneyToMerchant(List<Disbursement> disbursements) {
        LOG.info("Sending money to merchant");
    }
}
