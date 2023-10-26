package com.ctsi.ssdc.admin.domain.dto;

import java.io.Serializable;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.apache.commons.lang3.StringUtils;

import com.ctsi.ssdc.admin.domain.CscpMenusExample.Criteria;
import com.ctsi.ssdc.criteria.IntegerCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;

/**
 * Criteria for the entity CscpMenus
 *
 * @author ctsi biyi generator
 *
 */
public class CscpMenusCriteria implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LIKE_FORMAT = "%%%s%%";

	private LongCriteria id;
	private StringCriteria name;
	private StringCriteria icon;
	private StringCriteria title;
	private StringCriteria url;
	private StringCriteria httpMethod;
	private StringCriteria component;
	private LongCriteria parentId;
	private StringCriteria type;
	private StringCriteria permissionCode;
	private IntegerCriteria orderby;

	public LongCriteria getId() {
		return id;
	}

	public void setId(LongCriteria id) {
		this.id = id;
	}

	public StringCriteria getName() {
		return name;
	}

	public void setName(StringCriteria name) {
		this.name = name;
	}

	public StringCriteria getIcon() {
		return icon;
	}

	public void setIcon(StringCriteria icon) {
		this.icon = icon;
	}

	public StringCriteria getTitle() {
		return title;
	}

	public void setTitle(StringCriteria title) {
		this.title = title;
	}

	public StringCriteria getUrl() {
		return url;
	}

	public void setUrl(StringCriteria url) {
		this.url = url;
	}

	public StringCriteria getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(StringCriteria httpMethod) {
		this.httpMethod = httpMethod;
	}

	public StringCriteria getComponent() {
		return component;
	}

	public void setComponent(StringCriteria component) {
		this.component = component;
	}

	public LongCriteria getParentId() {
		return parentId;
	}

	public void setParentId(LongCriteria parentId) {
		this.parentId = parentId;
	}

	public StringCriteria getType() {
		return type;
	}

	public void setType(StringCriteria type) {
		this.type = type;
	}

	public StringCriteria getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(StringCriteria permissionCode) {
		this.permissionCode = permissionCode;
	}

	public IntegerCriteria getOrderby() {
		return orderby;
	}

	public void setOrderby(IntegerCriteria orderby) {
		this.orderby = orderby;
	}

	public Criteria buildCriteria(Criteria criteria) {
		if (criteria == null) {
			return criteria;
		}

		this.buildIdCriteria(criteria);
		this.buildNameCriteria(criteria);
		this.buildIconCriteria(criteria);
		this.buildTitleCriteria(criteria);
		this.buildUrlCriteria(criteria);
		this.buildHttpMethodCriteria(criteria);
		this.buildComponentCriteria(criteria);
		this.buildParentIdCriteria(criteria);
		this.buildTypeCriteria(criteria);
		this.buildPermissionCodeCriteria(criteria);
		this.buildOrderbyCriteria(criteria);

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

	private void buildNameCriteria(Criteria criteria) {

		if (this.getName() == null) {
			return;
		}

		if (this.getName().getEquals() != null) {
			criteria.andNameEqualTo(this.getName().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getName().getContains())) {
				criteria.andNameLike(String.format(LIKE_FORMAT, this.getName().getContains()));
			}
		}
	}

	private void buildIconCriteria(Criteria criteria) {

		if (this.getIcon() == null) {
			return;
		}

		if (this.getIcon().getEquals() != null) {
			criteria.andIconEqualTo(this.getIcon().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getIcon().getContains())) {
				criteria.andIconLike(String.format(LIKE_FORMAT, this.getIcon().getContains()));
			}
		}
	}

	private void buildTitleCriteria(Criteria criteria) {

		if (this.getTitle() == null) {
			return;
		}

		if (this.getTitle().getEquals() != null) {
			criteria.andTitleEqualTo(this.getTitle().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getTitle().getContains())) {
				criteria.andTitleLike(String.format(LIKE_FORMAT, this.getTitle().getContains()));
			}
		}
	}

	private void buildUrlCriteria(Criteria criteria) {

		if (this.getUrl() == null) {
			return;
		}

		if (this.getUrl().getEquals() != null) {
			criteria.andUrlEqualTo(this.getUrl().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getUrl().getContains())) {
				criteria.andUrlLike(String.format(LIKE_FORMAT, this.getUrl().getContains()));
			}
		}
	}

	private void buildHttpMethodCriteria(Criteria criteria) {

		if (this.getHttpMethod() == null) {
			return;
		}

		if (this.getHttpMethod().getEquals() != null) {
			criteria.andHttpMethodEqualTo(this.getHttpMethod().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getHttpMethod().getContains())) {
				criteria.andHttpMethodLike(
						String.format(LIKE_FORMAT, this.getHttpMethod().getContains()));
			}
		}
	}

	private void buildComponentCriteria(Criteria criteria) {

		if (this.getComponent() == null) {
			return;
		}

		if (this.getComponent().getEquals() != null) {
			criteria.andComponentEqualTo(this.getComponent().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getComponent().getContains())) {
				criteria.andComponentLike(
						String.format(LIKE_FORMAT, this.getComponent().getContains()));
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

	private void buildTypeCriteria(Criteria criteria) {

		if (this.getType() == null) {
			return;
		}

		if (this.getType().getEquals() != null) {
			criteria.andTypeEqualTo(this.getType().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getType().getContains())) {
				criteria.andTypeLike(String.format(LIKE_FORMAT, this.getType().getContains()));
			}
		}
	}

	private void buildPermissionCodeCriteria(Criteria criteria) {

		if (this.getPermissionCode() == null) {
			return;
		}

		if (this.getPermissionCode().getEquals() != null) {
			criteria.andPermissionCodeEqualTo(this.getPermissionCode().getEquals());
		} else {
			if (StringUtils.isNotBlank(this.getPermissionCode().getContains())) {
				criteria.andPermissionCodeLike(
						String.format(LIKE_FORMAT, this.getPermissionCode().getContains()));
			}
		}
	}

	private void buildOrderbyCriteria(Criteria criteria) {

		if (this.getOrderby() == null) {
			return;
		}

		if (this.getOrderby().getEquals() != null) {
			criteria.andOrderbyEqualTo(this.getOrderby().getEquals());
		} else {

			if (this.getOrderby().getGreaterOrEqualThan() != null) {
				criteria.andOrderbyGreaterThanOrEqualTo(this.getOrderby().getGreaterOrEqualThan());
			}

			if (this.getOrderby().getLessOrEqualThan() != null) {
				criteria.andOrderbyLessThanOrEqualTo(this.getOrderby().getLessOrEqualThan());
			}
		}
	}

}
