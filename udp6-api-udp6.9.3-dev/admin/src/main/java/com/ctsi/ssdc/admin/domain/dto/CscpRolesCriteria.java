package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.admin.domain.CscpRolesExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Criteria for the entity CscpRoles
 *
 * @author ctsi biyi generator
 */
public class CscpRolesCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongCriteria id;
    private LongCriteria tenantId;
    private StringCriteria name;
    private StringCriteria roleExtent;
    private LongCriteria parentId;
    private StringCriteria icon;
    private StringCriteria dataScope;
    /**
     * 角色编码
     */
    private StringCriteria roleCode;

    /**
     * 所属系统
     */
    private StringCriteria systemId;


    public StringCriteria getDataScope() {
        return dataScope;
    }

    public void setDataScope(StringCriteria dataScope) {
        this.dataScope = dataScope;
    }

    public LongCriteria getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongCriteria tenantId) {
        this.tenantId = tenantId;
    }

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

    public StringCriteria getRoleExtent() {
        return roleExtent;
    }

    public void setRoleExtent(StringCriteria roleExtent) {
        this.roleExtent = roleExtent;
    }

    public LongCriteria getParentId() {
        return parentId;
    }

    public void setParentId(LongCriteria parentId) {
        this.parentId = parentId;
    }

    public StringCriteria getIcon() {
        return icon;
    }

    public void setIcon(StringCriteria icon) {
        this.icon = icon;
    }

    public StringCriteria getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(StringCriteria roleCode) {
        this.roleCode = roleCode;
    }

    public StringCriteria getSystemId() {
        return systemId;
    }

    public void setSystemId(StringCriteria systemId) {
        this.systemId = systemId;
    }

    public Criteria buildCriteria(Criteria criteria) {
        if (criteria == null) {
            return criteria;
        }

        this.buildIdCriteria(criteria);
        this.buildTenantIdCriteria(criteria);
        this.buildNameCriteria(criteria);
        this.buildRoleExtentCriteria(criteria);
        this.buildParentIdCriteria(criteria);
        this.buildIconCriteria(criteria);
        this.buildDataScopeCriteria(criteria);
        this.buildRoleCodeCriteria(criteria);
        this.buildSystemIdCriteria(criteria);
        return criteria;
    }

    private void buildDataScopeCriteria(Criteria criteria) {

        if (this.getDataScope() == null) {
            return;
        }

        if (this.getDataScope().getEquals() != null) {
            criteria.andDataScopeEqualTo(this.getDataScope().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getDataScope().getContains())) {
                criteria.andDataScopeLike(String.format("%%%s%%", this.getDataScope().getContains()));
            }
        }
    }

    private void buildRoleCodeCriteria(Criteria criteria) {

        if (this.getRoleCode() == null) {
            return;
        }

        if (this.getRoleCode().getEquals() != null) {
            criteria.andRoleCodeEqualTo(this.getRoleCode().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getRoleCode().getContains())) {
                criteria.andRoleCodeLike(String.format("%%%s%%", this.getRoleCode().getContains()));
            }
        }
    }

    private void buildSystemIdCriteria(Criteria criteria) {

        if (this.getSystemId() == null) {
            return;
        }

        if (this.getSystemId().getEquals() != null) {
            criteria.andSystemIdEqualTo(this.getSystemId().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getSystemId().getContains())) {
                criteria.andSystemIdLike(String.format("%%%s%%", this.getSystemId().getContains()));
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

    private void buildRoleExtentCriteria(Criteria criteria) {

        if (this.getRoleExtent() == null) {
            return;
        }

        if (this.getRoleExtent().getEquals() != null) {
            criteria.andRoleExtentEqualTo(this.getRoleExtent().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getRoleExtent().getContains())) {
                criteria.andRoleExtentLike(String.format("%%%s%%", this.getRoleExtent().getContains()));
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

    private void buildIconCriteria(Criteria criteria) {

        if (this.getIcon() == null) {
            return;
        }

        if (this.getIcon().getEquals() != null) {
            criteria.andIconEqualTo(this.getIcon().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getIcon().getContains())) {
                criteria.andIconLike(String.format("%%%s%%", this.getIcon().getContains()));
            }
        }
    }

}
