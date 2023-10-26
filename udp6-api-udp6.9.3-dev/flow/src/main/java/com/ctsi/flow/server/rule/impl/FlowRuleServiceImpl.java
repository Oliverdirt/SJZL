package com.ctsi.flow.server.rule.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.constant.ErrorCodeConstants;
import com.ctsi.flow.param.convert.FlowTaskAssignRuleConvert;
import com.ctsi.flow.param.model.*;
import com.ctsi.flow.param.request.FlowTaskAssignRuleCreateReq;
import com.ctsi.flow.param.request.FlowTaskAssignRuleUpdateReq;
import com.ctsi.flow.param.response.FlowTaskRuleResp;
import com.ctsi.flow.repository.ActOperatePermRepository;
import com.ctsi.flow.repository.CscpFlowTaskAssignRuleRepository;
import com.ctsi.flow.server.engine.FlowBpmnService;
import com.ctsi.flow.server.process.FlowDefService;
import com.ctsi.flow.server.rule.FlowRuleService;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.github.pagehelper.util.StringUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.*;

import static com.ctsi.flow.constant.ErrorCodeConstants.*;
import static com.ctsi.flow.util.RunFlowExceptionUtil.exception;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 *
 * @author cczz
 * @version 1.0  2022/7/14 16:51  by xx
 */
@Service
public class FlowRuleServiceImpl implements FlowRuleService {

    @Resource
    private ActOperatePermRepository permRepository;

//    @Resource
//    private BpmTaskAssignRuleRepository bpmTaskAssignRuleRepository;

    @Resource
    private CscpFlowTaskAssignRuleRepository cscpFlowTaskAssignRuleRepository;


    @Resource
    @Lazy
    private FlowBpmnService flowBpmnService;

    @Resource
    @Lazy
    private FlowDefService flowDefService;

    @Override
    public void updateTaskPerm(ActOperatePerm actOperatePerm) {
        if (StringUtils.isEmpty(actOperatePerm.getId())) {
            permRepository.insert(actOperatePerm);
        } else {
            permRepository.updateByPrimaryKeySelective(actOperatePerm);
        }
    }

    @Override
    public List<ActOperatePerm> selectTaskPerm(ActOperatePerm actOperatePerm) {
        ActOperatePermExample actOperatePermExample = new ActOperatePermExample();
        actOperatePermExample.createCriteria().andTaskDefinitionKeyEqualTo(actOperatePerm.getTaskDefinitionKey())
                .andTaskDefinitionFlagEqualTo(actOperatePerm.getTaskDefinitionFlag());
        return permRepository.selectByExample(actOperatePermExample);
    }

    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param modelId             流程模型的编号
     * @param processDefinitionId 流程定义的编号
     * @return 任务规则数组
     */
    @Override
    public List<FlowTaskRuleResp> getTaskAssignRuleList(String modelId, String processDefinitionId) {
        // 获得规则
        List<CscpFlowTaskAssignRule> rules = Collections.emptyList();
        BpmnModel model = null;
        if (StrUtil.isNotEmpty(modelId)) {
            rules = getTaskAssignRuleListByModelId(modelId);
            model = flowBpmnService.getBpmnModel(modelId);
        } else if (StrUtil.isNotEmpty(processDefinitionId)) {
            rules = getTaskAssignRuleListByProcessDefinitionId(processDefinitionId, null);
            model = flowDefService.getBpmnModel(processDefinitionId);
        }
        if (model == null) {
            return Collections.emptyList();
        }
        // 获得用户任务，只有用户任务才可以设置分配规则
        List<UserTask> userTasks = getBpmnModelElements(model, UserTask.class);
        if (CollUtil.isEmpty(userTasks)) {
            return Collections.emptyList();
        }

        // 转换数据
        return FlowTaskAssignRuleConvert.convertList(userTasks, rules);
    }

    /**
     * 创建任务分配规则
     *
     * @param reqVO 创建信息
     * @return 规则编号
     */
    @Override
    public Long createTaskAssignRule(@Valid FlowTaskAssignRuleCreateReq reqVO) {
        // 校验参数
//        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否已经配置
        CscpFlowTaskAssignRule existRule = cscpFlowTaskAssignRuleRepository.selectByByModelIdAndTaskDefinitionKey(reqVO.getModelId(), reqVO.getTaskDefinitionKey());
//        FlowTaskAssignRuleDO existRule = taskRuleMapper.selectListByModelIdAndTaskDefinitionKey(
//                reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        if (existRule != null) {
            throw exception(TASK_ASSIGN_RULE_EXISTS, reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        }

        // 存储
        CscpFlowTaskAssignRule rule = FlowTaskAssignRuleConvert.convert(reqVO);
        rule.setProcessDefinitionId(FlowTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL);
        rule.setCreator(SecurityHxUtils.getCurrentUserId() + "");
        rule.setCreateTime(ZonedDateTime.now());
        // 只有流程模型，才允许新建
        cscpFlowTaskAssignRuleRepository.insert(rule);
//        taskRuleMapper.insert(rule);
        return rule.getId();
    }

    /**
     * 更新任务分配规则
     *
     * @param reqVO 创建信息
     */
    @Override
    public void updateTaskAssignRule(@Valid FlowTaskAssignRuleUpdateReq reqVO) {
        // 校验参数
//        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否存在
        CscpFlowTaskAssignRule existRule = cscpFlowTaskAssignRuleRepository.selectByPrimaryKey(reqVO.getId());
//        FlowTaskAssignRuleDO existRule = taskRuleMapper.selectById(reqVO.getId());
        if (existRule == null) {
            throw exception(ErrorCodeConstants.TASK_ASSIGN_RULE_NOT_EXISTS);
        }
        // 只允许修改流程模型的规则
        if (!Objects.equals(FlowTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL, existRule.getProcessDefinitionId())) {
            throw exception(TASK_UPDATE_FAIL_NOT_MODEL);
        }

        // 执行更新
        CscpFlowTaskAssignRule result = FlowTaskAssignRuleConvert.convert(existRule, reqVO);
        result.setUpdater(SecurityHxUtils.getCurrentUserId() + "");
        result.setUpdateTime(ZonedDateTime.now());
        cscpFlowTaskAssignRuleRepository.updateByPrimaryKey(result);
//        taskRuleMapper.updateById();
    }

