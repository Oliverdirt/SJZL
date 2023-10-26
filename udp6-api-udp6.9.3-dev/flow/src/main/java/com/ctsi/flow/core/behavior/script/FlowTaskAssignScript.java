package com.ctsi.flow.core.behavior.script;

import com.ctsi.flow.enums.FlowTaskRuleScriptEnum;

import java.util.Set;

/**
 * 任务分配的自定义 Script 脚本
 * 使用场景：
 * 1. 设置审批人为发起人
 *
 * @author sunsheng
 */
public interface FlowTaskAssignScript {

    /**
     * 基于流程任务，获得任务的候选用户们
     *
     * @param processId
     * @return 候选人用户的编号数组
     */
    Set<Long> calculateTaskCandidateUsers(String processId);

    /**
     * 获得枚举值
     *
     * @return 枚举值
     */
    FlowTaskRuleScriptEnum getEnum();

}
