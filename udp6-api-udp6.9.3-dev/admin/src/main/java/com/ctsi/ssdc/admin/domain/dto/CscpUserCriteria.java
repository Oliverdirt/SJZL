package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.apache.commons.lang3.StringUtils;

import com.ctsi.ssdc.admin.domain.CscpUserExample.Criteria;
import com.ctsi.ssdc.criteria.StringCriteria;

/**
 * Criteria for the entity CscpUser
 *
 * @author ctsi biyi generator
 *
 */
public class CscpUserCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private StringCriteria username;
	private StringCriteria password;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public StringCriteria getUsername() {
		return username;
	}

	public void setUsername(StringCriteria username) {
		this.username = username;
	}

	public StringCriteria getPassword() {
		return password;
	}

	public void setPassword(StringCriteria password) {
		this.password = password;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildUsernameCriteria(criteria);
		this.buildPasswordCriteria(criteria);

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

	private void buildUsernameCriteria(Criteria criteria) {

		if (this.getUsername() == null) {
			return;
		}

		if (this.getUsername().getEquals() != null) {
			criteria.andUsernameEqualTo(this.getUsername().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getUsername().getContains())) {
				criteria.andUsernameLike(String.format("%%%s%%", this.getUsername().getContains()));
			}
		}
	}

	private void buildPasswordCriteria(Criteria criteria) {

		if (this.getPassword() == null) {
			return;
		}

		if (this.getPassword().getEquals() != null) {
			criteria.andPasswordEqualTo(this.getPassword().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getPassword().getContains())) {
				criteria.andPasswordLike(String.format("%%%s%%", this.getPassword().getContains()));
			}
		}
	}

}
