package com.ctsi.flow.param.model;

import java.io.Serializable;
import java.util.List;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;

import javax.persistence.Transient;

/**
 *  CscpFlowFieldPerms 实体类
 *
 * @author hx
 * @date 2022-10-24 15:47:57
 *
 */

@ApiModel(description = "CscpFlowFieldPerms")
public class CscpFlowFieldPerms  implements Serializable{
    /**
    * 主键id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 字段权限
    *
    */

    private String fieldPerms;
    /**
    * 表单id
    *
    */

    private String formId;
    /**
    * 表单名称
    *
    */

    private String fieldName;
    /**
    * 流程任务id
    *
    */

    private String taskDefinitionKey;
    /**
    * 流程标识
    *
    */

    private String taskDefinitionName;


    /**
     * 字段权限1
     *
     */
    @Transient
    @JsonSerialize
    private List<FieldPerm> fieldsPerms;

    public List<FieldPerm> getFieldsPerms() {
        return fieldsPerms;
    }

    public void setFieldsPerms(List<FieldPerm> fieldsPerms) {
        this.fieldsPerms = fieldsPerms;
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setFieldPerms(String fieldPerms){
        this.fieldPerms = fieldPerms;
    }
    public String getFieldPerms(){
        return fieldPerms;
    }
    public void setFormId(String formId){
        this.formId = formId;
    }
    public String getFormId(){
        return formId;
    }
    public void setFieldName(String fieldName){
        this.fieldName = fieldName;
    }
    public String getFieldName(){
        return fieldName;
    }
    public void setTaskDefinitionKey(String taskDefinitionKey){
        this.taskDefinitionKey = taskDefinitionKey;
    }
    public String getTaskDefinitionKey(){
        return taskDefinitionKey;
    }
    public void setTaskDefinitionName(String taskDefinitionName){
        this.taskDefinitionName = taskDefinitionName;
    }
    public String getTaskDefinitionName(){
        return taskDefinitionName;
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
        CscpFlowFieldPerms other=(CscpFlowFieldPerms)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getFieldPerms() ==null?this.getFieldPerms() !=null:this.getFieldPerms().equals(other.getFieldPerms()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getFieldName() ==null?this.getFieldName() !=null:this.getFieldName().equals(other.getFieldName()))
                && (this.getTaskDefinitionKey() ==null?this.getTaskDefinitionKey() !=null:this.getTaskDefinitionKey().equals(other.getTaskDefinitionKey()))
                && (this.getTaskDefinitionName() ==null?this.getTaskDefinitionName() !=null:this.getTaskDefinitionName().equals(other.getTaskDefinitionName()))
                && (this.getFieldsPerms() ==null?this.getFieldsPerms() !=null:this.getFieldsPerms().equals(other.getFieldsPerms()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFieldPerms() == null) ? 0 : getFieldPerms().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + ((getTaskDefinitionKey() == null) ? 0 : getTaskDefinitionKey().hashCode());
        result = prime * result + ((getTaskDefinitionName() == null) ? 0 : getTaskDefinitionName().hashCode());
        return result;
    }
}
