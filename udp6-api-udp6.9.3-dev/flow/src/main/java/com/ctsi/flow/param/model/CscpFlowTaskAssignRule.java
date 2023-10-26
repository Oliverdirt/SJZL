package com.ctsi.flow.param.model;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpFlowTaskAssignRule 实体类
 *
 * @author hx
 * @date 2022-08-05 10:19:18
 *
 */

@ApiModel(description = "CscpFlowTaskAssignRule")
public class CscpFlowTaskAssignRule  implements Serializable{
    /**
    * 流程任务定义的 key
    *
    */

    private String taskDefinitionKey;
    /**
    * 流程定义的编号
    *
    */

    private String processDefinitionId;
    /**
    * 规则值，JSON 数组
    *
    */

    private String options;
    /**
    * 创建者
    *
    */

    private String creator;
    /**
    * 更新者
    *
    */

    private String updater;
    /**
    * 规则类型
    *
    */

    private Integer type;
    /**
    * 流程模型的编号
    *
    */

    private String modelId;
    /**
    * 编号
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 租户编号
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tenantId;
    /**
    * 更新时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;
    /**
    * 创建时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;



    public void setTaskDefinitionKey(String taskDefinitionKey){
        this.taskDefinitionKey = taskDefinitionKey;
    }
    public String getTaskDefinitionKey(){
        return taskDefinitionKey;
    }
    public void setProcessDefinitionId(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    public String getProcessDefinitionId(){
        return processDefinitionId;
    }
    public void setOptions(String options){
        this.options = options;
    }
    public String getOptions(){
        return options;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }
    public String getCreator(){
        return creator;
    }
    public void setUpdater(String updater){
        this.updater = updater;
    }
    public String getUpdater(){
        return updater;
    }
    public void setType(Integer type){
        this.type = type;
    }
    public Integer getType(){
        return type;
    }
    public void setModelId(String modelId){
        this.modelId = modelId;
    }
    public String getModelId(){
        return modelId;
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
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
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
        CscpFlowTaskAssignRule other=(CscpFlowTaskAssignRule)that;
        return (this.getTaskDefinitionKey() ==null?this.getTaskDefinitionKey() !=null:this.getTaskDefinitionKey().equals(other.getTaskDefinitionKey()))
                && (this.getProcessDefinitionId() ==null?this.getProcessDefinitionId() !=null:this.getProcessDefinitionId().equals(other.getProcessDefinitionId()))
                && (this.getOptions() ==null?this.getOptions() !=null:this.getOptions().equals(other.getOptions()))
                && (this.getCreator() ==null?this.getCreator() !=null:this.getCreator().equals(other.getCreator()))
                && (this.getUpdater() ==null?this.getUpdater() !=null:this.getUpdater().equals(other.getUpdater()))
                && (this.getType() ==null?this.getType() !=null:this.getType().equals(other.getType()))
                && (this.getModelId() ==null?this.getModelId() !=null:this.getModelId().equals(other.getModelId()))
                && (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTaskDefinitionKey() == null) ? 0 : getTaskDefinitionKey().hashCode());
        result = prime * result + ((getProcessDefinitionId() == null) ? 0 : getProcessDefinitionId().hashCode());
        result = prime * result + ((getOptions() == null) ? 0 : getOptions().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getUpdater() == null) ? 0 : getUpdater().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getModelId() == null) ? 0 : getModelId().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}
