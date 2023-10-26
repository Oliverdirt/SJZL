package com.ctsi.ssdc.admin.domain;

import java.util.List;
import java.util.Objects;

/**
 * @Description: 工作流用户任务模块人员、候选人员、候选组信息等。
 * @Author: sunsheng
 **/
public class FlowUserTaskInfo {

    List<CscpUserDetail> users;

    List<CscpRoles> roles;


    public List<CscpUserDetail> getUsers() {
        return users;
    }

    public void setUsers(List<CscpUserDetail> users) {
        this.users = users;
    }

    public List<CscpRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<CscpRoles> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowUserTaskInfo that = (FlowUserTaskInfo) o;
        return Objects.equals(users, that.users) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, roles);
    }
}
