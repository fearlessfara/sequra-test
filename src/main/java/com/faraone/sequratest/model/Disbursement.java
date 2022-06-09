package com.faraone.sequratest.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private BigDecimal feeAmount;

    @Column
    private BigDecimal netAmount;

    @Column
    BigDecimal grossAmount;

    @ManyToOne
    private Merchant merchant;

    @Column
    private Instant periodStart;

    @Column
    private Instant periodEnd;

    @CreationTimestamp
    private Instant createdAt;

    public Disbursement() {
        //hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal fee) {
        this.feeAmount = fee;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public com.faraone.sequratest.model.Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(com.faraone.sequratest.model.Merchant merchant) {
        this.merchant = merchant;
    }

    public Instant getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Instant start) {
        this.periodStart = start;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Instant end) {
        this.periodEnd = end;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
