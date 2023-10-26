package com.ctsi.ssdc.customize.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *  CscpCustomizeVpage 实体类
 *
 * @author hx
 * @date 2022-09-26 16:45:11
 *
 */

@ApiModel(description = "CscpCustomizeVpage")
public class CscpCustomizeVpage  implements Serializable{
    /**
     * PageId
     *
     */
    @AutoId(primaryKey = "page_id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long pageId;
    /**
     * 所属模块id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)




    private Long moduleId;
    /**
     * 页面名称
     *
     */

    private String pageName;
    /**
     * 页面类型 0 表单 1 列表
     *
     */

    private String pageType;
    /**
     * 页面数据源
     *
     */

    private String pageDatabase;
    /**
     * 页面数据表
     *
     */

    private String pageTable;
    /**
     * 页面json
     *
     */

    private String pageJson;
    /**
     * 表单选项
     *
     */

    private String pageOption;
    /**
     * 创建者ID
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long createdBy;
    /**
     * 创建时间
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")

    private ZonedDateTime createdTime;
    /**
     * 更新者ID
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
     * 租户id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long tenantId;
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
     * 页面表类型：0代表单表 1代表主表 2代表子表
     *
     */

    private String tableType;
    /**
     * 关联主表的page_id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long mainPageId;
    /**
     * 排序
     *
     */

    private Integer sortNum;
    /**
     * 关联主表的外键
     *
     */

    private String subPageFk;
    /**
     * 数据库表名
     *
     */

    private String tableDesc;
    /**
     * 是否为主页面 0 主页面 1 子页面
     *
     */

    private String isMainPage;
    /**
     * 模块id
     *
     */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long modelId;
    /**
     * 数据模型名称
     *
     */

    private String modelName;
    /**
     * 模块名称
     *
     */

    private String moduleName;



    public void setPageId(Long pageId){
        this.pageId = pageId;
    }
    public Long getPageId(){
        return pageId;
    }
    public void setModuleId(Long moduleId){
        this.moduleId = moduleId;
    }
    public Long getModuleId(){
        return moduleId;
    }
    public void setPageName(String pageName){
        this.pageName = pageName;
    }
    public String getPageName(){
        return pageName;
    }
    public void setPageType(String pageType){
        this.pageType = pageType;
    }
    public String getPageType(){
        return pageType;
    }
    public void setPageDatabase(String pageDatabase){
        this.pageDatabase = pageDatabase;
    }
    public String getPageDatabase(){
        return pageDatabase;
    }
    public void setPageTable(String pageTable){
        this.pageTable = pageTable;
    }
    public String getPageTable(){
        return pageTable;
    }
    public void setPageJson(String pageJson){
        this.pageJson = pageJson;
    }
    public String getPageJson(){
        return pageJson;
    }
    public void setPageOption(String pageOption){
        this.pageOption = pageOption;
    }
    public String getPageOption(){
        return pageOption;
    }
    public void setCreatedBy(Long createdBy){
        this.createdBy = createdBy;
    }
    public Long getCreatedBy(){
        return createdBy;
    }
    public void setCreatedTime(ZonedDateTime createdTime){
        this.createdTime = createdTime;
    }
    public ZonedDateTime getCreatedTime(){
        return createdTime;
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
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    public Long getTenantId(){
        return tenantId;
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
    public void setTableType(String tableType){
        this.tableType = tableType;
    }
    public String getTableType(){
        return tableType;
    }
    public void setMainPageId(Long mainPageId){
        this.mainPageId = mainPageId;
    }
    public Long getMainPageId(){
        return mainPageId;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }
    public Integer getSortNum(){
        return sortNum;
    }
    public void setSubPageFk(String subPageFk){
        this.subPageFk = subPageFk;
    }
    public String getSubPageFk(){
        return subPageFk;
    }
    public void setTableDesc(String tableDesc){
        this.tableDesc = tableDesc;
    }
    public String getTableDesc(){
        return tableDesc;
    }
    public void setIsMainPage(String isMainPage){
        this.isMainPage = isMainPage;
    }
    public String getIsMainPage(){
        return isMainPage;
    }
    public void setModelId(Long modelId){
        this.modelId = modelId;
    }
    public Long getModelId(){
        return modelId;
    }
    public void setModelName(String modelName){
        this.modelName = modelName;
    }
    public String getModelName(){
        return modelName;
    }
    public void setModuleName(String moduleName){
        this.moduleName = moduleName;
    }
    public String getModuleName(){
        return moduleName;
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
        CscpCustomizeVpage other=(CscpCustomizeVpage)that;
        return (this.getPageId() ==null?this.getPageId() !=null:this.getPageId().equals(other.getPageId()))
                && (this.getModuleId() ==null?this.getModuleId() !=null:this.getModuleId().equals(other.getModuleId()))
                && (this.getPageName() ==null?this.getPageName() !=null:this.getPageName().equals(other.getPageName()))
                && (this.getPageType() ==null?this.getPageType() !=null:this.getPageType().equals(other.getPageType()))
                && (this.getPageDatabase() ==null?this.getPageDatabase() !=null:this.getPageDatabase().equals(other.getPageDatabase()))
                && (this.getPageTable() ==null?this.getPageTable() !=null:this.getPageTable().equals(other.getPageTable()))
                && (this.getPageJson() ==null?this.getPageJson() !=null:this.getPageJson().equals(other.getPageJson()))
                && (this.getPageOption() ==null?this.getPageOption() !=null:this.getPageOption().equals(other.getPageOption()))
                && (this.getCreatedBy() ==null?this.getCreatedBy() !=null:this.getCreatedBy().equals(other.getCreatedBy()))
                && (this.getCreatedTime() ==null?this.getCreatedTime() !=null:this.getCreatedTime().equals(other.getCreatedTime()))
                && (this.getUpdateBy() ==null?this.getUpdateBy() !=null:this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() ==null?this.getUpdateTime() !=null:this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getTenantId() ==null?this.getTenantId() !=null:this.getTenantId().equals(other.getTenantId()))
                && (this.getDelFlag() ==null?this.getDelFlag() !=null:this.getDelFlag().equals(other.getDelFlag()))
                && (this.getStatus() ==null?this.getStatus() !=null:this.getStatus().equals(other.getStatus()))
                && (this.getTableType() ==null?this.getTableType() !=null:this.getTableType().equals(other.getTableType()))
                && (this.getMainPageId() ==null?this.getMainPageId() !=null:this.getMainPageId().equals(other.getMainPageId()))
                && (this.getSortNum() ==null?this.getSortNum() !=null:this.getSortNum().equals(other.getSortNum()))
                && (this.getSubPageFk() ==null?this.getSubPageFk() !=null:this.getSubPageFk().equals(other.getSubPageFk()))
                && (this.getTableDesc() ==null?this.getTableDesc() !=null:this.getTableDesc().equals(other.getTableDesc()))
                && (this.getIsMainPage() ==null?this.getIsMainPage() !=null:this.getIsMainPage().equals(other.getIsMainPage()))
                && (this.getModelId() ==null?this.getModelId() !=null:this.getModelId().equals(other.getModelId()))
                && (this.getModelName() ==null?this.getModelName() !=null:this.getModelName().equals(other.getModelName()))
                && (this.getModuleName() ==null?this.getModuleName() !=null:this.getModuleName().equals(other.getModuleName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPageId() == null) ? 0 : getPageId().hashCode());
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getPageName() == null) ? 0 : getPageName().hashCode());
        result = prime * result + ((getPageType() == null) ? 0 : getPageType().hashCode());
        result = prime * result + ((getPageDatabase() == null) ? 0 : getPageDatabase().hashCode());
        result = prime * result + ((getPageTable() == null) ? 0 : getPageTable().hashCode());
        result = prime * result + ((getPageJson() == null) ? 0 : getPageJson().hashCode());
        result = prime * result + ((getPageOption() == null) ? 0 : getPageOption().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTableType() == null) ? 0 : getTableType().hashCode());
        result = prime * result + ((getMainPageId() == null) ? 0 : getMainPageId().hashCode());
        result = prime * result + ((getSortNum() == null) ? 0 : getSortNum().hashCode());
        result = prime * result + ((getSubPageFk() == null) ? 0 : getSubPageFk().hashCode());
        result = prime * result + ((getTableDesc() == null) ? 0 : getTableDesc().hashCode());
        result = prime * result + ((getIsMainPage() == null) ? 0 : getIsMainPage().hashCode());
        result = prime * result + ((getModelId() == null) ? 0 : getModelId().hashCode());
        result = prime * result + ((getModelName() == null) ? 0 : getModelName().hashCode());
        result = prime * result + ((getModuleName() == null) ? 0 : getModuleName().hashCode());
        return result;
    }
}
