package com.ctsi.ssdc.enums;

/**
 * 展示类型
 */
public enum ShowTypeEnum {

    /**
     * input框
     */
    INPUT(0,"input"),
    /**
     * 密码框
     */
    PASSWORD(1,"password"),
    /**
     * 单选
     */
    RADIO(2,"radio"),
    /**
     * 多选
     */
    CHECKBOX(3,"checkbox"),
    /**
     *
     * 时间date
     */
    DATE(4,"date"),
    /**
     * 日期 datetime
     */
    DATETIME(5,"datetime"),
    /**
     * 多行文本
     */
    TEXTAREA(6,"textarea"),
    /**
     * 下拉选择
     */
    Select(7,"select");

    private Integer number;

    private String type;

    ShowTypeEnum(Integer number, String type) {
        this.number = number;
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据数字获取对应的字段类型名称
     *
     * @param typeNumber
     * @return
     */
    public static String getNameByNum(Integer typeNumber) {
        for (ShowTypeEnum showTypeEnum : ShowTypeEnum.values()){
            if (typeNumber.equals(showTypeEnum.number)){
                return showTypeEnum.type;
            }
        }
        throw new IllegalArgumentException("number:" + typeNumber + " is not a valid type(字段类型数字不正确) ");
    }

    /**
     * 根据字段类型名称获取对应的数字
     *
     * @param name@return
     */
    public static Integer getNumByName(String name) {
        name = name.toLowerCase();
        for (ShowTypeEnum showTypeEnum : ShowTypeEnum.values()){
            if (name.equals(showTypeEnum.type)){
                return showTypeEnum.number;
            }
        }
        return null;
    }
}

