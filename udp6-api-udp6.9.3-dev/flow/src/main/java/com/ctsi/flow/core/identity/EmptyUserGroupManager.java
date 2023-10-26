package com.ctsi.flow.core.identity;

import org.activiti.api.runtime.shared.identity.UserGroupManager;

import java.util.Collections;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-25 15:51
 * @description ：
 */

public class EmptyUserGroupManager implements UserGroupManager {

    @Override
    public List<String> getUserGroups(String s) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getUserRoles(String s) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getGroups() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getUsers() {
        return Collections.emptyList();
    }

}

