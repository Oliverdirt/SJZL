package com.ctsi.ssdc.gen.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author ctsi-biyi-generator
*/
public class CscpHxFormSuite implements Serializable {
    /**
     *主键
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.id
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /**
     *模板名称
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.suite_name
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    private String suiteName;

    /**
     *模板编码（英文）
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.suite_code
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    private String suiteCode;

    /**
     *模板版本
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.suite_version
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    private String suiteVersion;

    /**
     *模板类型
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.suite_type
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    private Integer suiteType;

    /**
     *保存路径
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_template_suite.suite_path
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    private String suitePath;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.id
     *
     * @return the value of code_template_suite.id
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.id
     *
     * @param id the value for code_template_suite.id
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.suite_name
     *
     * @return the value of code_template_suite.suite_name
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public String getSuiteName() {
        return suiteName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.suite_name
     *
     * @param suiteName the value for code_template_suite.suite_name
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName == null ? null : suiteName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.suite_code
     *
     * @return the value of code_template_suite.suite_code
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public String getSuiteCode() {
        return suiteCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.suite_code
     *
     * @param suiteCode the value for code_template_suite.suite_code
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setSuiteCode(String suiteCode) {
        this.suiteCode = suiteCode == null ? null : suiteCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.suite_version
     *
     * @return the value of code_template_suite.suite_version
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public String getSuiteVersion() {
        return suiteVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.suite_version
     *
     * @param suiteVersion the value for code_template_suite.suite_version
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setSuiteVersion(String suiteVersion) {
        this.suiteVersion = suiteVersion == null ? null : suiteVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.suite_type
     *
     * @return the value of code_template_suite.suite_type
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public Integer getSuiteType() {
        return suiteType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.suite_type
     *
     * @param suiteType the value for code_template_suite.suite_type
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setSuiteType(Integer suiteType) {
        this.suiteType = suiteType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_template_suite.suite_path
     *
     * @return the value of code_template_suite.suite_path
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public String getSuitePath() {
        return suitePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_template_suite.suite_path
     *
     * @param suitePath the value for code_template_suite.suite_path
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    public void setSuitePath(String suitePath) {
        this.suitePath = suitePath == null ? null : suitePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table code_template_suite
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
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
        CscpHxFormSuite other = (CscpHxFormSuite) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSuiteName() == null ? other.getSuiteName() == null : this.getSuiteName().equals(other.getSuiteName()))
            && (this.getSuiteCode() == null ? other.getSuiteCode() == null : this.getSuiteCode().equals(other.getSuiteCode()))
            && (this.getSuiteVersion() == null ? other.getSuiteVersion() == null : this.getSuiteVersion().equals(other.getSuiteVersion()))
            && (this.getSuiteType() == null ? other.getSuiteType() == null : this.getSuiteType().equals(other.getSuiteType()))
            && (this.getSuitePath() == null ? other.getSuitePath() == null : this.getSuitePath().equals(other.getSuitePath()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table code_template_suite
     *
     * @mbg.generated Fri Jun 14 16:57:33 CST 2019
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSuiteName() == null) ? 0 : getSuiteName().hashCode());
        result = prime * result + ((getSuiteCode() == null) ? 0 : getSuiteCode().hashCode());
        result = prime * result + ((getSuiteVersion() == null) ? 0 : getSuiteVersion().hashCode());
        result = prime * result + ((getSuiteType() == null) ? 0 : getSuiteType().hashCode());
        result = prime * result + ((getSuitePath() == null) ? 0 : getSuitePath().hashCode());
        return result;
    }
}