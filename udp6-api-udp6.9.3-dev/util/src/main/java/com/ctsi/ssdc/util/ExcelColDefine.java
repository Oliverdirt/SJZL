package com.ctsi.ssdc.util;


/**
 * excel列配置类
 * @author ctsi
 *
 */
public class ExcelColDefine {
	private String head;
	private Integer type;
	private Integer isHide;
	private String property;
	
	public final static int COL_TYPE_STRING = 0;
	public final static int COL_TYPE_NUMBER = 1;
	public final static int COL_HIDDEN = 2;
	public final static int COL_NOT_HIDDEN = 3;
	
	public ExcelColDefine(){}
	
	
	public ExcelColDefine(String head, Integer type, Integer isHide, String property) {
		super();
		this.head = head;
		this.type = type;
		this.isHide = isHide;
		this.property = property;
	}


	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}


	public Integer getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public Integer getIsHide() {
		return isHide;
	}


	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}
	
}
