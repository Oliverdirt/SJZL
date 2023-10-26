package com.ctsi.flow.core.service;

import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;

import java.util.Set;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-10-24 14:33
 * @description ：
 */
public interface CandidateUsersService {


    /**
     * create by: guoyanpei
     * description:  计算出任务节点的用户
     * params:
     * return: 用户集合
     */
    Set<Long> calculateTaskCandidateUsers(String processId, CscpFlowTaskAssignRule rule);
}
