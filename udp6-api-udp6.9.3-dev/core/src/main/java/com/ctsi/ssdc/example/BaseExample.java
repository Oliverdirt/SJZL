package com.ctsi.ssdc.example;

import org.springframework.data.domain.Pageable;

public abstract class BaseExample {

	public String buildOrderByClause(Pageable pageable) {
		if (pageable != null && pageable.getSort() != null) {
			StringBuilder sb = new StringBuilder();
			pageable.getSort()
					.forEach(sort -> sb.append(sort.getProperty()).append(" ").append(sort.getDirection()).append(","));
			if (sb.length() > 1) {
				return sb.substring(0, sb.length() - 1);
			}
		}
		return null;
	}

	public abstract void setOrderByClause(String orderByClause);

	
	public abstract Object buildCriteria();
}
