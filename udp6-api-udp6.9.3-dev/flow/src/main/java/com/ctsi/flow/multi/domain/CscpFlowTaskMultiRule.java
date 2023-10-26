package com.ctsi.flow.multi.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpFlowTaskMultiRule 实体类
 *
 * @author hx
 * @date 2022-12-01 15:33:44
 *
 */

@ApiModel(description = "CscpFlowTaskMultiRule")
public class CscpFlowTaskMultiRule  implements Serializable{
    /**
     * Id
     *
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
     * 流程任务定义的 key
     *
     */

    private String taskDefinitionKey;
    /**
     * 多实例策略
     *
     */

    private String taskMulti;
    /**
     * 策略值
     *
     */

    private Integer scheme;
    /**
     * 创建人
     *
     */

    private String creator;
    /**
     * 创建时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;
    /**
     * 更新人
     *
     */

    private String updater;
    /**
     * 更新时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;



    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setTaskDefinitionKey(String taskDefinitionKey){
        this.taskDefinitionKey = taskDefinitionKey;
    }
    public String getTaskDefinitionKey(){
        return taskDefinitionKey;
    }
    public void setTaskMulti(String taskMulti){
        this.taskMulti = taskMulti;
    }
    public String getTaskMulti(){
        return taskMulti;
    }
    public void setScheme(Integer scheme){
        this.scheme = scheme;
    }
    public Integer getScheme(){
        return scheme;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }
    public String getCreator(){
        return creator;
    }
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
    }
    public void setUpdater(String updater){
        this.updater = updater;
    }
    public String getUpdater(){
        return updater;
    }
    public void setUpdateTime(ZonedDateTime updateTime){
        this.updateTime = updateTime;
    }
    public ZonedDateTime getUpdateTime(){
        return updateTime;
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
        CscpFlowTaskMultiRule other=(CscpFlowTaskMultiRule)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getTaskDefinitionKey() ==null?this.getTaskDefinitionKey() !=null:this.getTaskDefinitionKey().equals(other.getTaskDefinitionKey()))
                && (this.getTaskMulti() ==null?this.getTaskMulti() !=null:this.getTaskMulti().equals(other.getTaskMulti()))
                && (this.getScheme() ==null?this.getScheme() !=null:this.getScheme().equals(other.getScheme()))
                && (this.getCreator() ==null?this.getCreator() !=null:this.getCreator().equals(other.getCreator()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdater() ==null?this.getUpdater() !=null:this.getUpdater().equals(other.getUpdater()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskDefinitionKey() == null) ? 0 : getTaskDefinitionKey().hashCode());
        result = prime * result + ((getTaskMulti() == null) ? 0 : getTaskMulti().hashCode());
        result = prime * result + ((getScheme() == null) ? 0 : getScheme().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdater() == null) ? 0 : getUpdater().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
