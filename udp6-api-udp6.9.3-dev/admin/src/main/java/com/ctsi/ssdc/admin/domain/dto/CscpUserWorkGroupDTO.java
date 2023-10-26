package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author MyBatis Generator
*/
public class CscpUserWorkGroupDTO implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_work_group.id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_work_group.user_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_user_work_group.group_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long groupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_user_work_group
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_work_group.id
     *
     * @return the value of cscp_user_work_group.id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_work_group.id
     *
     * @param id the value for cscp_user_work_group.id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_work_group.user_id
     *
     * @return the value of cscp_user_work_group.user_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_work_group.user_id
     *
     * @param userId the value for cscp_user_work_group.user_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cscp_user_work_group.group_id
     *
     * @return the value of cscp_user_work_group.group_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cscp_user_work_group.group_id
     *
     * @param groupId the value for cscp_user_work_group.group_id
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_work_group
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
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
        CscpUserWorkGroupDTO other = (CscpUserWorkGroupDTO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_work_group
     *
     * @mbg.generated Sat Apr 28 14:39:47 CST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        return result;
    }
}