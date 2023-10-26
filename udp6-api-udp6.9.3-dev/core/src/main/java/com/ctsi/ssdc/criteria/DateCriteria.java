package com.ctsi.ssdc.criteria;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.Date;

public class DateCriteria extends RangeCriteria<Date> {

    private static final long serialVersionUID = 1L;

    @Override
    @DateTimeFormat(iso = ISO.DATE)
    public DateCriteria setEquals(Date equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.DATE)
    public DateCriteria setGreaterOrEqualThan(Date equals) {
        super.setGreaterOrEqualThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = ISO.DATE)
    public DateCriteria setLessOrEqualThan(Date equals) {
        super.setLessOrEqualThan(equals);
        return this;
    }

}
