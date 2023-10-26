package com.ctsi.ssdc.admin.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author ctsi-biyi-generator
*/
public class CscpUserPasswordChangeLog implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_password_change_log.id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_password_change_log.user_id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_password_change_log.password
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_password_change_log.time
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    private ZonedDateTime time;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_password_change_log.id
     *
     * @return the value of cscp_user_password_change_log.id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_password_change_log.id
     *
     * @param id the value for cscp_user_password_change_log.id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_password_change_log.user_id
     *
     * @return the value of cscp_user_password_change_log.user_id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_password_change_log.user_id
     *
     * @param userId the value for cscp_user_password_change_log.user_id
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_password_change_log.password
     *
     * @return the value of cscp_user_password_change_log.password
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_password_change_log.password
     *
     * @param password the value for cscp_user_password_change_log.password
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_password_change_log.time
     *
     * @return the value of cscp_user_password_change_log.time
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public ZonedDateTime getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_password_change_log.time
     *
     * @param time the value for cscp_user_password_change_log.time
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
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
        CscpUserPasswordChangeLog other = (CscpUserPasswordChangeLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPassword() == null ?
                other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        return result;
    }
}