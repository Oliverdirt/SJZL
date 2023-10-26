package com.ctsi.ssdc.enums;

public enum ComponentTypeEnum {
    /**
     * 单行输入
     */
    SINGLELINE(0,"单行输入"),
    /**
     * 多行输入
     */
    MULTILINE(1,"多行输入"),
    /**
     * 计数器
     */
    COUNTER(2,"计数器"),
    /**
     * 单选项
     */
    SINGLEOPTION(3,"单选项"),
    /**
     * 多选项
     */
    MULTIOPTIONS(4,"多选项"),
    /**
     * 下拉选项
     */
    DROPDOWNOPTIONS(5,"下拉选项"),
    /**
     * 时间
     */
    TIME(6,"时间"),
    /**
     * 时间范围
     */
    TIMEFRAME(7,"时间范围"),
    /**
     * 日期
     */
    DATE(8,"日期"),
    /**
     * 日期范围
     */
    DATERANG(9,"日期范围"),
    /**
     * 开关
     */
    SWITCH(10,"开关"),
    /**
     * 评分
     */
    SCORE(11,"评分"),
    /**
     * 颜色选择器
     */
    COLORSELECTOR(12,"颜色选择器"),
    /**
     * 滑块
     */
    SLIDER(13,"滑块"),
    /**
     * 静态文字
     */
    STATICTEXT(14,"静态文字"),
    /**
     * html
     */
    HTML(15,"html"),
    /***
     * 按钮
     */
    BUTTON(16,"按钮"),
    /**
     * 分割线
     */
    DIVIDINGLINE(17,"分割线");

    private Integer number;

    private String type;


     ComponentTypeEnum(Integer number,String type){
        this.number = number;
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
