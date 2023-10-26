package com.ctsi.ssdc.enums;

/**
 * 查询类型枚举类
 */
public enum QueryTypeEnum {
    /**
     * 等于
     */
    EQUALS(0,"=","equals"),
    /**
     * 大于等于
     */
    GREATER(1,">=","greaterOrEqualThan"),
    /**
     * 小于等于
     */
    LESS(2,"<=","lessOrEqualThan"),
    /**
     * like 模糊匹配
     */
    CONTAINS(3,"like","contains"),
    /**
     * 范围查询 时间
     */
    BETWEEN(4,"between","between")
    ;
    //对应序号
    private Integer num;
//    对应符号
    private String sign;
//    对应名称
    private String type;

     QueryTypeEnum(Integer num, String sign, String type) {
        this.num = num;
        this.sign = sign;
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public String getSign() {
        return sign;
    }

    public String getType() {
        return type;
    }

    public static QueryTypeEnum getByNum(Integer num){
         for (QueryTypeEnum queryTypeEnum : QueryTypeEnum.values()){
             if (queryTypeEnum.num.equals(num)){
                 return queryTypeEnum;
             }
         }
         return null;
    }
}
