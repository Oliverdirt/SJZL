package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import java.util.List;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
/**
 *  CscpHxDformTable 实体类
 *
 * @author hx
 * @date 2022-11-14 14:22:09
 *
 */

@ApiModel(description = "CscpHxDformTable")
public class CscpHxDformTable  implements Serializable{
    /**
    * TableId
    *
    */
    @AutoId(primaryKey = "table_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tableId;
    /**
    * 表名称
    *
    */

    private String tableName;
    /**
    * 表描述
    *
    */

    private String tableContent;
    /**
    * 数据库类型
    *
    */

    private Integer dbType;
    /**
    * 持久化类型
    *
    */

    private Integer persistenceType;
    /**
    * 是否同步数据库
    *
    */

    private Integer isDbSynch;
    /**
    * 表单类型（单表，主子表、树表）
    *
    */

    private Integer formType;
    /**
    * 关联子表的表名
    *
    */

    private String subTableName;
    /**
    * 子表关联的外键名
    *
    */

    private String subTableFkName;
    /**
    * 生成包路径
    *
    */

    private String packageName;
    /**
    * 生成代码方式（0zip压缩包 1自定义路径）
    *
    */

    private Integer genType;
    /**
    * 生成路径（不填默认项目路径）
    *
    */

    private String genPath;
    /**
    * 其它生成选项
    *
    */

    private String options;
    /**
    * 备注
    *
    */

    private String remark;
    /**
    * 模板类型
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long formSuite;
    /**
    * 关联主表table_id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long mainTableId;

    /** 表列信息 */
    private List<CscpHxDformColumn> columns;

    private List<CscpHxDformTable> cscpHxFormTables;

    public List<CscpHxDformTable> getCscpHxFormTables() {
        return cscpHxFormTables;
    }

    public void setCscpHxFormTables(List<CscpHxDformTable> cscpHxFormTables) {
        this.cscpHxFormTables = cscpHxFormTables;
    }

    public List<CscpHxDformColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<CscpHxDformColumn> columns) {
        this.columns = columns;
    }

    public void setTableId(Long tableId){
        this.tableId = tableId;
    }
    public Long getTableId(){
        return tableId;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    public String getTableName(){
        return tableName;
    }
    public void setTableContent(String tableContent){
        this.tableContent = tableContent;
    }
    public String getTableContent(){
        return tableContent;
    }
    public void setDbType(Integer dbType){
        this.dbType = dbType;
    }
    public Integer getDbType(){
        return dbType;
    }
    public void setPersistenceType(Integer persistenceType){
        this.persistenceType = persistenceType;
    }
    public Integer getPersistenceType(){
        return persistenceType;
    }
    public void setIsDbSynch(Integer isDbSynch){
        this.isDbSynch = isDbSynch;
    }
    public Integer getIsDbSynch(){
        return isDbSynch;
    }
    public void setFormType(Integer formType){
        this.formType = formType;
    }
    public Integer getFormType(){
        return formType;
    }
    public void setSubTableName(String subTableName){
        this.subTableName = subTableName;
    }
    public String getSubTableName(){
        return subTableName;
    }
    public void setSubTableFkName(String subTableFkName){
        this.subTableFkName = subTableFkName;
    }
    public String getSubTableFkName(){
        return subTableFkName;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public String getPackageName(){
        return packageName;
    }
    public void setGenType(Integer genType){
        this.genType = genType;
    }
    public Integer getGenType(){
        return genType;
    }
    public void setGenPath(String genPath){
        this.genPath = genPath;
    }
    public String getGenPath(){
        return genPath;
    }
    public void setOptions(String options){
        this.options = options;
    }
    public String getOptions(){
        return options;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return remark;
    }
    public void setFormSuite(Long formSuite){
        this.formSuite = formSuite;
    }
    public Long getFormSuite(){
        return formSuite;
    }
    public void setMainTableId(Long mainTableId){
        this.mainTableId = mainTableId;
    }
    public Long getMainTableId(){
        return mainTableId;
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
        CscpHxDformTable other=(CscpHxDformTable)that;
        return (this.getTableId() ==null?this.getTableId() !=null:this.getTableId().equals(other.getTableId()))
                && (this.getTableName() ==null?this.getTableName() !=null:this.getTableName().equals(other.getTableName()))
                && (this.getTableContent() ==null?this.getTableContent() !=null:this.getTableContent().equals(other.getTableContent()))
                && (this.getDbType() ==null?this.getDbType() !=null:this.getDbType().equals(other.getDbType()))
                && (this.getPersistenceType() ==null?this.getPersistenceType() !=null:this.getPersistenceType().equals(other.getPersistenceType()))
                && (this.getIsDbSynch() ==null?this.getIsDbSynch() !=null:this.getIsDbSynch().equals(other.getIsDbSynch()))
                && (this.getFormType() ==null?this.getFormType() !=null:this.getFormType().equals(other.getFormType()))
                && (this.getSubTableName() ==null?this.getSubTableName() !=null:this.getSubTableName().equals(other.getSubTableName()))
                && (this.getSubTableFkName() ==null?this.getSubTableFkName() !=null:this.getSubTableFkName().equals(other.getSubTableFkName()))
                && (this.getPackageName() ==null?this.getPackageName() !=null:this.getPackageName().equals(other.getPackageName()))
                && (this.getGenType() ==null?this.getGenType() !=null:this.getGenType().equals(other.getGenType()))
                && (this.getGenPath() ==null?this.getGenPath() !=null:this.getGenPath().equals(other.getGenPath()))
                && (this.getOptions() ==null?this.getOptions() !=null:this.getOptions().equals(other.getOptions()))
                && (this.getRemark() ==null?this.getRemark() !=null:this.getRemark().equals(other.getRemark()))
                && (this.getFormSuite() ==null?this.getFormSuite() !=null:this.getFormSuite().equals(other.getFormSuite()))
                && (this.getMainTableId() ==null?this.getMainTableId() !=null:this.getMainTableId().equals(other.getMainTableId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getTableContent() == null) ? 0 : getTableContent().hashCode());
        result = prime * result + ((getDbType() == null) ? 0 : getDbType().hashCode());
        result = prime * result + ((getPersistenceType() == null) ? 0 : getPersistenceType().hashCode());
        result = prime * result + ((getIsDbSynch() == null) ? 0 : getIsDbSynch().hashCode());
        result = prime * result + ((getFormType() == null) ? 0 : getFormType().hashCode());
        result = prime * result + ((getSubTableName() == null) ? 0 : getSubTableName().hashCode());
        result = prime * result + ((getSubTableFkName() == null) ? 0 : getSubTableFkName().hashCode());
        result = prime * result + ((getPackageName() == null) ? 0 : getPackageName().hashCode());
        result = prime * result + ((getGenType() == null) ? 0 : getGenType().hashCode());
        result = prime * result + ((getGenPath() == null) ? 0 : getGenPath().hashCode());
        result = prime * result + ((getOptions() == null) ? 0 : getOptions().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getFormSuite() == null) ? 0 : getFormSuite().hashCode());
        result = prime * result + ((getMainTableId() == null) ? 0 : getMainTableId().hashCode());
        return result;
    }
}
