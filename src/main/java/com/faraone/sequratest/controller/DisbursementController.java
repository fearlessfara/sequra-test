package com.faraone.sequratest.controller;

import com.faraone.sequratest.dto.DisbursementDTO;
import com.faraone.sequratest.dto.DisbursementSearchBean;
import com.faraone.sequratest.dto.DisbursementSearchResult;
import com.faraone.sequratest.model.Disbursement;
import com.faraone.sequratest.service.DisbursementService;
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

    @Autowired
    DisbursementService disbursementService;

    @PostMapping("/search")
    public DisbursementSearchResult searchDisbursement(@RequestBody DisbursementSearchBean disbursementSearchBean) {
        List<DisbursementDTO> disbursements = disbursementService.searchDisbursements(disbursementSearchBean).stream()
                .map(this::toDTO)
                .toList();
        return new DisbursementSearchResult(disbursements, disbursementSearchBean);
    }

    @PostMapping("/create")
    public DisbursementSearchResult createDisbursement(@RequestBody DisbursementSearchBean disbursementSearchBean) {
        if (isNull(disbursementSearchBean.merchantId())) {
            throw new IllegalArgumentException("Merchant id is required");
        }
        if (isNull(disbursementSearchBean.from()) || isNull(disbursementSearchBean.to())) {
            throw new IllegalArgumentException("From and To dates are required");
        }
        List<DisbursementDTO> disbursements = disbursementService.calculateDisbursements(disbursementSearchBean).stream()
                .map(this::toDTO)
                .toList();
        return new DisbursementSearchResult(disbursements, disbursementSearchBean);

    }

    private DisbursementDTO toDTO(Disbursement disbursement) {
        return new DisbursementDTO(disbursement.getNetAmount(), disbursement.getGrossAmount(), disbursement.getFeeAmount(), disbursement.getCreatedAt());
    }
}
