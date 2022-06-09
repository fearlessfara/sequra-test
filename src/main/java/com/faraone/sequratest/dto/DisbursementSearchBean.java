package com.faraone.sequratest.dto;

import java.time.Instant;

public record DisbursementSearchBean(Instant from, Instant to, Long merchantId) {
}
