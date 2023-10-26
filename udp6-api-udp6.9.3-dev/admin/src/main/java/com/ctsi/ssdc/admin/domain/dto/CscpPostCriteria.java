package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.admin.domain.CscpLogLoginExample;
import com.ctsi.ssdc.admin.domain.CscpMenusExample;
import com.ctsi.ssdc.admin.domain.CscpPostExample.Criteria;
import com.ctsi.ssdc.admin.domain.CscpRolesExample;
import com.ctsi.ssdc.criteria.IntegerCriteria;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.criteria.ZonedDateTimeCriteria;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class CscpPostCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongCriteria postId;
    private StringCriteria postCode;
    private StringCriteria postName;
    private IntegerCriteria postSort;
    private StringCriteria status;
    private StringCriteria createBy;
    private ZonedDateTimeCriteria createTime;
    private StringCriteria updateBy;
    private ZonedDateTimeCriteria updateTime;
    private StringCriteria remark;
    private LongCriteria tenantId;

    public LongCriteria getPostId() {
        return postId;
    }

    public void setPostId(LongCriteria postId) {
        this.postId = postId;
    }

    public StringCriteria getPostCode() {
        return postCode;
    }

    public void setPostCode(StringCriteria postCode) {
        this.postCode = postCode;
    }

    public StringCriteria getPostName() {
        return postName;
    }

    public void setPostName(StringCriteria postName) {
        this.postName = postName;
    }

    public IntegerCriteria getPostSort() {
        return postSort;
    }

    public void setPostSort(IntegerCriteria postSort) {
        this.postSort = postSort;
    }

    public StringCriteria getStatus() {
        return status;
    }

    public void setStatus(StringCriteria status) {
        this.status = status;
    }

    public StringCriteria getCreateBy() {
        return createBy;
    }

    public void setCreateBy(StringCriteria createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTimeCriteria getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTimeCriteria createTime) {
        this.createTime = createTime;
    }

    public StringCriteria getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(StringCriteria updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTimeCriteria getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTimeCriteria updateTime) {
        this.updateTime = updateTime;
    }

    public StringCriteria getRemark() {
        return remark;
    }

    public void setRemark(StringCriteria remark) {
        this.remark = remark;
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

        this.buildPostIdCriteria(criteria);
        this.buildPostCodeCriteria(criteria);
        this.buildPostNameCriteria(criteria);
        this.buildPostSortCriteria(criteria);
        this.buildStatusCriteria(criteria);
        this.buildCreateByCriteria(criteria);
        this.buildCreateTimeCriteria(criteria);
        this.buildUpdateByCriteria(criteria);
        this.buildUpdateTimeCriteria(criteria);
        this.buildRemarkCriteria(criteria);
        this.buildTenantIdCriteria(criteria);
        return criteria;
    }

    private void buildPostIdCriteria(Criteria criteria) {
        if (this.getPostId() == null) {
            return;
        }

        if (this.getPostId().getEquals() != null) {
            criteria.andPostIdEqualTo(this.getPostId().getEquals());
        } else {

            if (this.getPostId().getGreaterOrEqualThan() != null) {
                criteria.andPostIdGreaterThanOrEqualTo(this.getPostId().getGreaterOrEqualThan());
            }

            if (this.getPostId().getLessOrEqualThan() != null) {
                criteria.andPostIdLessThanOrEqualTo(this.getPostId().getLessOrEqualThan());
            }
        }
    }

    private void buildPostNameCriteria(Criteria criteria) {

        if (this.getPostName() == null) {
            return;
        }

        if (this.getPostName().getEquals() != null) {
            criteria.andPostNameEqualTo(this.getPostName().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getPostName().getContains())) {
                criteria.andPostNameLike(String.format("%%%s%%", this.getPostName().getContains()));
            }
        }
    }

    private void buildPostSortCriteria(Criteria criteria) {

        if (this.getPostSort() == null) {
            return;
        }

        if (this.getPostSort().getEquals() != null) {
            criteria.andPostSortEqualTo(this.getPostSort().getEquals());
        } else {

            if (this.getPostSort().getGreaterOrEqualThan() != null) {
                criteria.andPostSortGreaterThanOrEqualTo(this.getPostSort().getGreaterOrEqualThan());
            }

            if (this.getPostSort().getLessOrEqualThan() != null) {
                criteria.andPostSortLessThanOrEqualTo(this.getPostSort().getLessOrEqualThan());
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
                criteria.andStatusLike(String.format("%%%s%%", this.getStatus().getContains()));
            }
        }
    }

    private void buildPostCodeCriteria(Criteria criteria) {

        if (this.getPostCode() == null) {
            return;
        }

        if (this.getPostCode().getEquals() != null) {
            criteria.andPostCodeEqualTo(this.getPostCode().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getPostCode().getContains())) {
                criteria.andPostCodeLike(String.format("%%%s%%", this.getPostCode().getContains()));
            }
        }
    }

    private void buildCreateByCriteria(Criteria criteria) {

        if (this.getCreateBy() == null) {
            return;
        }

        if (this.getCreateBy().getEquals() != null) {
            criteria.andCreateByEqualTo(this.getCreateBy().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getCreateBy().getContains())) {
                criteria.andCreateByLike(String.format("%%%s%%", this.getCreateBy().getContains()));
            }
        }
    }

    private void buildCreateTimeCriteria(Criteria criteria) {

        if (this.getCreateTime() == null) {
            return;
        }

        if (this.getCreateTime().getEquals() != null) {
            criteria.andCreateTimeEqualTo(this.getCreateTime().getEquals());
        } else {

            if (this.getCreateTime().getGreaterOrEqualThan() != null) {
                criteria.andCreateTimeGreaterThanOrEqualTo(this.getCreateTime().getGreaterOrEqualThan());
            }

            if (this.getCreateTime().getLessOrEqualThan() != null) {
                criteria.andCreateTimeLessThanOrEqualTo(this.getCreateTime().getLessOrEqualThan());
            }
        }
    }

    private void buildUpdateByCriteria(Criteria criteria) {

        if (this.getUpdateBy() == null) {
            return;
        }

        if (this.getUpdateBy().getEquals() != null) {
            criteria.andUpdateByEqualTo(this.getUpdateBy().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getUpdateBy().getContains())) {
                criteria.andUpdateByLike(String.format("%%%s%%", this.getUpdateBy().getContains()));
            }
        }
    }

    private void buildUpdateTimeCriteria(Criteria criteria) {

        if (this.getUpdateTime() == null) {
            return;
        }

        if (this.getUpdateTime().getEquals() != null) {
            criteria.andUpdateTimeEqualTo(this.getUpdateTime().getEquals());
        } else {

            if (this.getUpdateTime().getGreaterOrEqualThan() != null) {
                criteria.andUpdateTimeGreaterThanOrEqualTo(this.getUpdateTime().getGreaterOrEqualThan());
            }

            if (this.getUpdateTime().getLessOrEqualThan() != null) {
                criteria.andUpdateTimeLessThanOrEqualTo(this.getUpdateTime().getLessOrEqualThan());
            }
        }
    }

    private void buildRemarkCriteria(Criteria criteria) {

        if (this.getRemark() == null) {
            return;
        }

        if (this.getRemark().getEquals() != null) {
            criteria.andRemarkEqualTo(this.getRemark().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getRemark().getContains())) {
                criteria.andRemarkLike(String.format("%%%s%%", this.getRemark().getContains()));
            }
        }
    }
    private void buildTenantIdCriteria(Criteria criteria) {
        if (this.getPostId() == null) {
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
}
