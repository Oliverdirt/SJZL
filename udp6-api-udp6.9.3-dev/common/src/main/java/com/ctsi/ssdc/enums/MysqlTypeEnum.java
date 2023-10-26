package com.ctsi.ssdc.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL数据库字典类型枚举
 */
public enum MysqlTypeEnum implements SQLType {
    //    数值类型
    TINYINT("tinyint",1,"Integer","INTEGER"),

    SMALLINT("smallint",2,"Integer","SMALLINT"),

    MEDIUMINT("mediumint",3,"Integer","INTEGER"),

    INT("int",4,"Integer","INTEGER"),

    BIGINT("bigint",5,"Long","BIGINT"),

    FLOAT("float",6,"Float","FLOAT"),

    DOUBLE("double",7,"Double","DOUBLE"),

    DECIMAL("decimal",8,"BigDecimal","DECIMAL"),

    //日期与时间类型
    DATE("date",9,"Date","DATE"),
//
//    TIME("time",10,"Date","TIME"),
//
//    YEAR("year",11,"Date","DATE"),

    DATETIME("datetime",12,"ZonedDateTime","DATE"),

    TIMESTAMP("timestamp",13,"ZonedDateTime","TIMESTAMP"),

    //字符串类型

    CHAR("char",14,"String","VARCHAR"),

    VARCHAR("varchar",15,"String","VARCHAR"),

//    TINYBLOB("tinyblob",16,"String","BLOB"),

    TINYTEXT("tinytext",17,"String","VARCHAR"),

//    BLOB("blob",18,"String","BLOB"),

    TEXT("text",19,"String","VARCHAR"),

//    MEDIUMBLOB("mediumblob",20,"String","BLOB"),

    MEDIUMTEXT("mediumtext",21,"String","VARCHAR"),

//    LONGBLOB("longblob",22,"String","BLOB"),

    LONGTEXT("longtext",23,"String","VARCHAR"),

    NUMBER("number",26,"String","VARCHAR"),

    FATHER_TABLE("table",25,"Long","number"),
    ;

    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段名称
     */
    private String javaType;

    /**
     * 字段名称
     */
    private String jdbcType;
    /**
     * 字段类型对应编号
     */
    private Integer num;

    public String getJavaType() {
        return javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    MysqlTypeEnum(String name, Integer num, String javaType, String jdbcType) {
        this.name = name;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.num = num;
    }

    /**
     * 根据数字获取对应的字段类型名称
     *
     */
    public static String getNameByNum(Integer typeNumber) {
        for (MysqlTypeEnum mysqlType : MysqlTypeEnum.values()){
            if (typeNumber.equals(mysqlType.num)){
                return mysqlType.name;
            }
        }
        throw new IllegalArgumentException("Num:" + typeNumber + " is not a valid type(字段类型数字不正确) ");
    }


    /**
     * 根据数字获取对应的javaType字段类型名称
     *
     */
    public static String getJavaTypeByNum(Integer typeNumber) {
        for (MysqlTypeEnum mysqlType : MysqlTypeEnum.values()){
            if (typeNumber.equals(mysqlType.num)){
                return mysqlType.javaType;
            }
        }
        throw new IllegalArgumentException("Num:" + typeNumber + " is not a valid type ");
    }


    /**
     * 根据数字获取对应的javaType字段类型名称
     *
     */
    public static String getJdbcTypeByNum(Integer typeNumber) {
        for (MysqlTypeEnum mysqlType : MysqlTypeEnum.values()){
            if (typeNumber.equals(mysqlType.num)){
                return mysqlType.jdbcType;
            }
        }
        throw new IllegalArgumentException("Num:" + typeNumber + " is not a valid type ");
    }

    /**
     * 根据字段类型名称获取对应的数字
     *
     * @param name@return
     */
    public static Integer getNumByName(String name) {
        name = name.toLowerCase();
        for (MysqlTypeEnum mysqlType : MysqlTypeEnum.values()){
            if (name.equals(mysqlType.name)){
                return mysqlType.num;
            }
        }
        return null;
    }


    /**
     * 获取字段类型名称
     *
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 获取字段类型数字
     *
     */
    @Override
    public Integer getNum() {
        return num;
    }

    public static List<Map> getTypeList() {
        List<Map> list = new ArrayList<>();
        for (MysqlTypeEnum mysqlType : MysqlTypeEnum.values()){
            Map<String, Object> map = new HashMap<>();
            map.put("typeNum",mysqlType.num);
            map.put("typeName",mysqlType.name);
            list.add(map);
        }
        return list;
    }


}