    /**
     * 获得流程模型的任务规则数组
     *
     * @param modelId 流程模型的编号
     * @return 任务规则数组
     */
    @Override
    public List<CscpFlowTaskAssignRule> getTaskAssignRuleListByModelId(String modelId) {
        CscpFlowTaskAssignRuleExample example = new CscpFlowTaskAssignRuleExample();
        StringCriteria criteria = new StringCriteria();
        criteria.setEquals(modelId);
        example.setModelId(criteria);
        List<CscpFlowTaskAssignRule> cscpFlowTaskAssignRules = cscpFlowTaskAssignRuleRepository.selectByExample(example);
        return cscpFlowTaskAssignRules;
//        return taskRuleMapper.selectListByModelId(modelId);
    }

    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param processDefinitionId 流程定义的编号
     * @param taskDefinitionKey   流程任务定义的 Key。允许空
     * @return 任务规则数组
     */
    @Override
    public List<CscpFlowTaskAssignRule> getTaskAssignRuleListByProcessDefinitionId(String processDefinitionId, String taskDefinitionKey) {

        CscpFlowTaskAssignRuleExample example = new CscpFlowTaskAssignRuleExample();
        example.or().andProcessDefinitionIdEqualTo(processDefinitionId);
        if (!StringUtil.isEmpty(taskDefinitionKey)) {
            example.or().andTaskDefinitionKeyEqualTo(taskDefinitionKey);
        }
        List<CscpFlowTaskAssignRule> cscpFlowTaskAssignRules = cscpFlowTaskAssignRuleRepository.selectByExample(example);
        return cscpFlowTaskAssignRules;
    }

    @Override
    public CscpFlowTaskAssignRule getTaskAssignRuleListByTaskDefinitionKey(String taskDefinitionKey) {
        CscpFlowTaskAssignRuleExample example = new CscpFlowTaskAssignRuleExample();
        example.or().andTaskDefinitionKeyEqualTo(taskDefinitionKey);
        List<CscpFlowTaskAssignRule> cscpFlowTaskAssignRules = cscpFlowTaskAssignRuleRepository.selectByExample(example);
        return cscpFlowTaskAssignRules.get(0);
    }

    @Override
    public void checkTaskAssignRuleAllConfig(String id) {
        // 一个用户任务都没配置，所以无需配置规则
        List<FlowTaskRuleResp> taskAssignRules = getTaskAssignRuleList(id, null);
        if (CollUtil.isEmpty(taskAssignRules)) {
            return;
        }
        // 校验未配置规则的任务
        taskAssignRules.forEach(rule -> {
            if (CollUtil.isEmpty(rule.getOptions())) {
                throw exception(MODEL_DEPLOY_FAIL_TASK_ASSIGN_RULE_NOT_CONFIG, rule.getTaskDefinitionName());
            }
        });
        // 校验未配置操作权限
    }

    @Override
    public boolean isTaskAssignRulesEquals(String modelId, String processDefinitionId) {
        // 调用 VO 接口的原因是，过滤掉流程模型不需要的规则，保持和 copyTaskAssignRules 方法的一致性
        List<FlowTaskRuleResp> modelRules = getTaskAssignRuleList(modelId, null);
        List<FlowTaskRuleResp> processInstanceRules = getTaskAssignRuleList(null, processDefinitionId);
        if (modelRules.size() != processInstanceRules.size()) {
            return false;
        }

        // 遍历，匹配对应的规则
        Map<String, FlowTaskRuleResp> processInstanceRuleMap = CollectionUtils.convertMap(processInstanceRules,
                FlowTaskRuleResp::getTaskDefinitionKey);
        for (FlowTaskRuleResp modelRule : modelRules) {
            FlowTaskRuleResp processInstanceRule = processInstanceRuleMap.get(modelRule.getTaskDefinitionKey());
            if (processInstanceRule == null) {
                return false;
            }
            if (!ObjectUtil.equals(modelRule.getType(), processInstanceRule.getType())
                    || !ObjectUtil.equal(modelRule.getOptions(), processInstanceRule.getOptions())) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获得 BPMN 流程中，指定的元素们
     *
     * @param model
     * @param clazz 指定元素。例如说，{@link org.activiti.bpmn.model.UserTask}、{@link org.activiti.bpmn.model.Gateway} 等等
     * @return 元素们
     */
    public static <T extends FlowElement> List<T> getBpmnModelElements(BpmnModel model, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        model.getProcesses().forEach(process -> {
            process.getFlowElements().forEach(flowElement -> {
                if (flowElement.getClass().isAssignableFrom(clazz)) {
                    result.add((T) flowElement);
                }
                setResult(clazz, result, flowElement);
            });
        });
        return result;
    }

    private static <T extends FlowElement> void setResult(Class<T> clazz, List<T> result, FlowElement flowElement) {
        if (flowElement.getClass().isAssignableFrom(SubProcess.class)) {
            SubProcess subProcess = (SubProcess) flowElement;
            Collection<FlowElement> flowElements = subProcess.getFlowElements();
            flowElements.forEach(e-> {
                if (e.getClass().isAssignableFrom(clazz)) {
                    result.add((T) e);
                }
                setResult(clazz,result,e);
            });
        }
    }


}
