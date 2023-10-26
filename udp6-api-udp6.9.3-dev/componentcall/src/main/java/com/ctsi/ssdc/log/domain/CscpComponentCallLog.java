package com.ctsi.ssdc.log.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 *  CscpComponentCallLog 实体类
 *
 * @author hx
 * @date 2022-03-19 11:01:42
 *
 */

@ApiModel(description = "CscpComponentCallLog")
public class CscpComponentCallLog  implements Serializable{
    /**
    * 主键
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;
    /**
    * 部门名称
    *
    */

    private String company;
    /**
    * 项目名称
    *
    */

    private String projectName;
    /**
    * 调用组件
    *
    */

    private String componentName;
    /**
    * 调用方法
    *
    */

    private String componentMethodName;
    /**
    * 调用时间
    *
    */
    @JSONField(format="yyyy-MM-dd",name = "call_time")
    private Date callTime;
    /**
    * 上传标志
    *
    */

    private Integer uploadFlag;



    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setCompany(String company){
        this.company = company;
    }
    public String getCompany(){
        return company;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public String getProjectName(){
        return projectName;
    }
    public void setComponentName(String componentName){
        this.componentName = componentName;
    }
    public String getComponentName(){
        return componentName;
    }
    public void setComponentMethodName(String componentMethodName){
        this.componentMethodName = componentMethodName;
    }
    public String getComponentMethodName(){
        return componentMethodName;
    }
    public void setCallTime(Date callTime){
        this.callTime = callTime == null ? null : (Date)callTime.clone();

    }
    public Date getCallTime(){
        return callTime == null ? null : (Date)callTime.clone();
    }
    public void setUploadFlag(Integer uploadFlag){
        this.uploadFlag = uploadFlag;
    }
    public Integer getUploadFlag(){
        return uploadFlag;
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
        CscpComponentCallLog other=(CscpComponentCallLog)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getCompany() ==null?this.getCompany() !=null:this.getCompany().equals(other.getCompany()))
                && (this.getProjectName() ==null?this.getProjectName() !=null:this.getProjectName().equals(other.getProjectName()))
                && (this.getComponentName() ==null?this.getComponentName() !=null:this.getComponentName().equals(other.getComponentName()))
                && (this.getComponentMethodName() ==null?this.getComponentMethodName() !=null:this.getComponentMethodName().equals(other.getComponentMethodName()))
                && (this.getCallTime() ==null?this.getCallTime() !=null:this.getCallTime().equals(other.getCallTime()))
                && (this.getUploadFlag() ==null?this.getUploadFlag() !=null:this.getUploadFlag().equals(other.getUploadFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompany() == null) ? 0 : getCompany().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getComponentName() == null) ? 0 : getComponentName().hashCode());
        result = prime * result + ((getComponentMethodName() == null) ? 0 : getComponentMethodName().hashCode());
        result = prime * result + ((getCallTime() == null) ? 0 : getCallTime().hashCode());
        result = prime * result + ((getUploadFlag() == null) ? 0 : getUploadFlag().hashCode());
        return result;
    }
}
