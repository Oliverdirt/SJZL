package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.apache.commons.lang3.StringUtils;

import com.ctsi.ssdc.admin.domain.CscpWorkGroupExample.Criteria;
import com.ctsi.ssdc.criteria.StringCriteria;

/**
 * Criteria for the entity CscpWorkGroup
 *
 * @author ctsi biyi generator
 *
 */
public class CscpWorkGroupCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private StringCriteria groupName;
	private StringCriteria description;
	private LongCriteria orgId;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public StringCriteria getGroupName() {
		return groupName;
	}

	public void setGroupName(StringCriteria groupName) {
		this.groupName = groupName;
	}

	public StringCriteria getDescription() {
		return description;
	}

	public void setDescription(StringCriteria description) {
		this.description = description;
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
		this.buildGroupNameCriteria(criteria);
		this.buildDescriptionCriteria(criteria);
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

	private void buildGroupNameCriteria(Criteria criteria) {

		if (this.getGroupName() == null) {
			return;
		}

		if (this.getGroupName().getEquals() != null) {
			criteria.andGroupNameEqualTo(this.getGroupName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getGroupName().getContains())) {
				criteria.andGroupNameLike(String.format("%%%s%%", this.getGroupName().getContains()));
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
