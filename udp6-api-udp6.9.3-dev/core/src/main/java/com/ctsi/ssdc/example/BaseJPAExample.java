package com.ctsi.ssdc.example;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseJPAExample<E> {

	public abstract Specification<E> createSpecification();

}
