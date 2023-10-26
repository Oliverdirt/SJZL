package com.ctsi.ssdc.enums;

/**
 * 表单类型枚举类
 */
public enum FormTypeEnum {

//     SINGLE(0,"单表"),
//     MASTER(1,"主表"),
//     SLAVE(2,"附表"),
//     ACT(3,"工作流表单");
    SINGLE(1,"单表"),
    TREE(2,"树表"),
    SUB(3,"主子表"),
    ACT(4,"工作流表单");
    private Integer number;

    private String type;

    private FormTypeEnum(Integer number,String type){
        this.number = number;
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public static FormTypeEnum getByNumer(Integer integer){
        for (FormTypeEnum formTypeEnum : FormTypeEnum.values()){
            if (formTypeEnum.number.equals(integer)){
                return formTypeEnum;
            }
        }
        return null;
    }
}
