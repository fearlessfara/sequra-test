package com.faraone.sequratest.repository;

import com.faraone.sequratest.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
