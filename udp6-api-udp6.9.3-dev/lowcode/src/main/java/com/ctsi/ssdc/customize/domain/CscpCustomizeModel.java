package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpCustomizeModel 实体类
 *
 * @author hx
 * @date 2022-08-31 09:40:19
 *
 */

@ApiModel(description = "CscpCustomizeModel")
public class CscpCustomizeModel  implements Serializable{
    /**
    * 数据模型id
    *
    */
    @AutoId(primaryKey = "model_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long modelId;
    /**
    * 数据模型类型
    *
    */

    private String modelType;
    /**
    * 数据模型名称
    *
    */

    private String modelName;
    /**
    * 数据模型标识
    *
    */

    private String modelIdentify;
    /**
    * 数据库表名
    *
    */

    private String tableName;
    /**
    * 数据库表id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tableId;
    /**
    * 数据库表注释
    *
    */

    private String tableDescription;
    /**
    * 模块名称
    *
    */

    private String moduleName;
    /**
    * 模块id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long moduleId;
    /**
    * 创建时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;
    /**
    * 创建者id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long createdBy;
    /**
    * 更新时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;
    /**
    * 更新者id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long updateBy;
    /**
    * 租户id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tenantId;

    /**
     * 创建方式
     */
    private String createMode;



    public void setModelId(Long modelId){
        this.modelId = modelId;
    }
    public Long getModelId(){
        return modelId;
    }
    public void setModelType(String modelType){
        this.modelType = modelType;
    }
    public String getModelType(){
        return modelType;
    }
    public void setModelName(String modelName){
        this.modelName = modelName;
    }
    public String getModelName(){
        return modelName;
    }
    public void setModelIdentify(String modelIdentify){
        this.modelIdentify = modelIdentify;
    }
    public String getModelIdentify(){
        return modelIdentify;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    public String getTableName(){
        return tableName;
    }
    public void setTableId(Long tableId){
        this.tableId = tableId;
    }
    public Long getTableId(){
        return tableId;
    }
    public void setTableDescription(String tableDescription){
        this.tableDescription = tableDescription;
    }
    public String getTableDescription(){
        return tableDescription;
    }
    public void setModuleName(String moduleName){
        this.moduleName = moduleName;
    }
    public String getModuleName(){
        return moduleName;
    }
    public void setModuleId(Long moduleId){
        this.moduleId = moduleId;
    }
    public Long getModuleId(){
        return moduleId;
    }
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
    }
    public void setCreatedBy(Long createdBy){
        this.createdBy = createdBy;
    }
    public Long getCreatedBy(){
        return createdBy;
    }
    public void setUpdateTime(ZonedDateTime updateTime){
        this.updateTime = updateTime;
    }
    public ZonedDateTime getUpdateTime(){
        return updateTime;
    }
    public void setUpdateBy(Long updateBy){
        this.updateBy = updateBy;
    }
    public Long getUpdateBy(){
        return updateBy;
    }
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    public Long getTenantId(){
        return tenantId;
    }

    public String getCreateMode() {
        return createMode;
    }

    public void setCreateMode(String createMode) {
        this.createMode = createMode;
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
        CscpCustomizeModel other=(CscpCustomizeModel)that;
        return (this.getModelId() ==null?this.getModelId() !=null:this.getModelId().equals(other.getModelId()))
                && (this.getModelType() ==null?this.getModelType() !=null:this.getModelType().equals(other.getModelType()))
                && (this.getModelName() ==null?this.getModelName() !=null:this.getModelName().equals(other.getModelName()))
                && (this.getModelIdentify() ==null?this.getModelIdentify() !=null:this.getModelIdentify().equals(other.getModelIdentify()))
                && (this.getTableName() ==null?this.getTableName() !=null:this.getTableName().equals(other.getTableName()))
                && (this.getTableId() ==null?this.getTableId() !=null:this.getTableId().equals(other.getTableId()))
                && (this.getTableDescription() ==null?this.getTableDescription() !=null:this.getTableDescription().equals(other.getTableDescription()))
                && (this.getModuleName() ==null?this.getModuleName() !=null:this.getModuleName().equals(other.getModuleName()))
                && (this.getModuleId() ==null?this.getModuleId() !=null:this.getModuleId().equals(other.getModuleId()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreatedBy() ==null?this.getCreatedBy() !=null:this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getCreateMode() ==null?this.getCreateMode() !=null:this.getCreateMode().equals(other.getCreateMode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getModelId() == null) ? 0 : getModelId().hashCode());
        result = prime * result + ((getModelType() == null) ? 0 : getModelType().hashCode());
        result = prime * result + ((getModelName() == null) ? 0 : getModelName().hashCode());
        result = prime * result + ((getModelIdentify() == null) ? 0 : getModelIdentify().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        result = prime * result + ((getTableDescription() == null) ? 0 : getTableDescription().hashCode());
        result = prime * result + ((getModuleName() == null) ? 0 : getModuleName().hashCode());
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getCreateMode() == null) ? 0 : getCreateMode().hashCode());
        return result;
    }
}
