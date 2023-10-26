package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
/**
 *  CscpHxDformColumn 实体类
 *
 * @author hx
 * @date 2022-11-14 14:36:13
 *
 */

@ApiModel(description = "CscpHxDformColumn")
public class CscpHxDformColumn  implements Serializable{
    /**
    * ColumnId
    *
    */
    @AutoId(primaryKey = "column_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long columnId;
    /**
    * 数据库表ID
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tableId;
    /**
    * 列名称
    *
    */

    private String columnName;
    /**
    * 列旧名称
    *
    */

    private String columnNameOld;
    /**
    * 列描述
    *
    */

    private String columnComment;
    /**
    * 列类型
    *
    */

    private Integer columnType;
    /**
    * 列长度
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long columnLength;
    /**
    * 小数点
    *
    */

    private Integer pointLength;
    /**
    * 是否主键（1是）
    *
    */

    private Integer isPk;
    /**
    * 默认值
    *
    */

    private String defaultValue;
    /**
    * 是否为空（1是）
    *
    */

    private Integer isNull;
    /**
    * 排序
    *
    */

    private Integer orderNum;
    /**
    * 是否表单字段（1是）
    *
    */

    private Integer isForm;
    /**
    * 是否列表字段（1是）
    *
    */

    private Integer isList;
    /**
    * 是否查询字段（1是）
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long isQuery;
    /**
    * 显示类型
    *
    */

    private Integer showType;
    /**
    * 查询方式（等于、不等于、大于、小于、范围）
    *
    */

    private Integer queryType;
    /**
    * 控件长度
    *
    */

    private Integer controlLength;
    /**
    * 字典编码
    *
    */

    private String dicCode;
    /**
    * 列校验
    *
    */

    private String validateRule;
    /**
    * 是否外键(1是)
    *
    */

    private String isFk;

    /**
     * 是否为业务主键
     *
     */
    private String isBk;

    private String columnRelation;

    private String tableName;



    public void setColumnId(Long columnId){
        this.columnId = columnId;
    }
    public Long getColumnId(){
        return columnId;
    }
    public void setTableId(Long tableId){
        this.tableId = tableId;
    }
    public Long getTableId(){
        return tableId;
    }
    public void setColumnName(String columnName){
        this.columnName = columnName;
    }
    public String getColumnName(){
        return columnName;
    }
    public void setColumnNameOld(String columnNameOld){
        this.columnNameOld = columnNameOld;
    }
    public String getColumnNameOld(){
        return columnNameOld;
    }
    public void setColumnComment(String columnComment){
        this.columnComment = columnComment;
    }
    public String getColumnComment(){
        return columnComment;
    }
    public void setColumnType(Integer columnType){
        this.columnType = columnType;
    }
    public Integer getColumnType(){
        return columnType;
    }
    public void setColumnLength(Long columnLength){
        this.columnLength = columnLength;
    }
    public Long getColumnLength(){
        return columnLength;
    }
    public void setPointLength(Integer pointLength){
        this.pointLength = pointLength;
    }
    public Integer getPointLength(){
        return pointLength;
    }
    public void setIsPk(Integer isPk){
        this.isPk = isPk;
    }
    public Integer getIsPk(){
        return isPk;
    }
    public void setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
    }
    public String getDefaultValue(){
        return defaultValue;
    }
    public void setIsNull(Integer isNull){
        this.isNull = isNull;
    }
    public Integer getIsNull(){
        return isNull;
    }
    public void setOrderNum(Integer orderNum){
        this.orderNum = orderNum;
    }
    public Integer getOrderNum(){
        return orderNum;
    }
    public void setIsForm(Integer isForm){
        this.isForm = isForm;
    }
    public Integer getIsForm(){
        return isForm;
    }
    public void setIsList(Integer isList){
        this.isList = isList;
    }
    public Integer getIsList(){
        return isList;
    }
    public void setIsQuery(Long isQuery){
        this.isQuery = isQuery;
    }
    public Long getIsQuery(){
        return isQuery;
    }
    public void setShowType(Integer showType){
        this.showType = showType;
    }
    public Integer getShowType(){
        return showType;
    }
    public void setQueryType(Integer queryType){
        this.queryType = queryType;
    }
    public Integer getQueryType(){
        return queryType;
    }
    public void setControlLength(Integer controlLength){
        this.controlLength = controlLength;
    }
    public Integer getControlLength(){
        return controlLength;
    }
    public void setDicCode(String dicCode){
        this.dicCode = dicCode;
    }
    public String getDicCode(){
        return dicCode;
    }
    public void setValidateRule(String validateRule){
        this.validateRule = validateRule;
    }
    public String getValidateRule(){
        return validateRule;
    }
    public void setIsFk(String isFk){
        this.isFk = isFk;
    }
    public String getIsFk(){
        return isFk;
    }

    public String getIsBk() {
        return isBk;
    }

    public void setIsBk(String isBk) {
        this.isBk = isBk;
    }

    public String getColumnRelation() {
        return columnRelation;
    }

