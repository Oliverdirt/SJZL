package com.ctsi.ssdc.gen.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author ctsi-biyi-generator
 */
@ApiModel(description = "CscpHxFormTable")
public class CscpHxFormTable implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.table_id
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    @AutoId(primaryKey = "table_id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long tableId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String tableName;


    /** 主键信息 */
    private CscpHxFormColumn pkColumn;

    /** 子表信息 */
    private CscpHxFormTable subTable;

    /** 表列信息 */
    private List<CscpHxFormColumn> columns;

    /** java类名称 **/
    private String className;

    /** 作者 **/
    private String functionAuthor;

    private CscpHxFormSuite cscpHxFormSuite;

    private List<CscpHxFormTable> cscpHxFormTables;

    Integer isFeildValid;

    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long mainTableId;

    public Long getMainTableId() {
        return mainTableId;
    }

    public void setMainTableId(Long mainTableId) {
        this.mainTableId = mainTableId;
    }

    public List<CscpHxFormTable> getCscpHxFormTables() {
        return cscpHxFormTables;
    }

    public void setCscpHxFormTables(List<CscpHxFormTable> cscpHxFormTables) {
        this.cscpHxFormTables = cscpHxFormTables;
    }

    public Integer getIsFeildValid() {
        return isFeildValid;
    }

    public void setIsFeildValid(Integer isFeildValid) {
        this.isFeildValid = isFeildValid;
    }

    public CscpHxFormSuite getCscpHxFormSuite() {
        return cscpHxFormSuite;
    }

    public void setCscpHxFormSuite(CscpHxFormSuite cscpHxFormSuite) {
        this.cscpHxFormSuite = cscpHxFormSuite;
    }

    public String getFunctionAuthor() {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor) {
        this.functionAuthor = functionAuthor;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public CscpHxFormColumn getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(CscpHxFormColumn pkColumn) {
        this.pkColumn = pkColumn;
    }

    public CscpHxFormTable getSubTable() {
        return subTable;
    }

    public void setSubTable(CscpHxFormTable subTable) {
        this.subTable = subTable;
    }

    public List<CscpHxFormColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<CscpHxFormColumn> columns) {
        this.columns = columns;
    }

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.table_content
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String tableContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.db_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Integer dbType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.persistence_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Integer persistenceType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.is_db_synch
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Integer isDbSynch;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.form_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Integer formType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.sub_table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String subTableName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.sub_table_fk_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String subTableFkName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.package_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String packageName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.gen_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Integer genType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.gen_path
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String genPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.options
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String options;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.remark
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_hx_form_table.form_suite
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    private Long formSuite;

    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.table_id
     *
     * @return the value of cscp_hx_form_table.table_id
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Long getTableId() {
        return tableId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.table_id
     *
     * @param tableId the value for cscp_hx_form_table.table_id
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.table_name
     *
     * @return the value of cscp_hx_form_table.table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.table_name
     *
     * @param tableName the value for cscp_hx_form_table.table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.table_content
     *
     * @return the value of cscp_hx_form_table.table_content
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getTableContent() {
        return tableContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.table_content
     *
     * @param tableContent the value for cscp_hx_form_table.table_content
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setTableContent(String tableContent) {
        this.tableContent = tableContent == null ? null : tableContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.db_type
     *
     * @return the value of cscp_hx_form_table.db_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Integer getDbType() {
        return dbType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.db_type
     *
     * @param dbType the value for cscp_hx_form_table.db_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setDbType(Integer dbType) {
        this.dbType = dbType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.persistence_type
     *
     * @return the value of cscp_hx_form_table.persistence_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Integer getPersistenceType() {
        return persistenceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.persistence_type
     *
     * @param persistenceType the value for cscp_hx_form_table.persistence_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setPersistenceType(Integer persistenceType) {
        this.persistenceType = persistenceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.is_db_synch
     *
     * @return the value of cscp_hx_form_table.is_db_synch
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Integer getIsDbSynch() {
        return isDbSynch;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.is_db_synch
     *
     * @param isDbSynch the value for cscp_hx_form_table.is_db_synch
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setIsDbSynch(Integer isDbSynch) {
        this.isDbSynch = isDbSynch;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.form_type
     *
     * @return the value of cscp_hx_form_table.form_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Integer getFormType() {
        return formType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.form_type
     *
     * @param formType the value for cscp_hx_form_table.form_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setFormType(Integer formType) {
        this.formType = formType ;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.sub_table_name
     *
     * @return the value of cscp_hx_form_table.sub_table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getSubTableName() {
        return subTableName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.sub_table_name
     *
     * @param subTableName the value for cscp_hx_form_table.sub_table_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setSubTableName(String subTableName) {
        this.subTableName = subTableName == null ? null : subTableName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.sub_table_fk_name
     *
     * @return the value of cscp_hx_form_table.sub_table_fk_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getSubTableFkName() {
        return subTableFkName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.sub_table_fk_name
     *
     * @param subTableFkName the value for cscp_hx_form_table.sub_table_fk_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setSubTableFkName(String subTableFkName) {
        this.subTableFkName = subTableFkName == null ? null : subTableFkName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.package_name
     *
     * @return the value of cscp_hx_form_table.package_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.package_name
     *
     * @param packageName the value for cscp_hx_form_table.package_name
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.gen_type
     *
     * @return the value of cscp_hx_form_table.gen_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Integer getGenType() {
        return genType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.gen_type
     *
     * @param genType the value for cscp_hx_form_table.gen_type
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setGenType(Integer genType) {
        this.genType = genType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.gen_path
     *
     * @return the value of cscp_hx_form_table.gen_path
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getGenPath() {
        return genPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.gen_path
     *
     * @param genPath the value for cscp_hx_form_table.gen_path
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setGenPath(String genPath) {
        this.genPath = genPath == null ? null : genPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.options
     *
     * @return the value of cscp_hx_form_table.options
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getOptions() {
        return options;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.options
     *
     * @param options the value for cscp_hx_form_table.options
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.remark
     *
     * @return the value of cscp_hx_form_table.remark
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.remark
     *
     * @param remark the value for cscp_hx_form_table.remark
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_hx_form_table.form_suite
     *
     * @return the value of cscp_hx_form_table.form_suite
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public Long getFormSuite() {
        return formSuite;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_hx_form_table.form_suite
     *
     * @param formSuite the value for cscp_hx_form_table.form_suite
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    public void setFormSuite(Long formSuite) {
        this.formSuite = formSuite;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_hx_form_table
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CscpHxFormTable other = (CscpHxFormTable) that;
        return (this.getTableId() == null ? other.getTableId() == null : this.getTableId().equals(other.getTableId()))
                && (this.getTableName() == null ? other.getTableName() == null : this.getTableName().equals(other.getTableName()))
                && (this.getTableContent() == null ? other.getTableContent() == null : this.getTableContent().equals(other.getTableContent()))
                && (this.getDbType() == null ? other.getDbType() == null : this.getDbType().equals(other.getDbType()))
                && (this.getPersistenceType() == null ? other.getPersistenceType() == null : this.getPersistenceType().equals(other.getPersistenceType()))
                && (this.getIsDbSynch() == null ? other.getIsDbSynch() == null : this.getIsDbSynch().equals(other.getIsDbSynch()))
                && (this.getFormType() == null ? other.getFormType() == null : this.getFormType().equals(other.getFormType()))
                && (this.getSubTableName() == null ? other.getSubTableName() == null : this.getSubTableName().equals(other.getSubTableName()))
                && (this.getSubTableFkName() == null ? other.getSubTableFkName() == null : this.getSubTableFkName().equals(other.getSubTableFkName()))
                && (this.getPackageName() == null ? other.getPackageName() == null : this.getPackageName().equals(other.getPackageName()))
                && (this.getGenType() == null ? other.getGenType() == null : this.getGenType().equals(other.getGenType()))
                && (this.getGenPath() == null ? other.getGenPath() == null : this.getGenPath().equals(other.getGenPath()))
                && (this.getOptions() == null ? other.getOptions() == null : this.getOptions().equals(other.getOptions()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getFormSuite() == null ? other.getFormSuite() == null : this.getFormSuite().equals(other.getFormSuite()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_hx_form_table
     *
     * @mbg.generated Thu Feb 17 14:10:22 CST 2022
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getTableContent() == null) ? 0 : getTableContent().hashCode());
        result = prime * result + ((getDbType() == null) ? 0 : getDbType().hashCode());
        result = prime * result + ((getPersistenceType() == null) ? 0 : getPersistenceType().hashCode());
        result = prime * result + ((getIsDbSynch() == null) ? 0 : getIsDbSynch().hashCode());
        result = prime * result + ((getFormType() == null) ? 0 : getFormType().hashCode());
        result = prime * result + ((getSubTableName() == null) ? 0 : getSubTableName().hashCode());
        result = prime * result + ((getSubTableFkName() == null) ? 0 : getSubTableFkName().hashCode());
        result = prime * result + ((getPackageName() == null) ? 0 : getPackageName().hashCode());
        result = prime * result + ((getGenType() == null) ? 0 : getGenType().hashCode());
        result = prime * result + ((getGenPath() == null) ? 0 : getGenPath().hashCode());
        result = prime * result + ((getOptions() == null) ? 0 : getOptions().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getFormSuite() == null) ? 0 : getFormSuite().hashCode());
        return result;
    }
}