package com.ctsi.ssdc.criteria;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;

public class LocalDateTimeCriteria extends RangeCriteria<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    @Override
    @DateTimeFormat(iso = ISO.DATE_TIME)
    public LocalDateTimeCriteria setEquals(LocalDateTime equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.DATE_TIME)
    public LocalDateTimeCriteria setGreaterOrEqualThan(LocalDateTime equals) {
        super.setGreaterOrEqualThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.DATE_TIME)
    public LocalDateTimeCriteria setLessOrEqualThan(LocalDateTime equals) {
        super.setLessOrEqualThan(equals);
        return this;
    }

}
