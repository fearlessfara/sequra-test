package com.faraone.sequratest.core;

import com.faraone.sequratest.model.FeeRate;
import com.faraone.sequratest.repository.FeeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeEngine {

    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Autowired
    FeeRateRepository feeRateRepository;


    //TODO this must be optimized, we can create at runtime a regex that matches exactly each amount range and cache the
    // range fee rates in a cache, after a couple of misses this should hit every time until the cache is cleared or the
    // application context reloaded
     public BigDecimal calculateFee(BigDecimal orderAmount) {
        FeeRate feeRate = feeRateRepository.findFeeRateForAmount(orderAmount).orElseThrow(() -> new IllegalStateException("No fee rate found for amount: " + orderAmount));
        return orderAmount.divide(HUNDRED, RoundingMode.HALF_EVEN).multiply(feeRate.getRate());
    }
}
