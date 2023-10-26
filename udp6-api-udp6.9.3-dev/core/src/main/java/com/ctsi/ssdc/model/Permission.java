package com.ctsi.ssdc.model;

public class Permission {

    private Integer id;
    private String name;
    private String code;
    private String icon;
    private String pclas;
    private Integer orderby;
    private Integer menuId;
    private boolean select = false;
    
    
    public Permission(Integer id, String name, String code, String icon, String pclas, Integer orderby,
            Integer menuId) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.icon = icon;
        this.pclas = pclas;
        this.orderby = orderby;
        this.menuId = menuId;
    }
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
    
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getPclas() {
        return pclas;
    }
    public void setPclas(String pclas) {
        this.pclas = pclas;
    }
    public Integer getOrderby() {
        return orderby;
    }
    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }
    public Integer getMenuId() {
        return menuId;
    }
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
    public boolean isSelect() {
        return select;
    }
    public void setSelect(boolean select) {
        this.select = select;
    }
    
    
    
    
    
}
