package com.faraone.sequratest;

import com.faraone.sequratest.controller.DisbursementController;
import com.faraone.sequratest.core.FeeEngine;
import com.faraone.sequratest.repository.DisbursementRepository;
import com.faraone.sequratest.repository.MerchantRepository;
import com.faraone.sequratest.repository.OrderRepository;
import com.faraone.sequratest.repository.ShopperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(BaselineSetup.class)
@ActiveProfiles("test")
class FeeEngineTest {

    @Autowired
    BaselineSetup baselineSetup;

    @Autowired
    DisbursementRepository disbursementRepository;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ShopperRepository shopperRepository;

    @Autowired
    DisbursementController disbursementController;

    @Autowired
    FeeEngine feeEngine;

    @BeforeEach
    public void setup() {
        disbursementRepository.deleteAll();
        orderRepository.deleteAll();
        merchantRepository.deleteAll();
        shopperRepository.deleteAll();

    }


    @Test
    void checkLimitCaseFeeRates() {

        BigDecimal fee = feeEngine.calculateFeeAmount(BigDecimal.ZERO);
        assertThat(fee).isEqualByComparingTo(BigDecimal.ZERO);

        fee = feeEngine.calculateFeeAmount(BigDecimal.valueOf(50.00));
        assertThat(fee).isEqualByComparingTo(BigDecimal.valueOf(0.475));

        fee = feeEngine.calculateFeeAmount(BigDecimal.valueOf(50.001).setScale(16, RoundingMode.HALF_UP));
        assertThat(fee).isEqualByComparingTo(BigDecimal.valueOf(0.4750095));

        fee = feeEngine.calculateFeeAmount(BigDecimal.valueOf(300).setScale(16, RoundingMode.HALF_UP));
        assertThat(fee).isEqualByComparingTo(BigDecimal.valueOf(2.85));

        fee = feeEngine.calculateFeeAmount(BigDecimal.valueOf(300.0001).setScale(16, RoundingMode.HALF_UP));
        assertThat(fee).isEqualByComparingTo(BigDecimal.valueOf(2.55000085));
    }
}
