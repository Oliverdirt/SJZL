package com.ctsi.ssdc.quartz.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;

/**
 *  CscpJobLog 实体类
 *
 * @author hx
 * @date 2022-05-30 17:05:20
 *
 */

@ApiModel(description = "CscpJobLog")
public class CscpJobLog implements Serializable{
    /**
    * 日志信息
    *
    */

    private String jobMessage;
    /**
    * 异常信息
    *
    */

    private String exceptionInfo;
    /**
    * 调用目标字符串
    *
    */

    private String invokeTarget;
    /**
    * 任务组名
    *
    */

    private String jobGroup;
    /**
    * 执行状态
    *
    */

    private String status;
    /**
    * 执行时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime executionTime;
    /**
    * 任务名称
    *
    */

    private String jobName;
    /**
    * 任务日志ID
    *
    */
    @AutoId(primaryKey = "job_log_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long jobLogId;

    /** 开始时间 */
    @Transient
    private Date startTime;

    /** 停止时间 */
    @Transient
    private Date stopTime;


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public void setJobMessage(String jobMessage){
        this.jobMessage = jobMessage;
    }
    public String getJobMessage(){
        return jobMessage;
    }
    public void setExceptionInfo(String exceptionInfo){
        this.exceptionInfo = exceptionInfo;
    }
    public String getExceptionInfo(){
        return exceptionInfo;
    }
    public void setInvokeTarget(String invokeTarget){
        this.invokeTarget = invokeTarget;
    }
    public String getInvokeTarget(){
        return invokeTarget;
    }
    public void setJobGroup(String jobGroup){
        this.jobGroup = jobGroup;
    }
    public String getJobGroup(){
        return jobGroup;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
    public void setExecutionTime(ZonedDateTime executionTime){
        this.executionTime = executionTime;
    }
    public ZonedDateTime getExecutionTime(){
        return executionTime;
    }
    public void setJobName(String jobName){
        this.jobName = jobName;
    }
    public String getJobName(){
        return jobName;
    }
    public void setJobLogId(Long jobLogId){
        this.jobLogId = jobLogId;
    }
    public Long getJobLogId(){
        return jobLogId;
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
        CscpJobLog other=(CscpJobLog)that;
        return (this.getJobMessage() ==null?this.getJobMessage() !=null:this.getJobMessage().equals(other.getJobMessage()))
                && (this.getExceptionInfo() ==null?this.getExceptionInfo() !=null:this.getExceptionInfo().equals(other.getExceptionInfo()))
                && (this.getInvokeTarget() ==null?this.getInvokeTarget() !=null:this.getInvokeTarget().equals(other.getInvokeTarget()))
                && (this.getJobGroup() ==null?this.getJobGroup() !=null:this.getJobGroup().equals(other.getJobGroup()))
                && (this.getStatus() ==null?this.getStatus() !=null:this.getStatus().equals(other.getStatus()))
                && (this.getExecutionTime() ==null?this.getExecutionTime() !=null:this.getExecutionTime().equals(other.getExecutionTime()))
                && (this.getJobName() ==null?this.getJobName() !=null:this.getJobName().equals(other.getJobName()))
                && (this.getJobLogId() ==null?this.getJobLogId() !=null:this.getJobLogId().equals(other.getJobLogId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getJobMessage() == null) ? 0 : getJobMessage().hashCode());
        result = prime * result + ((getExceptionInfo() == null) ? 0 : getExceptionInfo().hashCode());
        result = prime * result + ((getInvokeTarget() == null) ? 0 : getInvokeTarget().hashCode());
        result = prime * result + ((getJobGroup() == null) ? 0 : getJobGroup().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getExecutionTime() == null) ? 0 : getExecutionTime().hashCode());
        result = prime * result + ((getJobName() == null) ? 0 : getJobName().hashCode());
        result = prime * result + ((getJobLogId() == null) ? 0 : getJobLogId().hashCode());
        return result;
    }
}
