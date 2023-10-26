package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.admin.domain.CscpUserOrgExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;

/**
 * Criteria for the entity CscpUserOrg
 *
 * @author ctsi biyi generator
 *
 */
public class CscpUserOrgCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private LongCriteria userId;
	private LongCriteria orgId;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public LongCriteria getUserId() {
		return userId;
	}

	public void setUserId(LongCriteria userId) {
		this.userId = userId;
	}

	public LongCriteria getOrgId() {
		return orgId;
	}

	public void setOrgId(LongCriteria orgId) {
		this.orgId = orgId;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildUserIdCriteria(criteria);
		this.buildOrgIdCriteria(criteria);

		return criteria;
	}

	private void buildIdCriteria(Criteria criteria) {

		if (this.getId() == null) {
			return;
		}

		if (this.getId().getEquals() != null) {
			criteria.andIdEqualTo(this.getId().getEquals());
		} else {

			if (this.getId().getGreaterOrEqualThan() != null) {
				criteria.andIdGreaterThanOrEqualTo(this.getId().getGreaterOrEqualThan());
			}

			if (this.getId().getLessOrEqualThan() != null) {
				criteria.andIdLessThanOrEqualTo(this.getId().getLessOrEqualThan());
			}
		}
	}

	private void buildUserIdCriteria(Criteria criteria) {

		if (this.getUserId() == null) {
			return;
		}

		if (this.getUserId().getEquals() != null) {
			criteria.andUserIdEqualTo(this.getUserId().getEquals());
		} else {

			if (this.getUserId().getGreaterOrEqualThan() != null) {
				criteria.andUserIdGreaterThanOrEqualTo(this.getUserId().getGreaterOrEqualThan());
			}

			if (this.getUserId().getLessOrEqualThan() != null) {
				criteria.andUserIdLessThanOrEqualTo(this.getUserId().getLessOrEqualThan());
			}
		}
	}

	private void buildOrgIdCriteria(Criteria criteria) {

		if (this.getOrgId() == null) {
			return;
		}

		if (this.getOrgId().getEquals() != null) {
			criteria.andOrgIdEqualTo(this.getOrgId().getEquals());
		} else {

			if (this.getOrgId().getGreaterOrEqualThan() != null) {
				criteria.andOrgIdGreaterThanOrEqualTo(this.getOrgId().getGreaterOrEqualThan());
			}

			if (this.getOrgId().getLessOrEqualThan() != null) {
				criteria.andOrgIdLessThanOrEqualTo(this.getOrgId().getLessOrEqualThan());
			}
		}
	}

}
