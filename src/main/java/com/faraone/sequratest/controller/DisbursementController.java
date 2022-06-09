package com.faraone.sequratest.controller;

import com.faraone.sequratest.dto.DisbursementDTO;
import com.faraone.sequratest.dto.DisbursementSearchBean;
import com.faraone.sequratest.dto.DisbursementSearchResult;
import com.faraone.sequratest.model.Disbursement;
import com.faraone.sequratest.service.DisbursementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/disbursement")
public class DisbursementController {

    private static final Logger LOG = LoggerFactory.getLogger(DisbursementController.class);

    @Autowired
    DisbursementService disbursementService;

    @PostMapping("/search")
    public DisbursementSearchResult searchDisbursement(@RequestBody DisbursementSearchBean disbursementSearchBean) {
        LOG.info("Searching disbursement with params: {}", disbursementSearchBean);
        List<DisbursementDTO> disbursements = disbursementService.searchDisbursements(disbursementSearchBean).stream()
                .map(this::toDTO)
                .toList();
        return new DisbursementSearchResult(disbursements, disbursementSearchBean);
    }

    @PostMapping("/create")
    public DisbursementSearchResult createDisbursement(@RequestBody DisbursementSearchBean disbursementSearchBean) {
        LOG.info("Creating disbursement synchronously with params: {}", disbursementSearchBean);

        long start = System.currentTimeMillis();
        if (isNull(disbursementSearchBean.merchantId())) {
            throw new IllegalArgumentException("Merchant id is required");
        }
        if (isNull(disbursementSearchBean.from()) || isNull(disbursementSearchBean.to())) {
            throw new IllegalArgumentException("From and To dates are required");
        }
        List<DisbursementDTO> disbursements = disbursementService.calculateDisbursements(disbursementSearchBean).stream()
                .map(this::toDTO)
                .toList();
        long end = System.currentTimeMillis();
        LOG.info("Created {} disbursements in {} ms", disbursements.size(), end - start);
        return new DisbursementSearchResult(disbursements, disbursementSearchBean);

    }

    @PostMapping("/create-async")
    public void createDisbursementAsync(@RequestBody DisbursementSearchBean disbursementSearchBean) {
        if (isNull(disbursementSearchBean.merchantId())) {
            throw new IllegalArgumentException("Merchant id is required");
        }
        if (isNull(disbursementSearchBean.from()) || isNull(disbursementSearchBean.to())) {
            throw new IllegalArgumentException("From and To dates are required");
        }
        disbursementService.calculateDisbursementAsync(disbursementSearchBean);
    }

    private DisbursementDTO toDTO(Disbursement disbursement) {
        return new DisbursementDTO(disbursement.getNetAmount(), disbursement.getGrossAmount(), disbursement.getFeeAmount(), disbursement.getCreatedAt());
    }
}
