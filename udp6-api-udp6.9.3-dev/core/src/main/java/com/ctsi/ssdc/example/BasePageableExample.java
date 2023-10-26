package com.ctsi.ssdc.example;

import org.springframework.data.domain.Pageable;

public abstract class BasePageableExample {

	protected Pageable pageable;

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public String buildOrderByClause() {
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

	public abstract Object buildCriteria();
}
