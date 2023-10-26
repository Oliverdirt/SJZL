package com.ctsi.flow.param.model;

import java.io.Serializable;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;

import javax.validation.constraints.NotNull;

/**
 *  ActOperatePerm 实体类
 *
 * @author hx
 * @date 2022-07-21 18:37:18
 *
 */

@ApiModel(description = "ActOperatePerm")
public class ActOperatePerm  implements Serializable{
    /**
    * 唯一标识
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 任务标识
    *
    */

    @NotNull(message = "任务标识不能为空")
    private String taskDefinitionKey;
    /**
    * 任务名称
    *
    */

    private String taskDefinitionName;
    /**
    * 操作权限
    *
    */

    private String perms;

    /**
    * 流程标识
    *
    */

    private String taskDefinitionFlag;


    public String getTaskDefinitionFlag() {
        return taskDefinitionFlag;
    }

    public void setTaskDefinitionFlag(String taskDefinitionFlag) {
        this.taskDefinitionFlag = taskDefinitionFlag;
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTaskDefinitionName() {
        return taskDefinitionName;
    }

    public void setTaskDefinitionName(String taskDefinitionName) {
        this.taskDefinitionName = taskDefinitionName;
    }

    public void setPerms(String perms){
        this.perms = perms;
    }
    public String getPerms(){
        return perms;
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
        ActOperatePerm other=(ActOperatePerm)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getTaskDefinitionKey() ==null?this.getTaskDefinitionKey() !=null:this.getTaskDefinitionKey().equals(other.getTaskDefinitionKey()))
                && (this.getTaskDefinitionName() ==null?this.getTaskDefinitionName() !=null:this.getTaskDefinitionName().equals(other.getTaskDefinitionName()))
                && (this.getPerms() ==null?this.getPerms() !=null:this.getPerms().equals(other.getPerms()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskDefinitionKey() == null) ? 0 : getTaskDefinitionKey().hashCode());
        result = prime * result + ((getTaskDefinitionName() == null) ? 0 : getTaskDefinitionName().hashCode());
        result = prime * result + ((getPerms() == null) ? 0 : getPerms().hashCode());
        return result;
    }
}
