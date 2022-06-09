package com.faraone.sequratest.repository;

import com.faraone.sequratest.model.Shopper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopperRepository extends JpaRepository<Shopper, Long> {
}
