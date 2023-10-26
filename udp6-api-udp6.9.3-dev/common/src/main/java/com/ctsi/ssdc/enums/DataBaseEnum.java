package com.ctsi.ssdc.enums;

/**
 * 数据库类型枚举类
 */
public enum DataBaseEnum {
    /**
     * MySQL
     */
    MYSQL(0,"MySQL", "Mysql"),
    /**
     * Oracle
     */
    ORACLE(1,"Oracle","Oracle"),
    /**
     * Microsoft SQL Server
     */
    SQL_SERVER(2,"Microsoft SQL Server", "SqlServer"),
    /**
     * H2
     */
    H2(3,"H2", "H2");

    private String databaseProductName;

     private String databaseName;

     private Integer number;

    private DataBaseEnum(Integer number,String databaseProductName, String databaseName) {
        this.databaseProductName = databaseProductName;
        this.databaseName = databaseName;
        this.number = number;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Integer getNumber() {
        return number;
    }

    public static DataBaseEnum getByNumber(Integer number){
        for (DataBaseEnum dataBaseEnum : DataBaseEnum.values()){
            if (dataBaseEnum.getNumber().equals(number)){
                return dataBaseEnum;
            }
        }
        return null;
    }
}
