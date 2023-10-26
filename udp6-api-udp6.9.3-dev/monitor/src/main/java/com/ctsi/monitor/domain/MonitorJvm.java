package com.ctsi.monitor.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  MonitorJvm 实体类
 *
 * @author hx
 * @date 2022-05-27 14:02:10
 *
 */

@ApiModel(description = "MonitorJvm")
public class MonitorJvm  implements Serializable{
    /**
    * id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 当前JVM占用的内存总数(M)
    *
    */

    private Double total;
    /**
    * JVM已使用内存(M)
    *
    */

    private Double max;
    /**
    * JVM空闲内存(M)
    *
    */

    private Double free;
    /**
    * 已用JVM
    *
    */

    private Double used;
    /**
    * 使用率
    *
    */

    private Double usages;
    /**
    * 创建时间
    *
    */

    private String time;



    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setTotal(Double total){
        this.total = total;
    }
    public Double getTotal(){
        return total;
    }
    public void setMax(Double max){
        this.max = max;
    }
    public Double getMax(){
        return max;
    }
    public void setFree(Double free){
        this.free = free;
    }
    public Double getFree(){
        return free;
    }
    public void setUsed(Double used){
        this.used = used;
    }
    public Double getUsed(){
        return used;
    }
    public void setUsages(Double usages){
        this.usages = usages;
    }
    public Double getUsages(){
        return usages;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
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
        MonitorJvm other=(MonitorJvm)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getTotal() ==null?this.getTotal() !=null:this.getTotal().equals(other.getTotal()))
                && (this.getMax() ==null?this.getMax() !=null:this.getMax().equals(other.getMax()))
                && (this.getFree() ==null?this.getFree() !=null:this.getFree().equals(other.getFree()))
                && (this.getUsed() ==null?this.getUsed() !=null:this.getUsed().equals(other.getUsed()))
                && (this.getUsages() ==null?this.getUsages() !=null:this.getUsages().equals(other.getUsages()))
                && (this.getTime() ==null?this.getTime() !=null:this.getTime().equals(other.getTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTotal() == null) ? 0 : getTotal().hashCode());
        result = prime * result + ((getMax() == null) ? 0 : getMax().hashCode());
        result = prime * result + ((getFree() == null) ? 0 : getFree().hashCode());
        result = prime * result + ((getUsed() == null) ? 0 : getUsed().hashCode());
        result = prime * result + ((getUsages() == null) ? 0 : getUsages().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        return result;
    }
}
