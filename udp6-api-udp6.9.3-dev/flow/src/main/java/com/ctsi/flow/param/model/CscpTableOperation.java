package com.ctsi.flow.param.model;

import java.io.Serializable;
import java.util.List;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;

import javax.persistence.Transient;

/**
 *  CscpTableOperation 实体类
 *
 * @author hx
 * @date 2022-07-26 16:50:51
 *
 */

@ApiModel(description = "CscpTableOperation")
public class CscpTableOperation  implements Serializable{
    /**
     * 唯一标识
     *
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
     * 字段id
     *
     */

    private String fieldId;
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
     * 任务标识
     *
     */

    private String taskDefinitionKey;
    /**
     * 0可编辑1不可编辑
     *
     */

    private String perm;

    /**
     * 流程标识
     *
     */

    private String taskDefinitionFlag;

    @Transient
    private List<String> fieldIds;

    public String getTaskDefinitionFlag() {
        return taskDefinitionFlag;
    }

    public void setTaskDefinitionFlag(String taskDefinitionFlag) {
        this.taskDefinitionFlag = taskDefinitionFlag;
    }

    public List<String> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(List<String> fieldIds) {
        this.fieldIds = fieldIds;
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setFieldId(String fieldId){
        this.fieldId = fieldId;
    }
    public String getFieldId(){
        return fieldId;
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
    public void setPerm(String perm){
        this.perm = perm;
    }
    public String getPerm(){
        return perm;
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
        CscpTableOperation other=(CscpTableOperation)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getFieldId() ==null?this.getFieldId() !=null:this.getFieldId().equals(other.getFieldId()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getFieldName() ==null?this.getFieldName() !=null:this.getFieldName().equals(other.getFieldName()))
                && (this.getTaskDefinitionKey() ==null?this.getTaskDefinitionKey() !=null:this.getTaskDefinitionKey().equals(other.getTaskDefinitionKey()))
                && (this.getPerm() ==null?this.getPerm() !=null:this.getPerm().equals(other.getPerm()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFieldId() == null) ? 0 : getFieldId().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + ((getTaskDefinitionKey() == null) ? 0 : getTaskDefinitionKey().hashCode());
        result = prime * result + ((getPerm() == null) ? 0 : getPerm().hashCode());
        return result;
    }
}
