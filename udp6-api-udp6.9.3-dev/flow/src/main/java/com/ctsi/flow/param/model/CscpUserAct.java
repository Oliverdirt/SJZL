package com.ctsi.flow.param.model;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *  CscpUserAct 实体类
 *
 * @author hx
 * @date 2022-07-26 15:55:12
 *
 */

@ApiModel(description = "CscpUserAct")
public class CscpUserAct  implements Serializable{
    /**
    * id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 用户id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long userId;
    /**
    * 流程id
    *
    */

    private String procDefId;
    /**
    * 流程名称
    *
    */

    private String procDefName;
    /**
    * 表单id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long formId;
    /**
    * DefinedId
    *
    */

    private String definedId;

    /**
     * 是否收藏（1 是   2 否）
     *
     */
    private String isCollect;


    private String processId;

    /**
     * 收藏时间
     *
     * */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private ZonedDateTime collectTime;

    /**
     * 流程发布时间
     *
     * */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private ZonedDateTime publishTime;

    public ZonedDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(ZonedDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public ZonedDateTime getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(ZonedDateTime collectTime) {
        this.collectTime = collectTime;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public Long getUserId(){
        return userId;
    }
    public void setProcDefId(String procDefId){
        this.procDefId = procDefId;
    }
    public String getProcDefId(){
        return procDefId;
    }
    public void setProcDefName(String procDefName){
        this.procDefName = procDefName;
    }
    public String getProcDefName(){
        return procDefName;
    }
    public void setFormId(Long formId){
        this.formId = formId;
    }
    public Long getFormId(){
        return formId;
    }
    public void setDefinedId(String definedId){
        this.definedId = definedId;
    }
    public String getDefinedId(){
        return definedId;
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
        CscpUserAct other=(CscpUserAct)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getUserId() ==null?this.getUserId() !=null:this.getUserId().equals(other.getUserId()))
                && (this.getProcDefId() ==null?this.getProcDefId() !=null:this.getProcDefId().equals(other.getProcDefId()))
                && (this.getProcDefName() ==null?this.getProcDefName() !=null:this.getProcDefName().equals(other.getProcDefName()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getIsCollect() ==null?this.getIsCollect() !=null:this.getIsCollect().equals(other.getIsCollect()))
                && (this.getProcessId() ==null?this.getProcessId() !=null:this.getProcessId().equals(other.getProcessId()))
                && (this.getCollectTime() ==null?this.getCollectTime() !=null:this.getCollectTime().equals(other.getCollectTime()))
                && (this.getPublishTime() ==null?this.getPublishTime() !=null:this.getPublishTime().equals(other.getPublishTime()))
                && (this.getDefinedId() ==null?this.getDefinedId() !=null:this.getDefinedId().equals(other.getDefinedId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getProcDefId() == null) ? 0 : getProcDefId().hashCode());
        result = prime * result + ((getProcDefName() == null) ? 0 : getProcDefName().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getDefinedId() == null) ? 0 : getDefinedId().hashCode());
        result = prime * result + ((getProcessId() == null) ? 0 : getProcessId().hashCode());
        result = prime * result + ((getCollectTime() == null) ? 0 : getCollectTime().hashCode());
        result = prime * result + ((getPublishTime() == null) ? 0 : getPublishTime().hashCode());
        result = prime * result + ((getIsCollect() == null) ? 0 : getIsCollect().hashCode());
        return result;
    }
}
