package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.admin.domain.CscpLogLoginExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.criteria.ZonedDateTimeCriteria;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Criteria for the entity CscpLogLogin
 *
 * @author ctsi biyi generator
 *
 */
public class CscpLogLoginCriteria implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LIKE_FORMAT = "%%%s%%";

	private LongCriteria id;
	private LongCriteria tenantId;
	private StringCriteria userName;
	private StringCriteria ip;
	private StringCriteria message;
	private ZonedDateTimeCriteria time;
	private StringCriteria status;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public StringCriteria getUserName() {
		return userName;
	}

	public void setUserName(StringCriteria userName) {
		this.userName = userName;
	}

	public StringCriteria getIp() {
		return ip;
	}

	public void setIp(StringCriteria ip) {
		this.ip = ip;
	}

	public StringCriteria getMessage() {
		return message;
	}

	public void setMessage(StringCriteria message) {
		this.message = message;
	}

	public ZonedDateTimeCriteria getTime() {
		return time;
	}

	public void setTime(ZonedDateTimeCriteria time) {
		this.time = time;
	}

	public StringCriteria getStatus() {
		return status;
	}

	public void setStatus(StringCriteria status) {
		this.status = status;
	}

	public LongCriteria getTenantId() {
		return tenantId;
	}

	public void setTenantId(LongCriteria tenantId) {
		this.tenantId = tenantId;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildTenantIdCriteria(criteria);
		this.buildUserNameCriteria(criteria);
		this.buildIpCriteria(criteria);
		this.buildMessageCriteria(criteria);
		this.buildTimeCriteria(criteria);
		this.buildStatusCriteria(criteria);

		return criteria;
	}

	private void buildTenantIdCriteria(Criteria criteria) {
		if (this.getTenantId() == null) {
			return;
		}

		if (this.getTenantId().getEquals() != null) {
			criteria.andTenantIdEqualTo(this.getTenantId().getEquals());
		} else {

			if (this.getTenantId().getGreaterOrEqualThan() != null) {
				criteria.andTenantIdGreaterThanOrEqualTo(this.getTenantId().getGreaterOrEqualThan());
			}

			if (this.getTenantId().getLessOrEqualThan() != null) {
				criteria.andTenantIdLessThanOrEqualTo(this.getTenantId().getLessOrEqualThan());
			}
		}
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

	private void buildUserNameCriteria(Criteria criteria) {

		if (this.getUserName() == null) {
			return;
		}

		if (this.getUserName().getEquals() != null) {
			criteria.andUserNameEqualTo(this.getUserName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getUserName().getContains())) {
				criteria.andUserNameLike(String.format(LIKE_FORMAT, this.getUserName().getContains()));
			}
		}
	}

	private void buildIpCriteria(Criteria criteria) {

		if (this.getIp() == null) {
			return;
		}

		if (this.getIp().getEquals() != null) {
			criteria.andIpEqualTo(this.getIp().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getIp().getContains())) {
				criteria.andIpLike(String.format(LIKE_FORMAT, this.getIp().getContains()));
			}
		}
	}

	private void buildMessageCriteria(Criteria criteria) {

		if (this.getMessage() == null) {
			return;
		}

		if (this.getMessage().getEquals() != null) {
			criteria.andMessageEqualTo(this.getMessage().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getMessage().getContains())) {
				criteria.andMessageLike(String.format(LIKE_FORMAT, this.getMessage().getContains()));
			}
		}
	}

	private void buildTimeCriteria(Criteria criteria) {

		if (this.getTime() == null) {
			return;
		}

		if (this.getTime().getEquals() != null) {
			criteria.andTimeEqualTo(this.getTime().getEquals());
		} else {

			if (this.getTime().getGreaterOrEqualThan() != null) {
				criteria.andTimeGreaterThanOrEqualTo(this.getTime().getGreaterOrEqualThan());
			}

			if (this.getTime().getLessOrEqualThan() != null) {
				criteria.andTimeLessThanOrEqualTo(this.getTime().getLessOrEqualThan());
			}
		}
	}

	private void buildStatusCriteria(Criteria criteria) {

		if (this.getStatus() == null) {
			return;
		}

		if (this.getStatus().getEquals() != null) {
			criteria.andStatusEqualTo(this.getStatus().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getStatus().getContains())) {
				criteria.andStatusLike(String.format(LIKE_FORMAT, this.getStatus().getContains()));
			}
		}
	}

}
