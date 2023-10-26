package com.ctsi.ssdc.sysconfig.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *  CscpSysConfig 实体类
 *
 * @author hx
 * @date 2022-08-24 14:40:57
 *
 */

@ApiModel(description = "CscpSysConfig")
public class CscpSysConfig  implements Serializable{
    /**
    * 参数主键
    *
    */
    @AutoId(primaryKey = "config_id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long configId;
    /**
    * 参数名称
    *
    */
    @NotBlank(message = "参数名称不能为空！")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+([a-zA-Z0-9]?)+$",message = "参数名称必须中文开头后可跟字母、数字！！！")
    @Length(max = 20,message = "参数名称长度小于等于20")
    private String configName;
    /**
    * 参数键名
    *
    */
    @NotBlank(message = "参数键名不能为空！")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9._-]+",message = "参数键名必须以字母开头，后边可跟字母、数字、下划线、短中线和小数点。")
    @Length(max = 100,message = "参数名称长度小于等于100")
    private String configKey;
    /**
    * 参数键值
    *
    */
    @NotBlank(message = "参数键值不能为空！")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9._-]+",message = "参数键值必须以字母开头，后边可跟字母、数字、下划线、短中线和小数点。")
    @Length(max = 100,message = "参数名称长度小于等于100")
    private String configValue;
    /**
    * 参数配置项
    *
    */

    private String configOption;
    /**
    * 创建者
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long createBy;
    /**
    * 创建时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createTime;
    /**
    * 更新者
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long updateBy;
    /**
    * 更新时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;
    /**
    * 备注
    *
    */
    @Length(max = 500,message = "参数备注长度小于等于500")
    private String remark;

    /**
     * 开启状态
     *
     */
    private Integer configStatus;




    public void setConfigId(Long configId){
        this.configId = configId;
    }
    public Long getConfigId(){
        return configId;
    }
    public void setConfigName(String configName){
        this.configName = configName;
    }
    public String getConfigName(){
        return configName;
    }
    public void setConfigKey(String configKey){
        this.configKey = configKey;
    }
    public String getConfigKey(){
        return configKey;
    }
    public void setConfigValue(String configValue){
        this.configValue = configValue;
    }
    public String getConfigValue(){
        return configValue;
    }
    public void setConfigOption(String configOption){
        this.configOption = configOption;
    }
    public String getConfigOption(){
        return configOption;
    }
    public void setCreateBy(Long createBy){
        this.createBy = createBy;
    }
    public Long getCreateBy(){
        return createBy;
    }
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
    }
    public void setUpdateBy(Long updateBy){
        this.updateBy = updateBy;
    }
    public Long getUpdateBy(){
        return updateBy;
    }
    public void setUpdateTime(ZonedDateTime updateTime){
        this.updateTime = updateTime;
    }
    public ZonedDateTime getUpdateTime(){
        return updateTime;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return remark;
    }

    public void setConfigStatus(Integer configStatus){
        this.configStatus = configStatus;
    }
    public Integer getConfigStatus(){
        return configStatus;
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
        CscpSysConfig other=(CscpSysConfig)that;
        return (this.getConfigId() ==null?this.getConfigId() !=null:this.getConfigId().equals(other.getConfigId()))
                && (this.getConfigName() ==null?this.getConfigName() !=null:this.getConfigName().equals(other.getConfigName()))
                && (this.getConfigKey() ==null?this.getConfigKey() !=null:this.getConfigKey().equals(other.getConfigKey()))
                && (this.getConfigValue() ==null?this.getConfigValue() !=null:this.getConfigValue().equals(other.getConfigValue()))
                && (this.getConfigOption() ==null?this.getConfigOption() !=null:this.getConfigOption().equals(other.getConfigOption()))
                && (this.getCreateBy() ==null?this.getCreateBy() !=null:this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getRemark() ==null?this.getRemark() !=null:this.getRemark().equals(other.getRemark()))
                && (this.getConfigStatus() ==null?this.getConfigStatus() !=null:this.getConfigStatus().equals(other.getConfigStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getConfigId() == null) ? 0 : getConfigId().hashCode());
        result = prime * result + ((getConfigName() == null) ? 0 : getConfigName().hashCode());
        result = prime * result + ((getConfigKey() == null) ? 0 : getConfigKey().hashCode());
        result = prime * result + ((getConfigValue() == null) ? 0 : getConfigValue().hashCode());
        result = prime * result + ((getConfigOption() == null) ? 0 : getConfigOption().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getConfigStatus() == null) ? 0 : getConfigStatus().hashCode());
        return result;
    }
}
