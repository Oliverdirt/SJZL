package com.ctsi.flow.core.behavior.script.impl;


import com.ctsi.flow.core.behavior.script.FlowTaskAssignScript;
import com.ctsi.flow.enums.FlowTaskRuleScriptEnum;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 分配给发起人审批的 Script 实现类
 *
 * @author sunsheng
 */
@Component
public class FlowTaskAssignStartUserScript implements FlowTaskAssignScript {


    @Resource
    private RuntimeService runtimeService;

    @Override
    public Set<Long> calculateTaskCandidateUsers(String processId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        Long userId = Long.parseLong(processInstance.getStartUserId());
        return new HashSet<>(Collections.singletonList(userId));
    }

    @Override
    public FlowTaskRuleScriptEnum getEnum() {
        return FlowTaskRuleScriptEnum.START_USER;
    }

}
