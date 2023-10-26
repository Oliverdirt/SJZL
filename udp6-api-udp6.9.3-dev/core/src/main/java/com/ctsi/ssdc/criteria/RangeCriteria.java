package com.ctsi.ssdc.criteria;

public class RangeCriteria<T extends Comparable<? super T>> extends FieldCriteria<T> {

    private static final long serialVersionUID = 1L;

    private T greaterOrEqualThan;
    private T lessOrEqualThan;
    
	public T getGreaterOrEqualThan() {
		return greaterOrEqualThan;
	}

	public RangeCriteria<T> setGreaterOrEqualThan(T greaterOrEqualThan) {
		this.greaterOrEqualThan = greaterOrEqualThan;
		return this;
	}

	public T getLessOrEqualThan() {
		return lessOrEqualThan;
	}

	public RangeCriteria<T> setLessOrEqualThan(T lessOrEqualThan) {
		this.lessOrEqualThan = lessOrEqualThan;
		return this;
	}

	@Override
    public String toString() {
        return getCriteriaName() + " ["
        	+ (getGreaterOrEqualThan() != null ? "greaterOrEqualThan=" + getGreaterOrEqualThan() + ", " : "")
            + (getLessOrEqualThan() != null ? "lessOrEqualThan=" + getLessOrEqualThan() + ", " : "")
            + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
            + "]";
    }

}
