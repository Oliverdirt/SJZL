package com.ctsi.ssdc.criteria;

public class StringCriteria extends FieldCriteria<String> {

    private static final long serialVersionUID = 1L;

    private String contains;

    public String getContains() {
        return contains;
    }

    public StringCriteria setContains(String contains) {
        this.contains = contains;
        return this;
    }

    @Override
    public String toString() {
        return getCriteriaName() + " ["
            + (getContains() != null ? "contains=" + getContains() + ", " : "")
            + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
            + "]";
    }

}
