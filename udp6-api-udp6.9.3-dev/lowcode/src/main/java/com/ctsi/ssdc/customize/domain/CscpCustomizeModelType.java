package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpCustomizeModelType 实体类
 *
 * @author hx
 * @date 2022-08-29 16:33:42
 *
 */

@ApiModel(description = "CscpCustomizeModelType")
public class CscpCustomizeModelType  implements Serializable{
    /**
    * 模型类型id
    *
    */
    @AutoId(primaryKey = "model_type_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long modelTypeId;
    /**
    * 模型类型
    *
    */

    private String modelType;
    /**
    * 模型类型名称
    *
    */

    private String modelName;
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



    public void setModelTypeId(Long modelTypeId){
        this.modelTypeId = modelTypeId;
    }
    public Long getModelTypeId(){
        return modelTypeId;
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
        CscpCustomizeModelType other=(CscpCustomizeModelType)that;
        return (this.getModelTypeId() ==null?this.getModelTypeId() !=null:this.getModelTypeId().equals(other.getModelTypeId()))
                && (this.getModelType() ==null?this.getModelType() !=null:this.getModelType().equals(other.getModelType()))
                && (this.getModelName() ==null?this.getModelName() !=null:this.getModelName().equals(other.getModelName()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreatedBy() ==null?this.getCreatedBy() !=null:this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getModelTypeId() == null) ? 0 : getModelTypeId().hashCode());
        result = prime * result + ((getModelType() == null) ? 0 : getModelType().hashCode());
        result = prime * result + ((getModelName() == null) ? 0 : getModelName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        return result;
    }
}
