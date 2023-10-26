package com.ctsi.ssdc.model;

public class Roles {
    private Integer id;
    private String name;
    private String roleExtent;
    private Integer parentId;
    private String icon;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
}
