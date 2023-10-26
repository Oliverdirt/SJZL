package com.ctsi.ssdc.gen.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
/**
 *  CscpCustomizeVform 实体类
 *
 * @author hx
 * @date 2022-05-23 09:59:33
 *
 */

@ApiModel(description = "CscpCustomizeVform")
public class CscpCustomizeVform  implements Serializable{
    /**
    * 表单id
    *
    */
    @AutoId(primaryKey = "form_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long formId;
    /**
    * 表单名称
    *
    */

    private String formName;
    /**
    * 表单数据库
    *
    */

    private String formDatabase;
    /**
    * 表单数据表
    *
    */

    private String formTable;
    /**
    * 表单json
    *
    */

    private String formJson;
    /**
    * 表单选项
    *
    */

    private String formOption;
    /**
    * 租户id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tenantId;
    /**
    * 创建时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createdTime;
    /**
    * 创建人id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long createdBy;
    /**
    * 更新时间
    *
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime updateTime;
    /**
    * 更新人id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long updateBy;
    /**
    * 是否删除
    *
    */

    private String delFlag;
    /**
    * 状态值
    *
    */

    private String status;

    /**
     * 表类型：0代表单表 1代表主表
     *
     */

    private String formType;
    /**
     * 关联主表的form_id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long mainFormId;
    /**
     * 排序
     *
     */

    private Integer sortNum;
    /**
     * 关联主表的外键
     *
     */
    private String subFormFk;
    /**
     * 数据库表名
     *
     */

    private String tableDesc;

    private List<CscpCustomizeVfield> vfieldList;

    private int startIdex;


    private int pageSize;

    /**
     * 排序字段
     */
    private String sort;

    private String formUrl;

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public List<CscpCustomizeVfield> getVfieldList() {
        return vfieldList;
    }

    public void setVfieldList(List<CscpCustomizeVfield> vfieldList) {
        this.vfieldList = vfieldList;
    }

    public void setFormId(Long formId){
        this.formId = formId;
    }
    public Long getFormId(){
        return formId;
    }
    public void setFormName(String formName){
        this.formName = formName;
    }
    public String getFormName(){
        return formName;
    }
    public void setFormDatabase(String formDatabase){
        this.formDatabase = formDatabase;
    }
    public String getFormDatabase(){
        return formDatabase;
    }
    public void setFormTable(String formTable){
        this.formTable = formTable;
    }
    public String getFormTable(){
        return formTable;
    }
    public void setFormJson(String formJson){
        this.formJson = formJson;
    }
    public String getFormJson(){
        return formJson;
    }
    public void setFormOption(String formOption){
        this.formOption = formOption;
    }
    public String getFormOption(){
        return formOption;
    }
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    public Long getTenantId(){
        return tenantId;
    }
    public void setCreatedTime(ZonedDateTime createdTime){
        this.createdTime = createdTime;
    }
    public ZonedDateTime getCreatedTime(){
        return createdTime;
    }
    public void setCreatedBy(Long createdBy){
        this.createdBy = createdBy;
    }
    public Long getCreatedBy(){
        return createdBy;
    }
    public void setUpdateTime(ZonedDateTime updateTime){
        this.updateTime = updateTime;
    }
    public ZonedDateTime getUpdateTime(){
        return updateTime;
    }
    public void setUpdateBy(Long updateBy){
        this.updateBy = updateBy;
    }
    public Long getUpdateBy(){
        return updateBy;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }
    public String getDelFlag(){
        return delFlag;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
    public void setFormType(String formType){
        this.formType = formType;
    }
    public String getFormType(){
        return formType;
    }
    public void setMainFormId(Long mainFormId){
        this.mainFormId = mainFormId;
    }
    public Long getMainFormId(){
        return mainFormId;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getSubFormFk() {
        return subFormFk;
    }

    public void setSubFormFk(String subFormFk) {
        this.subFormFk = subFormFk;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getStartIdex() {
        return startIdex;
    }

    public void setStartIdex(int startIdex) {
        this.startIdex = startIdex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
        CscpCustomizeVform other=(CscpCustomizeVform)that;
        return (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getFormName() ==null?this.getFormName() !=null:this.getFormName().equals(other.getFormName()))
                && (this.getFormDatabase() ==null?this.getFormDatabase() !=null:this.getFormDatabase().equals(other.getFormDatabase()))
                && (this.getFormTable() ==null?this.getFormTable() !=null:this.getFormTable().equals(other.getFormTable()))
                && (this.getFormJson() ==null?this.getFormJson() !=null:this.getFormJson().equals(other.getFormJson()))
                && (this.getFormOption() ==null?this.getFormOption() !=null:this.getFormOption().equals(other.getFormOption()))
                && (this.getCreatedBy() ==null?this.getCreatedBy() !=null:this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getCreatedTime() ==null?this.getCreatedTime() !=null:this.getCreatedTime().equals(other.getCreatedTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getDelFlag() ==null?this.getDelFlag() !=null:this.getDelFlag().equals(other.getDelFlag()))
                && (this.getStatus() ==null?this.getStatus() !=null:this.getStatus().equals(other.getStatus()))
                && (this.getFormType() ==null?this.getFormType() !=null:this.getFormType().equals(other.getFormType()))
                && (this.getMainFormId() ==null?this.getMainFormId() !=null:this.getMainFormId().equals(other.getMainFormId()))
                && (this.getSortNum() ==null?this.getSortNum() !=null:this.getSortNum().equals(other.getSortNum()))
                && (this.getSubFormFk() ==null?this.getSubFormFk() !=null:this.getSubFormFk().equals(other.getSubFormFk()))
                && (this.getFormUrl() ==null?this.getFormUrl() !=null:this.getFormUrl().equals(other.getFormUrl()))
                && (this.getTableDesc() ==null?this.getTableDesc() !=null:this.getTableDesc().equals(other.getTableDesc()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getFormName() == null) ? 0 : getFormName().hashCode());
        result = prime * result + ((getFormDatabase() == null) ? 0 : getFormDatabase().hashCode());
        result = prime * result + ((getFormTable() == null) ? 0 : getFormTable().hashCode());
        result = prime * result + ((getFormJson() == null) ? 0 : getFormJson().hashCode());
        result = prime * result + ((getFormOption() == null) ? 0 : getFormOption().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getFormType() == null) ? 0 : getFormType().hashCode());
        result = prime * result + ((getMainFormId() == null) ? 0 : getMainFormId().hashCode());
        result = prime * result + ((getSortNum() == null) ? 0 : getSortNum().hashCode());
        result = prime * result + ((getSubFormFk() == null) ? 0 : getSubFormFk().hashCode());
        result = prime * result + ((getFormUrl() == null) ? 0 : getFormUrl().hashCode());
        result = prime * result + ((getTableDesc() == null) ? 0 : getTableDesc().hashCode());
        return result;
    }
}
