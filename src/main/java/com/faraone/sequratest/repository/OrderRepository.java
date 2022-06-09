package com.faraone.sequratest.repository;

import com.faraone.sequratest.model.Merchant;
import com.faraone.sequratest.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.status = :status and o.completedAt >= :instantFrom and o.completedAt <= :instantTo")
    Page<Order> findOrdersCompletedInTimeFrameAndStatusPaged(Instant instantFrom, Instant instantTo, Order.Status status, Pageable pageable);


    @Query("select o from Order o where o.status = :status and o.completedAt >= :instantFrom and o.completedAt <= :instantTo and o.merchant = :merchant")
    Page<Order> findOrdersByMerchantAndCompletedInTimeFrameAndStatusPaged(Merchant merchant, Instant instantFrom, Instant instantTo, Order.Status status, Pageable pageable);
}
