package com.ctsi.flow.server.process.impl;


import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.enums.FlowProcessInstanceDeleteReasonEnum;
import com.ctsi.flow.param.request.FlowProcessInstanceCancelReq;
import com.ctsi.flow.param.request.FlowProcessInstanceCreateReq;
import com.ctsi.flow.param.response.FlowActivityResp;
import com.ctsi.flow.param.response.FlowProcessInstanceResp;
import com.ctsi.flow.server.process.FlowDefService;
import com.ctsi.flow.server.process.FlowProcessInstanceService;
import com.ctsi.ssdc.gen.service.CscpCustomizeTemplateService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.ctsi.flow.constant.ErrorCodeConstants.*;
import static com.ctsi.flow.util.RunFlowExceptionUtil.exception;


/**
 * @Description: 流程实例 Service 实现类
 * @Author: sunsheng
 **/
@Service
@Validated
@Slf4j
public class FlowProcessInstanceServiceImpl implements FlowProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private FlowDefService flowDefService;

    @Resource
    private CscpCustomizeTemplateService cscpCustomizeTemplateService;


    /**
     * 创建流程实例
     *
     * @param userId      用户编号
     * @param createReqVO 创建信息
     * @return 实例的编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Long userId, @Valid FlowProcessInstanceCreateReq createReqVO) {
        // 获得流程定义
        ProcessDefinition definition = flowDefService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        // 启动流程
        return startProcessInstance(userId, definition, createReqVO.getFormId(), createReqVO.getVariables(), null);
    }

    /**
     * 取消流程实例
     *
     * @param userId      用户编号
     * @param cancelReqVO 取消信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelProcessInstance(Long userId, @Valid FlowProcessInstanceCancelReq cancelReqVO) {
        // 校验流程实例存在
        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);
        }
        // 只能取消自己的
        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);
        }

        // 通过删除流程实例，实现流程实例的取消
        runtimeService.deleteProcessInstance(cancelReqVO.getId(),
                FlowProcessInstanceDeleteReasonEnum.CANCEL_TASK.format(cancelReqVO.getReason()));
    }


    /**
     * 获得流程实例 VO 信息
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    @Override
    public FlowProcessInstanceResp getProcessInstanceVO(String id) {

        // 获得流程实例
        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);
        FlowProcessInstanceResp flowProcessInstanceResp = new FlowProcessInstanceResp();
        if (processInstance == null) {
            return null;
        }
        ProcessDefinition processDefinition = flowDefService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        flowProcessInstanceResp.setEndTime(processInstance.getEndTime());
        flowProcessInstanceResp.setCreateTime(processInstance.getStartTime());
        flowProcessInstanceResp.setResult(processInstance.getEndTime() == null ? 1 : 2);
        flowProcessInstanceResp.setName(processInstance.getName());
        flowProcessInstanceResp.setId(id);

        FlowProcessInstanceResp.ProcessDefinition processDefinition1 = new FlowProcessInstanceResp.ProcessDefinition();
        processDefinition1.setId(processDefinition.getId());
        String bpmnXml = flowDefService.getProcessDefinitionBpmnXml(processInstance.getProcessDefinitionId());

        processDefinition1.setBpmnXml(bpmnXml);
        flowProcessInstanceResp.setProcessDefinition(processDefinition1);
        return flowProcessInstanceResp;
    }

    public static Long parseLong(String str) {
        return StrUtil.isNotEmpty(str) ? Long.valueOf(str) : null;
    }

    /**
     * 获得流程实例
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    /**
     * 获得流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表
     */
    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    /**
     * 获得历史的流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 历史的流程实例列表
     */
    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();
    }



    @Override
    public List<FlowActivityResp> getActivityListByProcessInstanceId(String processInstanceId) {

        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();

        List<FlowActivityResp> respList = Lists.newArrayList();
        activityList.forEach(e -> {
            FlowActivityResp task = new FlowActivityResp();
            task.setKey(e.getActivityId());
            task.setType(e.getActivityType());
            task.setStartTime(e.getStartTime());
            task.setEndTime(e.getEndTime());
            task.setTaskId(e.getTaskId());
            respList.add(task);
        });
        return respList;
    }


    private String startProcessInstance(Long userId, ProcessDefinition definition,
                                        Long formId, Map<String, Object> variables, String businessKey) {
        // 校验流程定义
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }
        if (definition.isSuspended()) {
            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);
        }

        Authentication.setAuthenticatedUserId(String.valueOf(userId));
        // 创建流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), businessKey, variables);
        // 设置流程名字
        runtimeService.setProcessInstanceName(instance.getId(), definition.getName());


        // 插入业务表信息
        if (formId != null) {
            variables.put("instance_id", instance.getId());
            cscpCustomizeTemplateService.addTemplate(formId, variables);
        }

        return instance.getId();
    }
}
