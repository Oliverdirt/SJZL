package com.ctsi.flow.multi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ctsi.flow.core.service.CandidateUsersService;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRuleExample;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.model.MultiInstanceVariables;
import com.ctsi.flow.multi.service.CscpFlowTaskMultiRuleService;
import com.ctsi.flow.multi.service.MultiInstanceService;
import com.ctsi.flow.multi.strategy.IMulti;
import com.ctsi.flow.multi.utils.FlowMultiUtils;
import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;
import com.ctsi.flow.server.rule.FlowRuleService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gyp
 */
@Service("multiInstanceService")
@Slf4j
public class MultiInstanceServiceImpl implements MultiInstanceService {

    private static final ThreadLocal<MultiInstanceDecisionResult> DECISION_HOLDER = new ThreadLocal<>();

    private final RuntimeService runtimeService;

    public MultiInstanceServiceImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Resource
    private CandidateUsersService candidateUsersService;

    @Resource
    private FlowRuleService flowTaskRuleService;

    @Resource
    private CscpFlowTaskMultiRuleService cscpFlowTaskMultiRuleService;


    @Override
    public void install(String parentExecutionId) {

    }

    @Override
    public void update(String parentExecutionId, String vote) {

        Object approve = runtimeService.getVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfApprove.name());
        Object reject = runtimeService.getVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfReject.name());
        if (approve == null || reject == null) {
            runtimeService.setVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfApprove.name(), 0);
            runtimeService.setVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfReject.name(), 0);
            log.debug("初始化多实例任务流程参数");
        }

        int v = Integer.parseInt(vote);
        if (v == 1) {
            String approveName = MultiInstanceVariables.nrOfApprove.name();
            runtimeService.setVariableLocal(parentExecutionId, approveName,
                    (int) runtimeService.getVariableLocal(parentExecutionId, approveName) + 1);
        } else if (v == 0) {
            String rejectName = MultiInstanceVariables.nrOfReject.name();
            runtimeService.setVariableLocal(parentExecutionId, rejectName,
                    (int) runtimeService.getVariableLocal(parentExecutionId, rejectName) + 1);
        }

    }

    @Override
    public void remove(String parentExecutionId) {

        runtimeService.removeVariable(parentExecutionId, MultiInstanceVariables.nrOfApprove.name());
        runtimeService.removeVariable(parentExecutionId, MultiInstanceVariables.nrOfReject.name());
    }

    @Override
    public MultiInstanceDecisionResult decision(CscpFlowTaskMultiRule cscpFlowTaskMultiRule, String executionId, String parentExecutionId, String processDefinitionId, String vote) {

        // 自定义数据存储在上级的execution中
        int nrOfApprove = (int) runtimeService.getVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfApprove.name());
        int nrOfReject = (int) runtimeService.getVariableLocal(parentExecutionId, MultiInstanceVariables.nrOfReject.name());

        int nrOfInstances;
        int nrOfCompletedInstances;
        int nrOfActiveInstances;

        // loopCounter在自己的execution中
        int loopCounter = (int) runtimeService.getVariableLocal(executionId, MultiInstanceVariables.loopCounter.name());

        // 并行
        nrOfInstances = (int) runtimeService.getVariable(executionId, MultiInstanceVariables.nrOfInstances.name());
        nrOfCompletedInstances = (int) runtimeService.getVariable(executionId, MultiInstanceVariables.nrOfCompletedInstances.name());
        nrOfActiveInstances = (int) runtimeService.getVariable(executionId, MultiInstanceVariables.nrOfActiveInstances.name());

        MultiInstanceDecisionParam param = new MultiInstanceDecisionParam();
        param.setCscpFlowTaskMultiRule(cscpFlowTaskMultiRule);
        param.setLoopCounter(loopCounter);
        param.setNrOfActiveInstances(nrOfActiveInstances);
        param.setNrOfApprove(nrOfApprove);
        param.setNrOfCompletedInstances(nrOfCompletedInstances);
        param.setNrOfInstances(nrOfInstances);
        param.setNrOfReject(nrOfReject);
        param.setCurrentVote(Integer.parseInt(vote));

        MultiInstanceDecisionResult decision = this.validate(param);
        // 存储决策结果，供hasComplete使用
        DECISION_HOLDER.set(decision);
        return decision;

    }

    private MultiInstanceDecisionResult validate(MultiInstanceDecisionParam param) {

        Class<?> cls;
        try {
            cls = Class.forName(param.getCscpFlowTaskMultiRule().getTaskMulti());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("多实例策略未能成功实例化");
        }
        String simpleName = cls.getSimpleName();
        simpleName = FlowMultiUtils.toLowerCaseInitial(simpleName, true);
        IMulti multiStrategy;
        try {
            multiStrategy = (IMulti) FlowMultiUtils.springContext().getBean(simpleName);
        } catch (Exception e) {
            multiStrategy = FlowMultiUtils.springContext().getBean(cls);
        }
        return multiStrategy.validate(param);
    }


    @Override
    public List<String> pourAssigneeCollection(DelegateExecution execution) {

        String currentActivityId = execution.getCurrentActivityId();
        // 根据注入任务的配置ID获取当前节点配置的处理人
        CscpFlowTaskAssignRule task = flowTaskRuleService.getTaskAssignRuleListByTaskDefinitionKey(currentActivityId);
        Set<Long> ids = candidateUsersService.calculateTaskCandidateUsers(execution.getProcessInstanceId(), task);
        return ids.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public boolean hasComplete(DelegateExecution execution) {
        boolean intoNextNode = DECISION_HOLDER.get().isIntoNextNode();
        DECISION_HOLDER.remove();
        return intoNextNode;
    }

    @Override
    public boolean isMulti(String taskKey) {

        CscpFlowTaskMultiRuleExample example = new CscpFlowTaskMultiRuleExample();
        example.or().andTaskDefinitionKeyEqualTo(taskKey);
        List<CscpFlowTaskMultiRule> data = cscpFlowTaskMultiRuleService.findByExample(example).getData();

        return CollectionUtil.isNotEmpty(data);
    }
}
