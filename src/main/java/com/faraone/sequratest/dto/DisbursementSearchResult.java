package com.faraone.sequratest.dto;

import java.util.List;

public record DisbursementSearchResult(List<DisbursementDTO> disbursements, DisbursementSearchBean searchBean) {

}
