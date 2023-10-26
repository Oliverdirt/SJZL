package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpCustomizeModule 实体类
 *
 * @author hx
 * @date 2022-08-31 09:44:42
 *
 */

@ApiModel(description = "CscpCustomizeModule")
public class CscpCustomizeModule  implements Serializable{
    /**
    * 模块id
    *
    */
    @AutoId(primaryKey = "module_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long moduleId;
    /**
    * 模块名称
    *
    */

    private String moduleName;
    /**
    * 模块标识
    *
    */

    private String moduleIdentify;
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



    public void setModuleId(Long moduleId){
        this.moduleId = moduleId;
    }
    public Long getModuleId(){
        return moduleId;
    }
    public void setModuleName(String moduleName){
        this.moduleName = moduleName;
    }
    public String getModuleName(){
        return moduleName;
    }
    public void setModuleIdentify(String moduleIdentify){
        this.moduleIdentify = moduleIdentify;
    }
    public String getModuleIdentify(){
        return moduleIdentify;
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
        CscpCustomizeModule other=(CscpCustomizeModule)that;
        return (this.getModuleId() ==null?this.getModuleId() !=null:this.getModuleId().equals(other.getModuleId()))
                && (this.getModuleName() ==null?this.getModuleName() !=null:this.getModuleName().equals(other.getModuleName()))
                && (this.getModuleIdentify() ==null?this.getModuleIdentify() !=null:this.getModuleIdentify().equals(other.getModuleIdentify()))
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
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getModuleName() == null) ? 0 : getModuleName().hashCode());
        result = prime * result + ((getModuleIdentify() == null) ? 0 : getModuleIdentify().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        return result;
    }
}
