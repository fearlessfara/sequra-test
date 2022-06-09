package com.faraone.sequratest.core;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.isNull;

@Component
public final class FeeEngine {

    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    public static final BigDecimal ZERO_POINT_NINETY_FIVE = BigDecimal.valueOf(0.95);
    public static final BigDecimal ZERO_POINT_EIGHTY_FIVE = BigDecimal.valueOf(0.85);

    public BigDecimal calculateFeeAmount(BigDecimal orderAmount) {

        if (isNull(orderAmount) || orderAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        //crazy unnecessary precision, but we're dealing with money... also round up will "get us some more fees"
        BigDecimal amount = orderAmount.setScale(16, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.valueOf(50.00)) < 0) {
            return amount.divide(HUNDRED, RoundingMode.HALF_UP).multiply(BigDecimal.ONE);
        }
        if (amount.compareTo(BigDecimal.valueOf(50.00)) >= 0 && amount.compareTo(BigDecimal.valueOf(300.00)) <= 0) {
            return amount.divide(HUNDRED, RoundingMode.HALF_UP).multiply(ZERO_POINT_NINETY_FIVE);
        }
        return amount.divide(HUNDRED, RoundingMode.HALF_UP).multiply(ZERO_POINT_EIGHTY_FIVE);
    }
}