    public void setColumnRelation(String columnRelation) {
        this.columnRelation = columnRelation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object that){
        if(this==that){
            return true;
        }
        if(that==null){
            return false;
        }
        if(getClass()!=that.getClass()){
            return false;
        }
        CscpHxDformColumn other=(CscpHxDformColumn)that;
        return (this.getColumnId() ==null?this.getColumnId() !=null:this.getColumnId().equals(other.getColumnId()))
                && (this.getTableId() ==null?this.getTableId() !=null:this.getTableId().equals(other.getTableId()))
                && (this.getColumnName() ==null?this.getColumnName() !=null:this.getColumnName().equals(other.getColumnName()))
                && (this.getColumnNameOld() ==null?this.getColumnNameOld() !=null:this.getColumnNameOld().equals(other.getColumnNameOld()))
                && (this.getColumnComment() ==null?this.getColumnComment() !=null:this.getColumnComment().equals(other.getColumnComment()))
                && (this.getColumnType() ==null?this.getColumnType() !=null:this.getColumnType().equals(other.getColumnType()))
                && (this.getColumnLength() ==null?this.getColumnLength() !=null:this.getColumnLength().equals(other.getColumnLength()))
                && (this.getPointLength() ==null?this.getPointLength() !=null:this.getPointLength().equals(other.getPointLength()))
                && (this.getIsPk() ==null?this.getIsPk() !=null:this.getIsPk().equals(other.getIsPk()))
                && (this.getDefaultValue() ==null?this.getDefaultValue() !=null:this.getDefaultValue().equals(other.getDefaultValue()))
                && (this.getIsNull() ==null?this.getIsNull() !=null:this.getIsNull().equals(other.getIsNull()))
                && (this.getOrderNum() ==null?this.getOrderNum() !=null:this.getOrderNum().equals(other.getOrderNum()))
                && (this.getIsForm() ==null?this.getIsForm() !=null:this.getIsForm().equals(other.getIsForm()))
                && (this.getIsList() ==null?this.getIsList() !=null:this.getIsList().equals(other.getIsList()))
                && (this.getIsQuery() ==null?this.getIsQuery() !=null:this.getIsQuery().equals(other.getIsQuery()))
                && (this.getShowType() ==null?this.getShowType() !=null:this.getShowType().equals(other.getShowType()))
                && (this.getQueryType() ==null?this.getQueryType() !=null:this.getQueryType().equals(other.getQueryType()))
                && (this.getControlLength() ==null?this.getControlLength() !=null:this.getControlLength().equals(other.getControlLength()))
                && (this.getDicCode() ==null?this.getDicCode() !=null:this.getDicCode().equals(other.getDicCode()))
                && (this.getValidateRule() ==null?this.getValidateRule() !=null:this.getValidateRule().equals(other.getValidateRule()))
                && (this.getIsFk() ==null?this.getIsFk() !=null:this.getIsFk().equals(other.getIsFk()))
                && (this.getIsBk() ==null?this.getIsBk() !=null:this.getIsBk().equals(other.getIsBk()))
                && (this.getColumnRelation() ==null?this.getColumnRelation() !=null:this.getColumnRelation().equals(other.getColumnRelation()))
                && (this.getTableName() ==null?this.getTableName() !=null:this.getTableName().equals(other.getTableName()));

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getColumnId() == null) ? 0 : getColumnId().hashCode());
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        result = prime * result + ((getColumnName() == null) ? 0 : getColumnName().hashCode());
        result = prime * result + ((getColumnNameOld() == null) ? 0 : getColumnNameOld().hashCode());
        result = prime * result + ((getColumnComment() == null) ? 0 : getColumnComment().hashCode());
        result = prime * result + ((getColumnType() == null) ? 0 : getColumnType().hashCode());
        result = prime * result + ((getColumnLength() == null) ? 0 : getColumnLength().hashCode());
        result = prime * result + ((getPointLength() == null) ? 0 : getPointLength().hashCode());
        result = prime * result + ((getIsPk() == null) ? 0 : getIsPk().hashCode());
        result = prime * result + ((getDefaultValue() == null) ? 0 : getDefaultValue().hashCode());
        result = prime * result + ((getIsNull() == null) ? 0 : getIsNull().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getIsForm() == null) ? 0 : getIsForm().hashCode());
        result = prime * result + ((getIsList() == null) ? 0 : getIsList().hashCode());
        result = prime * result + ((getIsQuery() == null) ? 0 : getIsQuery().hashCode());
        result = prime * result + ((getShowType() == null) ? 0 : getShowType().hashCode());
        result = prime * result + ((getQueryType() == null) ? 0 : getQueryType().hashCode());
        result = prime * result + ((getControlLength() == null) ? 0 : getControlLength().hashCode());
        result = prime * result + ((getDicCode() == null) ? 0 : getDicCode().hashCode());
        result = prime * result + ((getValidateRule() == null) ? 0 : getValidateRule().hashCode());
        result = prime * result + ((getIsFk() == null) ? 0 : getIsFk().hashCode());
        result = prime * result + ((getIsBk() == null) ? 0 : getIsBk().hashCode());
        result = prime * result + ((getColumnRelation() == null) ? 0 : getColumnRelation().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        return result;
    }
}
