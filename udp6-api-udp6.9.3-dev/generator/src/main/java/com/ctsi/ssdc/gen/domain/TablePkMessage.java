package com.ctsi.ssdc.gen.domain;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "TablePkMessage")
public class TablePkMessage implements Serializable {
    //表名
    private String tableName;
    //实例名
    private String tableSchema;
    //主键名称 多个用逗号隔开
    private String pkColumn;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true;}
        if (o == null || getClass() != o.getClass()) { return false;}
        TablePkMessage that = (TablePkMessage) o;
        return Objects.equals(tableName, that.tableName) && Objects.equals(tableSchema, that.tableSchema) && Objects.equals(pkColumn, that.pkColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, tableSchema, pkColumn);
    }

    @Override
    public String toString() {
        return "TablePkMessage{" +
                "tableName='" + tableName + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", pkColumn='" + pkColumn + '\'' +
                '}';
    }
}
