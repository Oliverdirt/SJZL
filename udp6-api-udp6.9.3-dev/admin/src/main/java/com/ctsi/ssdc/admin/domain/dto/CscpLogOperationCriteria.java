package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.admin.domain.CscpLogLoginExample;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.criteria.ZonedDateTimeCriteria;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Criteria for the entity CscpLogOperation
 *
 * @author ctsi biyi generator
 *
 */
public class CscpLogOperationCriteria implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LIKE_FORMAT = "%%%s%%";

	private LongCriteria id;
	private LongCriteria tenantId;
	private LongCriteria userid;
	private StringCriteria username;
	private StringCriteria uri;
	private StringCriteria ip;
	private StringCriteria params;
	private StringCriteria method;
	private StringCriteria message;
	private StringCriteria status;
	private ZonedDateTimeCriteria time;
	private StringCriteria error;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public LongCriteria getUserid() {
		return userid;
	}

	public void setUserid(LongCriteria userid) {
		this.userid = userid;
	}

	public StringCriteria getUsername() {
		return username;
	}

	public void setUsername(StringCriteria username) {
		this.username = username;
	}

	public StringCriteria getUri() {
		return uri;
	}

	public void setUri(StringCriteria uri) {
		this.uri = uri;
	}

	public StringCriteria getIp() {
		return ip;
	}

	public void setIp(StringCriteria ip) {
		this.ip = ip;
	}

	public StringCriteria getParams() {
		return params;
	}

	public void setParams(StringCriteria params) {
		this.params = params;
	}

	public StringCriteria getMethod() {
		return method;
	}

	public void setMethod(StringCriteria method) {
		this.method = method;
	}

	public StringCriteria getMessage() {
		return message;
	}

	public void setMessage(StringCriteria message) {
		this.message = message;
	}

	public StringCriteria getStatus() {
		return status;
	}

	public void setStatus(StringCriteria status) {
		this.status = status;
	}

	public ZonedDateTimeCriteria getTime() {
		return time;
	}

	public void setTime(ZonedDateTimeCriteria time) {
		this.time = time;
	}

	public StringCriteria getError() {
		return error;
	}

	public void setError(StringCriteria error) {
		this.error = error;
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
		this.buildUseridCriteria(criteria);
		this.buildUsernameCriteria(criteria);
		this.buildUriCriteria(criteria);
		this.buildIpCriteria(criteria);
		this.buildParamsCriteria(criteria);
		this.buildMethodCriteria(criteria);
		this.buildMessageCriteria(criteria);
		this.buildStatusCriteria(criteria);
		this.buildTimeCriteria(criteria);
		this.buildErrorCriteria(criteria);

		return criteria;
	}
	private void buildTenantIdCriteria(CscpLogLoginExample.Criteria criteria) {
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

	private void buildUseridCriteria(Criteria criteria) {

		if (this.getUserid() == null) {
			return;
		}

		if (this.getUserid().getEquals() != null) {
			criteria.andUseridEqualTo(this.getUserid().getEquals());
		} else {

			if (this.getUserid().getGreaterOrEqualThan() != null) {
				criteria.andUseridGreaterThanOrEqualTo(this.getUserid().getGreaterOrEqualThan());
			}

			if (this.getUserid().getLessOrEqualThan() != null) {
				criteria.andUseridLessThanOrEqualTo(this.getUserid().getLessOrEqualThan());
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
				criteria.andUsernameLike(String.format(LIKE_FORMAT, this.getUsername().getContains()));
			}
		}
	}

	private void buildUriCriteria(Criteria criteria) {

		if (this.getUri() == null) {
			return;
		}

		if (this.getUri().getEquals() != null) {
			criteria.andUriEqualTo(this.getUri().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getUri().getContains())) {
				criteria.andUriLike(String.format(LIKE_FORMAT, this.getUri().getContains()));
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

	private void buildParamsCriteria(Criteria criteria) {

		if (this.getParams() == null) {
			return;
		}

		if (this.getParams().getEquals() != null) {
			criteria.andParamsEqualTo(this.getParams().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getParams().getContains())) {
				criteria.andParamsLike(String.format(LIKE_FORMAT, this.getParams().getContains()));
			}
		}
	}

	private void buildMethodCriteria(Criteria criteria) {

		if (this.getMethod() == null) {
			return;
		}

		if (this.getMethod().getEquals() != null) {
			criteria.andMethodEqualTo(this.getMethod().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getMethod().getContains())) {
				criteria.andMethodLike(String.format(LIKE_FORMAT, this.getMethod().getContains()));
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

	private void buildErrorCriteria(Criteria criteria) {

		if (this.getError() == null) {
			return;
		}

		if (this.getError().getEquals() != null) {
			criteria.andErrorEqualTo(this.getError().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getError().getContains())) {
				criteria.andErrorLike(String.format(LIKE_FORMAT, this.getError().getContains()));
			}
		}
	}

}
