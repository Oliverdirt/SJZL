package com.ctsi.flow.approve.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
/**
 *  Approve 实体类
 *
 * @author hx
 * @date 2022-11-03 19:50:16
 *
 */

@ApiModel(description = "Approve")
public class Approve  implements Serializable{
    /**
    * Id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 天数
    *
    */

    private Integer day;
    /**
    * 理由
    *
    */

    private String reason;
    /**
    * 出发地
    *
    */

    private String fromArea;
    /**
    * 前往地
    *
    */

    private String toArea;
    /**
    * 用户ID
    *
    */

    private Integer userId;
    /**
    * 实例ID
    *
    */

    private String instanceId;



    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setDay(Integer day){
        this.day = day;
    }
    public Integer getDay(){
        return day;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getReason(){
        return reason;
    }
    public void setFromArea(String fromArea){
        this.fromArea = fromArea;
    }
    public String getFromArea(){
        return fromArea;
    }
    public void setToArea(String toArea){
        this.toArea = toArea;
    }
    public String getToArea(){
        return toArea;
    }
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setInstanceId(String instanceId){
        this.instanceId = instanceId;
    }
    public String getInstanceId(){
        return instanceId;
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
        Approve other=(Approve)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getDay() ==null?this.getDay() !=null:this.getDay().equals(other.getDay()))
                && (this.getReason() ==null?this.getReason() !=null:this.getReason().equals(other.getReason()))
                && (this.getFromArea() ==null?this.getFromArea() !=null:this.getFromArea().equals(other.getFromArea()))
                && (this.getToArea() ==null?this.getToArea() !=null:this.getToArea().equals(other.getToArea()))
                && (this.getUserId() ==null?this.getUserId() !=null:this.getUserId().equals(other.getUserId()))
                && (this.getInstanceId() ==null?this.getInstanceId() !=null:this.getInstanceId().equals(other.getInstanceId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDay() == null) ? 0 : getDay().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getFromArea() == null) ? 0 : getFromArea().hashCode());
        result = prime * result + ((getToArea() == null) ? 0 : getToArea().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getInstanceId() == null) ? 0 : getInstanceId().hashCode());
        return result;
    }
}
