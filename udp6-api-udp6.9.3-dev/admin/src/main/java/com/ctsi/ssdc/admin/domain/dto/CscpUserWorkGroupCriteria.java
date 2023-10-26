package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.admin.domain.CscpUserWorkGroupExample.Criteria;

import com.ctsi.ssdc.criteria.LongCriteria;

/**
 * Criteria for the entity CscpUserWorkGroup
 *
 * @author ctsi biyi generator
 *
 */
public class CscpUserWorkGroupCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private LongCriteria userId;
	private LongCriteria groupId;

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

	public LongCriteria getGroupId() {
		return groupId;
	}

	public void setGroupId(LongCriteria groupId) {
		this.groupId = groupId;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}
		this.buildIdCriteria(criteria);
		this.buildUserIdCriteria(criteria);
		this.buildGroupIdCriteria(criteria);

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

}
