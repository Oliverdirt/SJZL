package com.ctsi.flow.core.behavior;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.core.service.CandidateUsersService;
import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;
import com.ctsi.flow.server.rule.FlowRuleService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-25 15:57
 * @description ：
 */

@Slf4j
public class FlowUserTaskActivityBehavior extends UserTaskActivityBehavior {

    public FlowUserTaskActivityBehavior(UserTask userTask) {
        super(userTask);
    }

    @Setter
    private FlowRuleService flowTaskRuleService;

    @Setter
    private CandidateUsersService candidateUsersService;


    @Override
    protected void handleAssignments(TaskEntityManager taskEntityManager,
                                     String assignee, String owner, List<String> candidateUsers, List<String> candidateGroups,
                                     TaskEntity task, ExpressionManager expressionManager, DelegateExecution execution) {

        if (StringUtils.isNotEmpty(assignee)) {
            Object assigneeExpressionValue = expressionManager.createExpression(assignee).getValue(execution);
            String assigneeValue = null;
            if (assigneeExpressionValue != null) {
                assigneeValue = assigneeExpressionValue.toString();
            }

            taskEntityManager.changeTaskAssigneeNoEvents(task, assigneeValue);
        } else {
            // 第一步，获得任务的规则
            CscpFlowTaskAssignRule rule = getTaskRule(task);
            // 第二步，获得任务的候选用户们
            Set<Long> candidateUserIds = candidateUsersService.calculateTaskCandidateUsers(task.getProcessInstanceId(), rule);
            // 第三步，设置处理人
            List<String> list = candidateUserIds.stream().map(e -> e.toString()).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(list)) {
                throw new ActivitiException(StrUtil.format("流程任务({}/{}/{}) 找不到合适的节点处理人",
                        task.getId(), task.getProcessDefinitionId(), task.getTaskDefinitionKey()));
            }
            // 若审批人为多个,则都设置为候选人，后续通过签收处理;
            // 若候选人仅为一人,则直接设置任务审批人为该人选，如果xml配置候选人，以xml配置为准
            if (list.size() > 1) {
                task.addCandidateUsers(list);
            } else {
                String assigneeUserId = list.get(0);
                taskEntityManager.changeTaskAssignee(task, String.valueOf(assigneeUserId));
            }
        }

        if (candidateGroups != null && !candidateGroups.isEmpty()) {
            for (String candidateGroup : candidateGroups) {
                Expression groupIdExpr = expressionManager.createExpression(candidateGroup);
                Object value = groupIdExpr.getValue(execution);
                if (value instanceof String) {
                    List<String> candidates = extractCandidates((String) value);
                    task.addCandidateGroups(candidates);
                } else if (value instanceof Collection) {
                    task.addCandidateGroups((Collection) value);
                } else {
                    throw new ActivitiException("解析流程图候选组配置表达式错误");
                }
            }
        }

        if (candidateUsers != null && !candidateUsers.isEmpty()) {
            for (String candidateUser : candidateUsers) {
                Expression userIdExpr = expressionManager.createExpression(candidateUser);
                Object value = userIdExpr.getValue(execution);
                if (value instanceof String) {
                    List<String> candidates = extractCandidates((String) value);
                    task.addCandidateUsers(candidates);
                } else if (value instanceof Collection) {
                    task.addCandidateUsers((Collection) value);
                } else {
                    throw new ActivitiException("解析流程图候选人配置表达式错误");
                }
            }

        }
    }


    private CscpFlowTaskAssignRule getTaskRule(TaskEntity task) {
        List<CscpFlowTaskAssignRule> taskRules = flowTaskRuleService.getTaskAssignRuleListByProcessDefinitionId(task.getProcessDefinitionId(),
                task.getTaskDefinitionKey());
        if (CollUtil.isEmpty(taskRules)) {
            throw new ActivitiException(StrUtil.format("流程任务({}/{}/{}) 找不到符合的任务规则",
                    task.getId(), task.getProcessDefinitionId(), task.getTaskDefinitionKey()));
        }
        if (taskRules.size() > 1) {
            throw new ActivitiException(StrUtil.format("流程任务({}/{}/{}) 找到过多任务规则({})",
                    task.getId(), task.getProcessDefinitionId(), task.getTaskDefinitionKey(), taskRules.size()));
        }
        return taskRules.get(0);
    }
}
