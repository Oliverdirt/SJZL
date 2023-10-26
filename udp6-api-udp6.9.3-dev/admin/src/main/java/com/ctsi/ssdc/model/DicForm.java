package com.ctsi.ssdc.model;

public class DicForm {
	private Long id;
	private Integer dicType;
	private Integer dicValue;
	private String dicDisplay;
	private Integer dicGroupId;
	private Integer dicParentId;
	private Integer dicOrder;
	private String dicIcon;
	private String dicCheckRadio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDicType() {
		return dicType;
	}

	public void setDicType(Integer dicType) {
		this.dicType = dicType;
	}

	public Integer getDicValue() {
		return dicValue;
	}

	public void setDicValue(Integer dicValue) {
		this.dicValue = dicValue;
	}

	public String getDicDisplay() {
		return dicDisplay;
	}

	public void setDicDisplay(String dicDisplay) {
		this.dicDisplay = dicDisplay;
	}

	public Integer getDicGroupId() {
		return dicGroupId;
	}

	public void setDicGroupId(Integer dicGroupId) {
		this.dicGroupId = dicGroupId;
	}

	public Integer getDicParentId() {
		return dicParentId;
	}

	public void setDicParentId(Integer dicParentId) {
		this.dicParentId = dicParentId;
	}

	public Integer getDicOrder() {
		return dicOrder;
	}

	public void setDicOrder(Integer dicOrder) {
		this.dicOrder = dicOrder;
	}

	public String getDicIcon() {
		return dicIcon;
	}

	public void setDicIcon(String dicIcon) {
		this.dicIcon = dicIcon;
	}

	public String getDicCheckRadio() {
		return dicCheckRadio;
	}

	public void setDicCheckRadio(String dicCheckRadio) {
		this.dicCheckRadio = dicCheckRadio;
	}

}
