package com.ctsi.ssdc.gen.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.ZonedDateTime;
/**
 *  CscpCustomizeVfield 实体类
 *
 * @author hx
 * @date 2022-06-01 16:19:31
 *
 */

@ApiModel(description = "CscpCustomizeVfield")
public class CscpCustomizeVfield  implements Serializable{
    /**
     * 字段id
     *
     */
    @AutoId(primaryKey = "field_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long fieldId;
    /**
     * 表单id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long formId;
    /**
     * 表单名称
     *
     */

    private String fieldName;
    /**
     * 表单备注
     *
     */

    private String fieldComment;
    /**
     * 是否查询展示
     *
     */

    private String fieldQuery;
    /**
     * 查询类型
     *
     */

    private String queryType;
    /**
     * 控件类型
     *
     */

    private String controlType;
    /**
     * 是否列表展示
     *
     */

    private String fieldList;
    /**
     * 字段宽度
     *
     */

    private Double fieldWidth;
    /**
     * 字段排序
     *
     */

    private Integer fieldOrder;
    /**
     * 字段字典
     *
     */

    private String fieldDic;
    /**
     * 选项
     *
     */

    private String fieldOptions;
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
     * 字段类型
     *
     */

    private Integer fieldType;

    /**
     * 组件类型
     */
    private String componentType;


    /**
     * 是否表单展示 1 是 0 否
     *
     */

    private String isForm;





    public void setFieldId(Long fieldId){
        this.fieldId = fieldId;
    }
    public Long getFieldId(){
        return fieldId;
    }
    public void setFormId(Long formId){
        this.formId = formId;
    }
    public Long getFormId(){
        return formId;
    }
    public void setFieldName(String fieldName){
        this.fieldName = fieldName;
    }
    public String getFieldName(){
        return fieldName;
    }
    public void setFieldComment(String fieldComment){
        this.fieldComment = fieldComment;
    }
    public String getFieldComment(){
        return fieldComment;
    }
    public void setFieldQuery(String fieldQuery){
        this.fieldQuery = fieldQuery;
    }
    public String getFieldQuery(){
        return fieldQuery;
    }
    public void setQueryType(String queryType){
        this.queryType = queryType;
    }
    public String getQueryType(){
        return queryType;
    }
    public void setControlType(String controlType){
        this.controlType = controlType;
    }
    public String getControlType(){
        return controlType;
    }
    public void setFieldList(String fieldList){
        this.fieldList = fieldList;
    }
    public String getFieldList(){
        return fieldList;
    }
    public void setFieldWidth(Double fieldWidth){
        this.fieldWidth = fieldWidth;
    }
    public Double getFieldWidth(){
        return fieldWidth;
    }
    public void setFieldOrder(Integer fieldOrder){
        this.fieldOrder = fieldOrder;
    }
    public Integer getFieldOrder(){
        return fieldOrder;
    }
    public void setFieldDic(String fieldDic){
        this.fieldDic = fieldDic;
    }
    public String getFieldDic(){
        return fieldDic;
    }
    public void setFieldOptions(String fieldOptions){
        this.fieldOptions = fieldOptions;
    }
    public String getFieldOptions(){
        return fieldOptions;
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
    public void setFieldType(Integer fieldType){
        this.fieldType = fieldType;
    }
    public Integer getFieldType(){
        return fieldType;
    }


    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getIsForm() {
        return isForm;
    }

    public void setIsForm(String isForm) {
        this.isForm = isForm;
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
        CscpCustomizeVfield other=(CscpCustomizeVfield)that;
        return (this.getFieldId() ==null?this.getFieldId() !=null:this.getFieldId().equals(other.getFieldId()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getFieldName() ==null?this.getFieldName() !=null:this.getFieldName().equals(other.getFieldName()))
                && (this.getFieldComment() ==null?this.getFieldComment() !=null:this.getFieldComment().equals(other.getFieldComment()))
                && (this.getFieldQuery() ==null?this.getFieldQuery() !=null:this.getFieldQuery().equals(other.getFieldQuery()))
                && (this.getQueryType() ==null?this.getQueryType() !=null:this.getQueryType().equals(other.getQueryType()))
                && (this.getControlType() ==null?this.getControlType() !=null:this.getControlType().equals(other.getControlType()))
                && (this.getFieldList() ==null?this.getFieldList() !=null:this.getFieldList().equals(other.getFieldList()))
                && (this.getFieldWidth() ==null?this.getFieldWidth() !=null:this.getFieldWidth().equals(other.getFieldWidth()))
                && (this.getFieldOrder() ==null?this.getFieldOrder() !=null:this.getFieldOrder().equals(other.getFieldOrder()))
                && (this.getFieldDic() ==null?this.getFieldDic() !=null:this.getFieldDic().equals(other.getFieldDic()))
                && (this.getFieldOptions() ==null?this.getFieldOptions() !=null:this.getFieldOptions().equals(other.getFieldOptions()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getCreatedTime() ==null?this.getCreatedTime() !=null:this.getCreatedTime().equals(other.getCreatedTime()))
                && (this.getCreatedBy() ==null?this.getCreatedBy() !=null:this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getDelFlag() ==null?this.getDelFlag() !=null:this.getDelFlag().equals(other.getDelFlag()))
                && (this.getStatus() ==null?this.getStatus() !=null:this.getStatus().equals(other.getStatus()))
                && (this.getFieldType() ==null?this.getFieldType() !=null:this.getFieldType().equals(other.getFieldType()))
                && (this.getComponentType() ==null?this.getComponentType() !=null:this.getComponentType().equals(other.getComponentType()))
                && (this.getIsForm() ==null?this.getIsForm() !=null:this.getIsForm().equals(other.getIsForm()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFieldId() == null) ? 0 : getFieldId().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + ((getFieldComment() == null) ? 0 : getFieldComment().hashCode());
        result = prime * result + ((getFieldQuery() == null) ? 0 : getFieldQuery().hashCode());
        result = prime * result + ((getQueryType() == null) ? 0 : getQueryType().hashCode());
        result = prime * result + ((getControlType() == null) ? 0 : getControlType().hashCode());
        result = prime * result + ((getFieldList() == null) ? 0 : getFieldList().hashCode());
        result = prime * result + ((getFieldWidth() == null) ? 0 : getFieldWidth().hashCode());
        result = prime * result + ((getFieldOrder() == null) ? 0 : getFieldOrder().hashCode());
        result = prime * result + ((getFieldDic() == null) ? 0 : getFieldDic().hashCode());
        result = prime * result + ((getFieldOptions() == null) ? 0 : getFieldOptions().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getFieldType() == null) ? 0 : getFieldType().hashCode());
        result = prime * result + ((getComponentType() == null) ? 0 : getComponentType().hashCode());
        result = prime * result + ((getIsForm() == null) ? 0 : getIsForm().hashCode());
        return result;
    }
}
