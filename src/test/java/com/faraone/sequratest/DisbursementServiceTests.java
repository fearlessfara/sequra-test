package com.faraone.sequratest;

import com.faraone.sequratest.controller.DisbursementController;
import com.faraone.sequratest.core.DisbursementEngine;
import com.faraone.sequratest.dto.DisbursementSearchBean;
import com.faraone.sequratest.dto.DisbursementSearchResult;
import com.faraone.sequratest.repository.DisbursementRepository;
import com.faraone.sequratest.repository.MerchantRepository;
import com.faraone.sequratest.repository.OrderRepository;
import com.faraone.sequratest.repository.ShopperRepository;
import com.faraone.sequratest.service.DisbursementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(BaselineSetup.class)
class DisbursementServiceTests {

    private static final Logger LOG = LoggerFactory.getLogger(DisbursementServiceTests.class);

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
    DisbursementService disbursementService;

    @Autowired
    DisbursementEngine disbursementEngine;

    @BeforeEach
    public void setup() {
        disbursementRepository.deleteAll();
        orderRepository.deleteAll();
        merchantRepository.deleteAll();
        shopperRepository.deleteAll();

    }

    @Test
    void contextLoads() {
        baselineSetup.init();
        LOG.info("hello, this is a test");
    }

    @Test
    void testDisbursementFetch() {
        baselineSetup.init();
        DisbursementSearchBean dsb = new DisbursementSearchBean(Instant.EPOCH, Instant.now(), null);
        long count = disbursementRepository.count();
        DisbursementSearchResult result = disbursementController.searchDisbursement(dsb);

        assertThat(result.disbursements().size()).isEqualTo(count);

        dsb = new DisbursementSearchBean(Instant.EPOCH, Instant.now(), 1L);
        result = disbursementController.createDisbursement(dsb);
        assertThat(result.disbursements()).hasSize(14);
        //assertThat(result.disbursements().get(0).grossAmount().subtract(result.disbursements().get(0).feeAmount())).isEqualByComparingTo(result.disbursements().get(0).netAmount());
        //TODO here we should put assertions on a manually calculated disbursement, due to lack of time I won't be able
        //to to di in the 3 hours frame unfortunately
    }

}
