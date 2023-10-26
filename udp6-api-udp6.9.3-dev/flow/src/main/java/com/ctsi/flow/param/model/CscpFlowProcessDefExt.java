package com.ctsi.flow.param.model;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpFlowProcessDefExt 实体类
 *
 * @author hx
 * @date 2022-08-05 10:08:04
 *
 */

@ApiModel(description = "CscpFlowProcessDefExt")
public class CscpFlowProcessDefExt  implements Serializable{

    /**
     * 编号
     *
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /**
    * 描述
    *
    */
    private String description;

    /**
    * 流程模型的编号
    *
    */
    private String modelId;

    /**
    * 表单编号
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long formId;

    /**
    * 流程定义的编号
    *
    */
    private String processDefinitionId;

    /**
     * 创建者
     *
     */
    private String creator;

    /**
     * 创建时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;

    /**
     * 更新人
     *
     */
    private String updater;

    /**
    * 更新时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private ZonedDateTime updateTime;

    /**
     * 租户编号
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long tenantId;

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }
    public String getCreator(){
        return creator;
    }
    public void setModelId(String modelId){
        this.modelId = modelId;
    }
    public String getModelId(){
        return modelId;
    }
    public void setFormId(Long formId){
        this.formId = formId;
    }
    public Long getFormId(){
        return formId;
    }
    public void setProcessDefinitionId(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    public String getProcessDefinitionId(){
        return processDefinitionId;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    public Long getTenantId(){
        return tenantId;
    }
    public void setUpdateTime(ZonedDateTime updateTime){
        this.updateTime = updateTime;
    }
    public ZonedDateTime getUpdateTime(){
        return updateTime;
    }
    public void setUpdater(String updater){
        this.updater = updater;
    }
    public String getUpdater(){
        return updater;
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
        CscpFlowProcessDefExt other=(CscpFlowProcessDefExt)that;
        return (this.getDescription() ==null?this.getDescription() !=null:this.getDescription().equals(other.getDescription()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreator() ==null?this.getCreator() !=null:this.getCreator().equals(other.getCreator()))
                && (this.getModelId() ==null?this.getModelId() !=null:this.getModelId().equals(other.getModelId()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getProcessDefinitionId() ==null?this.getProcessDefinitionId() !=null:this.getProcessDefinitionId().equals(other.getProcessDefinitionId()))
                && (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdater() ==null?this.getUpdater() !=null:this.getUpdater().equals(other.getUpdater()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getModelId() == null) ? 0 : getModelId().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getProcessDefinitionId() == null) ? 0 : getProcessDefinitionId().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdater() == null) ? 0 : getUpdater().hashCode());
        return result;
    }
}
