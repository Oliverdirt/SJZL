package com.ctsi.flow.param.model;

import java.io.Serializable;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;

/**
 *  ActRdTurnTask 实体类
 *
 * @author hx
 * @date 2022-07-28 18:44:24
 *
 */

@ApiModel(description = "ActRdTurnTask")
public class ActRdTurnTask  implements Serializable{
    /**
     * 唯一标识
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
     * 表单id
     *
     */

    private String formId;
    /**
     * 流程id
     *
     */

    private String processId;
    /**
     * 流程实例id
     *
     */

    private String processInstid;
    /**
     * 流程名称
     *
     */

    private String processName;
    /**
     * 1传阅2转办
     *
     */

    private String type;
    /**
     * 传阅或转办时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;

    @JsonSerialize(using = LongtoStringSerialize.class)

    /**
     * 当前处理人
     */
    private Long curAssignee;

    @Transient
    private String curAssigneeName;

    /**
     * 上一个处理人
     */
    private Long lastAssignee;

    @Transient
    private String lastAssigneeName;

    public String getCurAssigneeName() {
        return curAssigneeName;
    }

    public void setCurAssigneeName(String curAssigneeName) {
        this.curAssigneeName = curAssigneeName;
    }

    public String getLastAssigneeName() {
        return lastAssigneeName;
    }

    public void setLastAssigneeName(String lastAssigneeName) {
        this.lastAssigneeName = lastAssigneeName;
    }

    public Long getCurAssignee() {
        return curAssignee;
    }

    public void setCurAssignee(Long curAssignee) {
        this.curAssignee = curAssignee;
    }

    public Long getLastAssignee() {
        return lastAssignee;
    }

    public void setLastAssignee(Long lastAssignee) {
        this.lastAssignee = lastAssignee;
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
    public void setFormId(String formId){
        this.formId = formId;
    }
    public String getFormId(){
        return formId;
    }
    public void setProcessId(String processId){
        this.processId = processId;
    }
    public String getProcessId(){
        return processId;
    }
    public void setProcessInstid(String processInstid){
        this.processInstid = processInstid;
    }
    public String getProcessInstid(){
        return processInstid;
    }
    public void setProcessName(String processName){
        this.processName = processName;
    }
    public String getProcessName(){
        return processName;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
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
        ActRdTurnTask other=(ActRdTurnTask)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getUserId() ==null?this.getUserId() !=null:this.getUserId().equals(other.getUserId()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getProcessId() ==null?this.getProcessId() !=null:this.getProcessId().equals(other.getProcessId()))
                && (this.getProcessInstid() ==null?this.getProcessInstid() !=null:this.getProcessInstid().equals(other.getProcessInstid()))
                && (this.getProcessName() ==null?this.getProcessName() !=null:this.getProcessName().equals(other.getProcessName()))
                && (this.getType() ==null?this.getType() !=null:this.getType().equals(other.getType()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getProcessId() == null) ? 0 : getProcessId().hashCode());
        result = prime * result + ((getProcessInstid() == null) ? 0 : getProcessInstid().hashCode());
        result = prime * result + ((getProcessName() == null) ? 0 : getProcessName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}
