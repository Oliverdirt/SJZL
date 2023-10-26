package com.ctsi.ssdc.admin.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpPost 实体类
 *
 * @author hx
 * @date 2022-08-31 15:23:50
 *
 */

@ApiModel(description = "CscpPost")
public class CscpPost  implements Serializable{
    /**
     * 岗位id
     *
     */
    @AutoId(primaryKey = "post_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long postId;
    /**
     * 岗位编码
     *
     */

    private String postCode;
    /**
     * 岗位名称
     *
     */

    private String postName;
    /**
     * 排序显示
     *
     */

    private Integer postSort;
    /**
     * 岗位状态
     *
     */

    private String status;
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
    /**
     * 备注
     *
     */

    private String remark;
    /**
     * 租户id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tenantId;



    public void setPostId(Long postId){
        this.postId = postId;
    }
    public Long getPostId(){
        return postId;
    }
    public void setPostCode(String postCode){
        this.postCode = postCode;
    }
    public String getPostCode(){
        return postCode;
    }
    public void setPostName(String postName){
        this.postName = postName;
    }
    public String getPostName(){
        return postName;
    }
    public void setPostSort(Integer postSort){
        this.postSort = postSort;
    }
    public Integer getPostSort(){
        return postSort;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
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
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return remark;
    }
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    public Long getTenantId(){
        return tenantId;
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
        CscpPost other=(CscpPost)that;
        return (this.getPostId() ==null?this.getPostId() !=null:this.getPostId().equals(other.getPostId()))
                && (this.getPostCode() ==null?this.getPostCode() !=null:this.getPostCode().equals(other.getPostCode()))
                && (this.getPostName() ==null?this.getPostName() !=null:this.getPostName().equals(other.getPostName()))
                && (this.getPostSort() ==null?this.getPostSort() !=null:this.getPostSort().equals(other.getPostSort()))
                && (this.getStatus() ==null?this.getStatus() !=null:this.getStatus().equals(other.getStatus()))
                && (this.getCreateBy() ==null?this.getCreateBy() !=null:this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() ==null?this.getCreateTime() !=null:this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getRemark() ==null?this.getRemark() !=null:this.getRemark().equals(other.getRemark()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getPostCode() == null) ? 0 : getPostCode().hashCode());
        result = prime * result + ((getPostName() == null) ? 0 : getPostName().hashCode());
        result = prime * result + ((getPostSort() == null) ? 0 : getPostSort().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        return result;
    }
}
