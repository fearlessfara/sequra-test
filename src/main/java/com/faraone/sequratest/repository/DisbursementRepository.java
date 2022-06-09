package com.faraone.sequratest.repository;

import com.faraone.sequratest.model.Disbursement;
import com.faraone.sequratest.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

    List<Disbursement> findByCreatedAtBetween(Instant from, Instant to);

    List<Disbursement> findByCreatedAtBetweenAndMerchant(Instant from, Instant to, Merchant merchant);

}
