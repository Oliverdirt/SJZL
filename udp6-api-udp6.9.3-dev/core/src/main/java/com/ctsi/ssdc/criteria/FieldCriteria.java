package com.ctsi.ssdc.criteria;

import java.io.Serializable;


public class FieldCriteria<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private T equals;

    public T getEquals() {
        return equals;
    }

    public FieldCriteria<T> setEquals(T equals) {
        this.equals = equals;
        return this;
    }

   
    @Override
    public String toString() {
        return getCriteriaName() + " ["
            + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
            + "]";
    }

    protected String getCriteriaName() {
        return this.getClass().getSimpleName();
    }
}
