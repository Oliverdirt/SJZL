package com.ctsi.ssdc.criteria;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalTime;

public class LocalTimeCriteria extends RangeCriteria<LocalTime> {

    private static final long serialVersionUID = 1L;

    @Override
    @DateTimeFormat(iso = ISO.TIME)
    public LocalTimeCriteria setEquals(LocalTime equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.TIME)
    public LocalTimeCriteria setGreaterOrEqualThan(LocalTime equals) {
        super.setGreaterOrEqualThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.TIME)
    public LocalTimeCriteria setLessOrEqualThan(LocalTime equals) {
        super.setLessOrEqualThan(equals);
        return this;
    }

}
