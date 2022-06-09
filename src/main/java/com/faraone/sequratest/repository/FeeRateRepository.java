package com.faraone.sequratest.repository;

import com.faraone.sequratest.model.FeeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface FeeRateRepository extends JpaRepository<FeeRate, Long> {
    @Query("SELECT f FROM FeeRate f WHERE f.rangeStart <= :orderAmount AND f.rangeEnd >= :orderAmount")
    Optional<FeeRate> findFeeRateForAmount(BigDecimal orderAmount);
}
