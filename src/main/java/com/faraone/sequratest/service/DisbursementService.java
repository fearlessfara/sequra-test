package com.faraone.sequratest.service;

import com.faraone.sequratest.core.DisbursementEngine;
import com.faraone.sequratest.dto.DisbursementSearchBean;
import com.faraone.sequratest.model.Disbursement;
import com.faraone.sequratest.model.Merchant;
import com.faraone.sequratest.repository.DisbursementRepository;
import com.faraone.sequratest.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class DisbursementService {
    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    DisbursementRepository disbursementRepository;

    @Autowired
    DisbursementEngine disbursementEngine;

    public List<Disbursement> searchDisbursements(DisbursementSearchBean disbursementSearchBean) {
        Instant from = disbursementSearchBean.from() != null ? disbursementSearchBean.from() : Instant.EPOCH;
        Instant to = disbursementSearchBean.to() != null ? disbursementSearchBean.to() : Instant.now();

        Merchant merchant = null;
        if (disbursementSearchBean.merchantId() != null) {
            merchant = merchantRepository.findById(disbursementSearchBean.merchantId())
                    .orElseThrow(() -> new IllegalArgumentException("No merchant found for id " + disbursementSearchBean.merchantId()));
        }

        if (merchant != null) {
            return disbursementRepository.findByCreatedAtBetweenAndMerchant(from, to, merchant);
        } else {
            return disbursementRepository.findByCreatedAtBetween(from, to);
        }
    }

    public List<Disbursement> calculateDisbursements(DisbursementSearchBean disbursementSearchBean) {
        Instant from = disbursementSearchBean.from() != null ? disbursementSearchBean.from() : Instant.EPOCH;
        Instant to = disbursementSearchBean.to() != null ? disbursementSearchBean.to() : Instant.now();
        Merchant merchant = merchantRepository.findById(disbursementSearchBean.merchantId())
                .orElseThrow(() -> new IllegalArgumentException("No merchant found for id " + disbursementSearchBean.merchantId()));

        return Collections.singletonList(disbursementEngine.calculateByMerchantAndTimeFrame(merchant, from, to));

    }
}
