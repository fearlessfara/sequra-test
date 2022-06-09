package com.faraone.sequratest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DisbursementDTO(BigDecimal netAmount, BigDecimal grossAmount, BigDecimal feeAmount, Instant createdAt) {
}
