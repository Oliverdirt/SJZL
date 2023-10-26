package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.apache.commons.lang3.StringUtils;

import com.ctsi.ssdc.admin.domain.CscpUserDetailExample.Criteria;
import com.ctsi.ssdc.criteria.DateCriteria;

import com.ctsi.ssdc.criteria.StringCriteria;

/**
 * Criteria for the entity CscpUserDetail
 *
 * @author ctsi biyi generator
 *
 */
public class CscpUserDetailCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private LongCriteria id;
	private LongCriteria userId;
	private StringCriteria familyName;
	private StringCriteria name;
	private StringCriteria mobile;
	private StringCriteria email;
	private StringCriteria imgPath;
	private DateCriteria lastLogin;
	private StringCriteria discTitle;
	private StringCriteria discDetail;
	private DateCriteria registerTime;
	private StringCriteria adminFlag;

	public StringCriteria getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(StringCriteria adminFlag) {
		this.adminFlag = adminFlag;
	}

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

	public StringCriteria getFamilyName() {
		return familyName;
	}

	public void setFamilyName(StringCriteria familyName) {
		this.familyName = familyName;
	}

	public StringCriteria getName() {
		return name;
	}

	public void setName(StringCriteria name) {
		this.name = name;
	}

	public StringCriteria getMobile() {
		return mobile;
	}

	public void setMobile(StringCriteria mobile) {
		this.mobile = mobile;
	}

	public StringCriteria getEmail() {
		return email;
	}

	public void setEmail(StringCriteria email) {
		this.email = email;
	}

	public StringCriteria getImgPath() {
		return imgPath;
	}

	public void setImgPath(StringCriteria imgPath) {
		this.imgPath = imgPath;
	}

	public DateCriteria getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(DateCriteria lastLogin) {
		this.lastLogin = lastLogin;
	}

	public StringCriteria getDiscTitle() {
		return discTitle;
	}

	public void setDiscTitle(StringCriteria discTitle) {
		this.discTitle = discTitle;
	}

	public StringCriteria getDiscDetail() {
		return discDetail;
	}

	public void setDiscDetail(StringCriteria discDetail) {
		this.discDetail = discDetail;
	}

	public DateCriteria getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(DateCriteria registerTime) {
		this.registerTime = registerTime;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildUserIdCriteria(criteria);
		this.buildFamilyNameCriteria(criteria);
		this.buildNameCriteria(criteria);
		this.buildMobileCriteria(criteria);
		this.buildEmailCriteria(criteria);
		this.buildImgPathCriteria(criteria);
		this.buildLastLoginCriteria(criteria);
		this.buildDiscTitleCriteria(criteria);
		this.buildDiscDetailCriteria(criteria);
		this.buildRegisterTimeCriteria(criteria);
		this.buildAdminFlagCriteria(criteria);
		return criteria;
	}

	private void buildAdminFlagCriteria(Criteria criteria) {

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

	private void buildFamilyNameCriteria(Criteria criteria) {

		if (this.getFamilyName() == null) {
			return;
		}

		if (this.getFamilyName().getEquals() != null) {
			criteria.andFamilyNameEqualTo(this.getFamilyName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getFamilyName().getContains())) {
				criteria.andFamilyNameLike(String.format("%%%s%%", this.getFamilyName().getContains()));
			}
		}
	}

	private void buildNameCriteria(Criteria criteria) {

		if (this.getName() == null) {
			return;
		}

		if (this.getName().getEquals() != null) {
			criteria.andNameEqualTo(this.getName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getName().getContains())) {
				criteria.andNameLike(String.format("%%%s%%", this.getName().getContains()));
			}
		}
	}

	private void buildMobileCriteria(Criteria criteria) {

		if (this.getMobile() == null) {
			return;
		}

		if (this.getMobile().getEquals() != null) {
			criteria.andMobileEqualTo(this.getMobile().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getMobile().getContains())) {
				criteria.andMobileLike(String.format("%%%s%%", this.getMobile().getContains()));
			}
		}
	}

	private void buildEmailCriteria(Criteria criteria) {

		if (this.getEmail() == null) {
			return;
		}

		if (this.getEmail().getEquals() != null) {
			criteria.andEmailEqualTo(this.getEmail().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getEmail().getContains())) {
				criteria.andEmailLike(String.format("%%%s%%", this.getEmail().getContains()));
			}
		}
	}

	private void buildImgPathCriteria(Criteria criteria) {

		if (this.getImgPath() == null) {
			return;
		}

		if (this.getImgPath().getEquals() != null) {
			criteria.andImgPathEqualTo(this.getImgPath().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getImgPath().getContains())) {
				criteria.andImgPathLike(String.format("%%%s%%", this.getImgPath().getContains()));
			}
		}
	}

	private void buildLastLoginCriteria(Criteria criteria) {

		if (this.getLastLogin() == null) {
			return;
		}

		if (this.getLastLogin().getEquals() != null) {
			criteria.andLastLoginEqualTo(this.getLastLogin().getEquals());
		} else {

			if (this.getLastLogin().getGreaterOrEqualThan() != null) {
				criteria.andLastLoginGreaterThanOrEqualTo(this.getLastLogin().getGreaterOrEqualThan());
			}

			if (this.getLastLogin().getLessOrEqualThan() != null) {
				criteria.andLastLoginLessThanOrEqualTo(this.getLastLogin().getLessOrEqualThan());
			}
		}
	}

	private void buildDiscTitleCriteria(Criteria criteria) {

		if (this.getDiscTitle() == null) {
			return;
		}

		if (this.getDiscTitle().getEquals() != null) {
			criteria.andDiscTitleEqualTo(this.getDiscTitle().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getDiscTitle().getContains())) {
				criteria.andDiscTitleLike(String.format("%%%s%%", this.getDiscTitle().getContains()));
			}
		}
	}

	private void buildDiscDetailCriteria(Criteria criteria) {

		if (this.getDiscDetail() == null) {
			return;
		}

		if (this.getDiscDetail().getEquals() != null) {
			criteria.andDiscDetailEqualTo(this.getDiscDetail().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getDiscDetail().getContains())) {
				criteria.andDiscDetailLike(String.format("%%%s%%", this.getDiscDetail().getContains()));
			}
		}
	}

	private void buildRegisterTimeCriteria(Criteria criteria) {

		if (this.getRegisterTime() == null) {
			return;
		}

		if (this.getRegisterTime().getEquals() != null) {
			criteria.andRegisterTimeEqualTo(this.getRegisterTime().getEquals());
		} else {

			if (this.getRegisterTime().getGreaterOrEqualThan() != null) {
				criteria.andRegisterTimeGreaterThanOrEqualTo(
						this.getRegisterTime().getGreaterOrEqualThan());
			}

			if (this.getRegisterTime().getLessOrEqualThan() != null) {
				criteria.andRegisterTimeLessThanOrEqualTo(this.getRegisterTime().getLessOrEqualThan());
			}
		}
	}

}
