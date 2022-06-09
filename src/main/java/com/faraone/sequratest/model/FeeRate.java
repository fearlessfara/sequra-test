package com.faraone.sequratest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class FeeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private BigDecimal rangeStart;

    @Column
    private BigDecimal rangeEnd;

    @Column
    private BigDecimal rate;

    public FeeRate() {
        //hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(BigDecimal from) {
        this.rangeStart = from;
    }

    public BigDecimal getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(BigDecimal to) {
        this.rangeEnd = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
