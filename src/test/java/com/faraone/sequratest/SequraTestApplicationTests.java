package com.faraone.sequratest;

import com.faraone.sequratest.controller.DisbursementController;
import com.faraone.sequratest.dto.DisbursementSearchBean;
import com.faraone.sequratest.dto.DisbursementSearchResult;
import com.faraone.sequratest.repository.DisbursementRepository;
import com.faraone.sequratest.repository.FeeRateRepository;
import com.faraone.sequratest.repository.MerchantRepository;
import com.faraone.sequratest.repository.OrderRepository;
import com.faraone.sequratest.repository.ShopperRepository;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestSetup.class)
@ActiveProfiles("test")
class SequraTestApplicationTests {

    private static final Logger LOG = LoggerFactory.getLogger(SequraTestApplicationTests.class);

    @Autowired
    TestSetup testSetup;

    @Autowired
    DisbursementRepository disbursementRepository;
    @Autowired
    FeeRateRepository feeRateRepository;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ShopperRepository shopperRepository;

    @Autowired
    DisbursementController disbursementController;

    @BeforeEach
    public void setup() {
        disbursementRepository.deleteAll();
        feeRateRepository.deleteAll();
        orderRepository.deleteAll();
        merchantRepository.deleteAll();
        shopperRepository.deleteAll();

    }

    @Test
    void contextLoads() {
        testSetup.init();
        System.out.println("hello");
    }

    @Test
    void testDisbursementFetch() {
        testSetup.init();
        DisbursementSearchBean dsb = new DisbursementSearchBean(Instant.EPOCH, Instant.now(), null);
        long count = disbursementRepository.count();
        DisbursementSearchResult result = disbursementController.searchDisbursement(dsb);

        assertThat(result.disbursements().size()).isEqualTo(count);

        dsb = new DisbursementSearchBean(Instant.EPOCH, Instant.now(), 1L);
        result = disbursementController.createDisbursement(dsb);
        assertThat(result.disbursements().size()).isEqualTo(count + 1);
    }

}
