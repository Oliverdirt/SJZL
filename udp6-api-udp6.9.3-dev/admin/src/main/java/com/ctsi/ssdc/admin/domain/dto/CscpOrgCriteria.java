package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.apache.commons.lang3.StringUtils;

import com.ctsi.ssdc.admin.domain.CscpOrgExample.Criteria;

import com.ctsi.ssdc.criteria.StringCriteria;

/**
 * Criteria for the entity CscpOrg
 *
 * @author ctsi biyi generator
 *
 */
public class CscpOrgCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private StringCriteria orgName;
	private StringCriteria description;
	private LongCriteria parentId;
	private LongCriteria orderby;

	public LongCriteria getOrderby() {
		return orderby;
	}

	public void setOrderby(LongCriteria orderby) {
		this.orderby = orderby;
	}

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public StringCriteria getOrgName() {
		return orgName;
	}

	public void setOrgName(StringCriteria orgName) {
		this.orgName = orgName;
	}

	public StringCriteria getDescription() {
		return description;
	}

	public void setDescription(StringCriteria description) {
		this.description = description;
	}

	public LongCriteria getParentId() {
		return parentId;
	}

	public void setParentId(LongCriteria parentId) {
		this.parentId = parentId;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildOrgNameCriteria(criteria);
		this.buildDescriptionCriteria(criteria);
		this.buildParentIdCriteria(criteria);
		this.buildOrgNameCriteria(criteria);

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

	private void buildOrgNameCriteria(Criteria criteria) {

		if (this.getOrgName() == null) {
			return;
		}

		if (this.getOrgName().getEquals() != null) {
			criteria.andOrgNameEqualTo(this.getOrgName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getOrgName().getContains())) {
				criteria.andOrgNameLike(String.format("%%%s%%", this.getOrgName().getContains()));
			}
		}
	}

	private void buildDescriptionCriteria(Criteria criteria) {

		if (this.getDescription() == null) {
			return;
		}

		if (this.getDescription().getEquals() != null) {
			criteria.andDescriptionEqualTo(this.getDescription().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getDescription().getContains())) {
				criteria.andDescriptionLike(
						String.format("%%%s%%", this.getDescription().getContains()));
			}
		}
	}

	private void buildParentIdCriteria(Criteria criteria) {

		if (this.getParentId() == null) {
			return;
		}

		if (this.getParentId().getEquals() != null) {
			criteria.andParentIdEqualTo(this.getParentId().getEquals());
		} else {

			if (this.getParentId().getGreaterOrEqualThan() != null) {
				criteria.andParentIdGreaterThanOrEqualTo(this.getParentId().getGreaterOrEqualThan());
			}

			if (this.getParentId().getLessOrEqualThan() != null) {
				criteria.andParentIdLessThanOrEqualTo(this.getParentId().getLessOrEqualThan());
			}
		}
	}

}
