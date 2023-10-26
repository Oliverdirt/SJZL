package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrgExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;

/**
 * Criteria for the entity CscpWorkGroupOrg
 *
 * @author ctsi biyi generator
 *
 */
public class CscpWorkGroupOrgCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private LongCriteria groupId;
	private LongCriteria orgId;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public LongCriteria getGroupId() {
		return groupId;
	}

	public void setGroupId(LongCriteria groupId) {
		this.groupId = groupId;
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
		this.buildGroupIdCriteria(criteria);
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

	private void buildGroupIdCriteria(Criteria criteria) {

		if (this.getGroupId() == null) {
			return;
		}

		if (this.getGroupId().getEquals() != null) {
			criteria.andGroupIdEqualTo(this.getGroupId().getEquals());
		} else {

			if (this.getGroupId().getGreaterOrEqualThan() != null) {
				criteria.andGroupIdGreaterThanOrEqualTo(this.getGroupId().getGreaterOrEqualThan());
			}

			if (this.getGroupId().getLessOrEqualThan() != null) {
				criteria.andGroupIdLessThanOrEqualTo(this.getGroupId().getLessOrEqualThan());
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
