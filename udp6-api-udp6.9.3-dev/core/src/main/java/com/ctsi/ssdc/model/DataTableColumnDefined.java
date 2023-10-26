package com.ctsi.ssdc.model;

public class DataTableColumnDefined {

    private String tableName;
    private String columnName;
    private String columnType;
    private String columnTypeName;
    private String columnDisplaySize;
    
    
    public DataTableColumnDefined(String tableName, String columnName, String columnType,
            String columnTypeName, String columnDisplaySize) {
        super();
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnTypeName = columnTypeName;
        this.columnDisplaySize = columnDisplaySize;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getColumnType() {
        return columnType;
    }
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
    public String getColumnTypeName() {
        return columnTypeName;
    }
    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }
    public String getColumnDisplaySize() {
        return columnDisplaySize;
    }
    public void setColumnDisplaySize(String columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }
    
}
