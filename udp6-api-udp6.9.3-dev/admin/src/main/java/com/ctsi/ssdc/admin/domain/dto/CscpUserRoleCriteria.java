package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.admin.domain.CscpUserRoleExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;

/**
 * Criteria for the entity CscpUserRole
 *
 * @author ctsi biyi generator
 *
 */
public class CscpUserRoleCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private LongCriteria userId;
	private LongCriteria roleId;

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

	public LongCriteria getRoleId() {
		return roleId;
	}

	public void setRoleId(LongCriteria roleId) {
		this.roleId = roleId;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildUserIdCriteria(criteria);
		this.buildRoleIdCriteria(criteria);

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

	private void buildRoleIdCriteria(Criteria criteria) {

		if (this.getRoleId() == null) {
			return;
		}

		if (this.getRoleId().getEquals() != null) {
			criteria.andRoleIdEqualTo(this.getRoleId().getEquals());
		} else {

			if (this.getRoleId().getGreaterOrEqualThan() != null) {
				criteria.andRoleIdGreaterThanOrEqualTo(this.getRoleId().getGreaterOrEqualThan());
			}

			if (this.getRoleId().getLessOrEqualThan() != null) {
				criteria.andRoleIdLessThanOrEqualTo(this.getRoleId().getLessOrEqualThan());
			}
		}
	}

}
