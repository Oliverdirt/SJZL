package com.ctsi.ssdc.errorcode.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.errorcode.exception.enums.ErrorCodeTypeEnum;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *  CscpErrorCode 实体类
 *
 * @author hx
 * @date 2022-09-05 15:50:09
 *
 */

@ApiModel(description = "CscpErrorCode")
public class CscpErrorCode  implements Serializable{
    /**
     * 错误码id
     *
     */
    @AutoId(primaryKey = "code_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long codeId;
    /**
     * 错误码类型
     *
     */

    private String codeType;
    /**
     * 应用名
     *
     */

    private String applicationName;
    /**
     * 错误码编码
     *
     */

    private Integer code;
    /**
     * 错误码错误提示
     *
     */

    private String codeMessage;
    /**
     * 备注
     *
     */

    private String remark;
    /**
     * 创建者
     *
     */

    private String createBy;
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

    private String updateBy;
    /**
     * 更新时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;



    public void setCodeId(Long codeId){
        this.codeId = codeId;
    }
    public Long getCodeId(){
        return codeId;
    }
    public void setCodeType(String codeType){
        this.codeType = codeType;
    }
    public String getCodeType(){
        return this.codeType;
    }
    public void setApplicationName(String applicationName){
        this.applicationName = applicationName;
    }
    public String getApplicationName(){
        return applicationName;
    }
    public void setCode(Integer code){
        this.code = code;
    }
    public Integer getCode(){
        return code;
    }
    public void setCodeMessage(String codeMessage){
        this.codeMessage = codeMessage;
    }
    public String getCodeMessage(){
        return codeMessage;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return remark;
    }
    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }
    public String getCreateBy(){
        return createBy;
    }
    public void setCreateTime(ZonedDateTime createTime){
        this.createTime = createTime;
    }
    public ZonedDateTime getCreateTime(){
        return createTime;
    }
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
    public String getUpdateBy(){
        return updateBy;
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
        CscpErrorCode other=(CscpErrorCode)that;
        return (this.getCodeId() ==null?this.getCodeId() !=null:this.getCodeId().equals(other.getCodeId()))
                && (this.getCodeType() ==null?this.getCodeType() !=null:this.getCodeType().equals(other.getCodeType()))
                && (this.getApplicationName() ==null?this.getApplicationName() !=null:this.getApplicationName().equals(other.getApplicationName()))
                && (this.getCode() ==null?this.getCode() !=null:this.getCode().equals(other.getCode()))
                && (this.getCodeMessage() ==null?this.getCodeMessage() !=null:this.getCodeMessage().equals(other.getCodeMessage()))
                && (this.getRemark() ==null?this.getRemark() !=null:this.getRemark().equals(other.getRemark()))
                && (this.getCreateBy() ==null?this.getCreateBy() !=null:this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCodeId() == null) ? 0 : getCodeId().hashCode());
        result = prime * result + ((getCodeType() == null) ? 0 : getCodeType().hashCode());
        result = prime * result + ((getApplicationName() == null) ? 0 : getApplicationName().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getCodeMessage() == null) ? 0 : getCodeMessage().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
