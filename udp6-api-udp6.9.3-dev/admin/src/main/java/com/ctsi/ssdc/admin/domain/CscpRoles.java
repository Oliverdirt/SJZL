package com.ctsi.ssdc.admin.domain;


import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author MyBatis Generator
*/
public class CscpRoles implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.id
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.name
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.role_extent
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    private String roleExtent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.parent_id
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.icon
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    private String icon;


    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long tenantId;
    /** 部门组（数据权限） */
    private Long[] deptIds;

    public Long[] getDeptIds() {
        Long[] temp = deptIds;
        return temp;
    }

    public void setDeptIds(Long[] deptIds) {
        Long[] temp = deptIds;
        this.deptIds = temp.clone();
    }

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_roles.data_scope
     *
     * @mbg.generated Tue Jan 11 10:03:44 CST 2022
     */
    private String dataScope;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_roles
     *
     * @mbg.generated Mon May 07 08:50:31 CST 2018
     */
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleExtent() {
        return roleExtent;
    }

    public void setRoleExtent(String roleExtent) {
        this.roleExtent = roleExtent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public static long getSerialVersionUid() {
        return serialVersionUID;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_roles.data_scope
     *
     * @return the value of cscp_roles.data_scope
     *
     * @mbg.generated Tue Jan 11 10:03:44 CST 2022
     */
    public String getDataScope() {
        return dataScope;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_roles.data_scope
     *
     * @param dataScope the value for cscp_roles.data_scope
     *
     * @mbg.generated Tue Jan 11 10:03:44 CST 2022
     */
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope == null ? null : dataScope.trim();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CscpRoles)) {
            return false;
        }
        CscpRoles cscpRoles = (CscpRoles) o;
        return Objects.equals(id, cscpRoles.id) &&
                Objects.equals(name, cscpRoles.name) &&
                Objects.equals(roleExtent, cscpRoles.roleExtent) &&
                Objects.equals(parentId, cscpRoles.parentId) &&
                Objects.equals(icon, cscpRoles.icon)&&
                Objects.equals(dataScope, cscpRoles.dataScope)&&
                Objects.equals(tenantId, cscpRoles.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roleExtent, parentId, icon,tenantId,dataScope);
    }
}