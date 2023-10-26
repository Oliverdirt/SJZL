package com.ctsi.flow.server.engine.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRuleExample;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.service.CscpFlowTaskMultiRuleService;
import com.ctsi.flow.multi.service.MultiInstanceService;
import com.ctsi.flow.param.FlowOutLine;
import com.ctsi.flow.param.model.ActRdTurnTask;
import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpFlowProcessDefExtExample;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.*;
import com.ctsi.flow.repository.CscpFlowProcessDefExtRepository;
import com.ctsi.flow.server.door.FlowDoorService;
import com.ctsi.flow.server.engine.FlowTaskService;
import com.ctsi.flow.server.process.FlowProcessInstanceService;
import com.ctsi.flow.util.*;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDTO;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.service.CscpCustomizeTemplateService;
import com.ctsi.ssdc.gen.service.CscpCustomizeVformService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ctsi.flow.constant.ErrorCodeConstants.*;
import static com.ctsi.flow.param.convert.FlowProcessDefinitionConvert.convertSuspendedToSuspensionState;
import static com.ctsi.flow.util.CollectionUtils.convertSet;
import static com.ctsi.flow.util.RunFlowExceptionUtil.exception;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-27 17:17
 * @description ：
 */

@Slf4j
@Service
public class FlowTaskServiceImpl implements FlowTaskService {


    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private FlowProcessInstanceService flowProcessInstanceService;

    @Resource
    private CscpUserService cscpUserService;

    @Resource
    private FlowDoorService flowDoorService;

    @Resource
    private CscpFlowProcessDefExtRepository cscpFlowProcessDefExtRepository;

    @Resource
    private CscpCustomizeTemplateService cscpCustomizeTemplateService;

    @Resource
    private CscpCustomizeVformService cscpCustomizeVformService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private CscpFlowTaskMultiRuleService cscpFlowTaskMultiRuleService;

    @Resource
    private MultiInstanceService multiInstanceService;

    @Resource
    private RuntimeService runtimeService;

    @Resource(name = "flow-extra-executor")
    private ThreadPoolTaskExecutor flowExtraExecutor;

    /**
     * 任务节点需要签收标志
     **/
    public static final Integer TASK_NEED_CLAIM = 1;

    /**
     * 任务节点不需要签收标志
     **/
    public static final Integer TASK_NOT_NEED_CLAIM = 0;

    @Override
    public PageInfo<FlowTaskTodoPageItemResp> getTodoTaskPage(Long userId, FlowTaskTodoPageReq page) {
        // 查询待办任务(候选人有自己 & 分配给自己)
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(String.valueOf(userId))
                .orderByTaskCreateTime().desc();
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(page), page.getSize());

        if (CollectionUtil.isEmpty(tasks)) {
            return null;
        }
        // 获得 ProcessInstance Map
        Map<String, ProcessInstance> processInstanceMap = flowProcessInstanceService.getProcessInstanceMap(
                convertSet(tasks, Task::getProcessInstanceId));
        // 取值 填充返回参数
        List<FlowTaskTodoPageItemResp> flowTaskTodoPageItemResps = CollectionUtils.convertList(tasks, task -> {
            FlowTaskTodoPageItemResp bpmTaskTodoPageItemRespVO = new FlowTaskTodoPageItemResp();
            bpmTaskTodoPageItemRespVO.setSuspensionState(convertSuspendedToSuspensionState(task.isSuspended()));
            bpmTaskTodoPageItemRespVO.setTaskCode(task.getTaskDefinitionKey());
            bpmTaskTodoPageItemRespVO.setId(task.getId());
            bpmTaskTodoPageItemRespVO.setName(task.getName());
            bpmTaskTodoPageItemRespVO.setClaimTime(task.getClaimTime());
            bpmTaskTodoPageItemRespVO.setCreateTime(task.getCreateTime());
            ProcessInstance processInstance = processInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                String startUserId = processInstance.getStartUserId();
                FlowTaskTodoPageItemResp.ProcessInstance processInstance1 = new FlowTaskTodoPageItemResp.ProcessInstance();
                processInstance1.setId(processInstance.getId());
                processInstance1.setName(processInstance.getName());
                if (startUserId != null) {
                    processInstance1.setStartUserId(Long.parseLong(processInstance.getStartUserId()));
                    CscpUserDTO one = cscpUserService.findOne(Long.valueOf(startUserId));
                    processInstance1.setStartUserNickname(one.getUsername());
                }
                String processDefinitionId = processInstance.getProcessDefinitionId();
                //获取formId
                CscpFlowProcessDefExtExample extExample = new CscpFlowProcessDefExtExample();
                extExample.or().andProcessDefinitionIdEqualTo(processDefinitionId);
                // 只会查询到一条数据
                List<CscpFlowProcessDefExt> cscpFlowProcessDefExts = cscpFlowProcessDefExtRepository.selectByExample(extExample);
                bpmTaskTodoPageItemRespVO.setFormId(String.valueOf(cscpFlowProcessDefExts.get(0).getFormId()));
                processInstance1.setProcessDefinitionId(processDefinitionId);
                bpmTaskTodoPageItemRespVO.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
                bpmTaskTodoPageItemRespVO.setProcessInstance(processInstance1);
            }
            // 根据当前是否已有处理人，判断是否需要进行签收操作
            if (StringUtils.isNotBlank(task.getAssignee())) {
                bpmTaskTodoPageItemRespVO.setClaimFalg(TASK_NOT_NEED_CLAIM);
            } else {
                bpmTaskTodoPageItemRespVO.setClaimFalg(TASK_NEED_CLAIM);
            }
            return bpmTaskTodoPageItemRespVO;
        });
        // 拼接结果
        long modelCount = taskQuery.count();
        PageInfo<FlowTaskTodoPageItemResp> pageInfo = new PageInfo<>(flowTaskTodoPageItemResps);

        PageHelperTool.initPageInfoObj(page.getPage(), (int) modelCount, page.getSize(), pageInfo);

        return pageInfo;
    }

    @Override
    public PageInfo<FlowTaskDonePageItemResp> getDoneTaskPage(Long userId, FlowTaskDonePageReq page) {

        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery().finished().deleteReason(null).taskAssignee(String.valueOf(userId));
        List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceQuery.listPage(PageUtils.getStart(page), page.getSize());

        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .finished() // 已完成
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序

        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(page), page.getSize());
        if (CollUtil.isEmpty(tasks)) {
            return new PageInfo<>();
        }

        // 获得 ProcessInstance Map
        Map<String, HistoricProcessInstance> processInstanceMap = flowProcessInstanceService.getHistoricProcessInstanceMap(
                convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));

        List<FlowTaskDonePageItemResp> flowTaskDownPageItemResps = CollectionUtils.convertList(tasks, task -> {
            FlowTaskDonePageItemResp bpmTaskDownPageItemResp = new FlowTaskDonePageItemResp();
            bpmTaskDownPageItemResp.setId(task.getId());
            bpmTaskDownPageItemResp.setName(task.getName());
            bpmTaskDownPageItemResp.setClaimTime(task.getClaimTime());
            bpmTaskDownPageItemResp.setCreateTime(task.getCreateTime());
            HistoricProcessInstance historicProcessInstance = processInstanceMap.get(task.getProcessInstanceId());
            if (historicProcessInstance != null) {
                String startUserId = historicProcessInstance.getStartUserId();

                FlowTaskTodoPageItemResp.ProcessInstance processInstance1 = new FlowTaskTodoPageItemResp.ProcessInstance();
                processInstance1.setId(historicProcessInstance.getId());
                processInstance1.setName(historicProcessInstance.getName());
                if (startUserId != null) {
                    processInstance1.setStartUserId(Long.parseLong(historicProcessInstance.getStartUserId()));
                    CscpUserDTO one = cscpUserService.findOne(Long.valueOf(startUserId));
                    processInstance1.setStartUserNickname(one.getUsername());
                }
                processInstance1.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
                bpmTaskDownPageItemResp.setProcessInstance(processInstance1);
                //获取formId
                CscpFlowProcessDefExtExample extExample = new CscpFlowProcessDefExtExample();
                extExample.or().andProcessDefinitionIdEqualTo(historicProcessInstance.getProcessDefinitionId());
                // 只会查询到一条数据
                List<CscpFlowProcessDefExt> cscpFlowProcessDefExts = cscpFlowProcessDefExtRepository.selectByExample(extExample);
                bpmTaskDownPageItemResp.setFormId(String.valueOf(cscpFlowProcessDefExts.get(0).getFormId()));

            }
            return bpmTaskDownPageItemResp;
        });
        // 拼接结果
        long modelCount = taskQuery.count();
        PageInfo<FlowTaskDonePageItemResp> pageInfo = new PageInfo<>(flowTaskDownPageItemResps);

        PageHelperTool.initPageInfoObj(page.getPage(), (int) modelCount, page.getSize(), pageInfo);

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String approveTask(Long userId, FlowTaskApproveReq req) {
        Task task = getTask(req.getId());
        String processDefinitionId = task.getProcessDefinitionId();
        String processInstanceId = task.getProcessInstanceId();
        //根据processDefinitionId查询出formId
        //获取formId
        updateFormMessage(req, processDefinitionId, processInstanceId);
        // 校验任务存在
        ProcessInstance processInstance = checkTask(task, userId, req.getId());
        Map<String, Object> processVariables = processInstance.getProcessVariables();
        Map<String, Object> flowMap = req.getFlowMap();
        if (!CollectionUtil.isEmpty(processVariables)) {
            processVariables.putAll(flowMap);
        } else {
            processVariables = flowMap;
        }

        // 添加多实例的决策
        String taskDefinitionKey = task.getTaskDefinitionKey();
        CscpFlowTaskMultiRuleExample example = new CscpFlowTaskMultiRuleExample();
        example.or().andTaskDefinitionKeyEqualTo(taskDefinitionKey);
        List<CscpFlowTaskMultiRule> data = cscpFlowTaskMultiRuleService.findByExample(example).getData();

        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();

        // 不为空，则是多实例，计算多实例的结果， 默认处理通过

        MultiInstanceDecisionResult decision = null;
        if (CollectionUtil.isNotEmpty(data)) {
            multiInstanceService.update(execution.getParentId(), req.getVote());
            decision = multiInstanceService.decision(data.get(0), execution.getId(), execution.getParentId(), processDefinitionId, req.getVote());
            // 添加决策结果
            if (decision.getMultiCalVal() != null) {
                processVariables.put(task.getTaskDefinitionKey() + "_result", decision.getMultiCalVal());
            }
        }

        // 添加处理走向参数
        if (req.getKey() != null) {
            processVariables.put(req.getKey(), req.getValue());
        }

        // 添加任务批注
        taskService.addComment(task.getId(), task.getProcessInstanceId(), req.getReason());
        taskService.setVariables(task.getId(), processVariables);
        //        // 完成任务，审批通过
        taskService.complete(task.getId(), processVariables);

        if (decision != null && decision.isIntoNextNode()) {
            deleteMultiHis();
        }

        return processInstanceId;

    }

    /**
     * create by: guoyanpei
     * description: 更新自定义表单数据
     * params: FlowTaskApproveReq req, String processDefinitionId, String processInstanceId
     * return: void
     */
    private void updateFormMessage(FlowTaskApproveReq req, String processDefinitionId, String processInstanceId) {

        CscpFlowProcessDefExtExample extExample = new CscpFlowProcessDefExtExample();
        extExample.or().andProcessDefinitionIdEqualTo(processDefinitionId);
        List<CscpFlowProcessDefExt> cscpFlowProcessDefExts = cscpFlowProcessDefExtRepository.selectByExample(extExample);
        Long formId = cscpFlowProcessDefExts.get(0).getFormId();

        CscpCustomizeVform cscpCustomizeVform = cscpCustomizeVformService.findOne(formId);

        //如果是自定义表单更新数据
        String formType = cscpCustomizeVform.getFormType();
        if (StringUtils.equals(formType, "1")) {
            HashMap<String, Object> queryMap = Maps.newHashMap();
            queryMap.put("instance_id", processInstanceId);
            //查询该条数据的业务库数据
            Map<String, Object> map = cscpCustomizeTemplateService.queryByInstanceId(formId, queryMap);
            map.putAll(req.getFlowMap());
            cscpCustomizeTemplateService.updateTemplate(formId, map);
        }
    }

    /**
     * create by: guoyanpei
     * description: 异步删除多实例的自动完成的任务
     */
    private void deleteMultiHis() {
        CompletableFuture.runAsync(() -> {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskDeleteReason("Multi-instance complete condition expression passed").list();
            list.forEach(e -> historyService.deleteHistoricTaskInstance(e.getId()));
        }, flowExtraExecutor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nextAssigner(String nextHandler, String procId) {

        if (StringUtils.isNotEmpty(nextHandler)) {

            //查询流程的待办任务
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(procId)
                    .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                    .list();

            // 获取最新的待办任务
            HistoricTaskInstance historicTaskInstance = tasks.get(0);
            String id = historicTaskInstance.getId();

            // 更新任务处理人
            taskService.setAssignee(id, nextHandler);

        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskAssignee(Long userId, FlowTaskUpdateAssigneeReq req) {
        Task task = getTask(req.getId());
        // 校验任务
        ProcessInstance processInstance = checkTask(task, userId, req.getId());
        // 更新负责人
        taskService.setAssignee(task.getId(), String.valueOf(req.getAssigneeUserId()));
        // 插入转办任务表信息
        String formId = flowDoorService.qryFormIdByProcessDefinitionId(processInstance.getProcessDefinitionId());
        ActRdTurnTask turnTask = new ActRdTurnTask();
        turnTask.setProcessInstid(processInstance.getId());
        turnTask.setCreateTime(ZonedDateTime.now());
        turnTask.setProcessName(task.getName());
        turnTask.setType("2");
        turnTask.setUserId(userId);
        turnTask.setProcessId(processInstance.getProcessDefinitionId());
        turnTask.setFormId(formId);
        turnTask.setCurAssignee(req.getAssigneeUserId());
        turnTask.setLastAssignee(userId);
        flowDoorService.createRdTurnTask(turnTask);
    }

    @Override
    public List<FlowTaskResp> getTaskListByProcessInstanceId(String processInstanceId) {

        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }
        List<HistoricTaskInstance> collect = tasks.stream().filter(e -> StringUtils.isEmpty(e.getDeleteReason())).collect(Collectors.toList());
        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = flowProcessInstanceService.getHistoricProcessInstance(processInstanceId);
        List<FlowTaskResp> flowTaskResps = CollectionUtils.convertList(collect, task -> {
            FlowTaskResp flowTaskResp = new FlowTaskResp();

            flowTaskResp.setDefinitionKey(task.getTaskDefinitionKey());
            flowTaskResp.setId(task.getId());
            flowTaskResp.setName(task.getName());
            flowTaskResp.setClaimTime(task.getClaimTime());
            flowTaskResp.setCreateTime(task.getCreateTime());
            flowTaskResp.setEndTime(task.getEndTime());
            flowTaskResp.setDurationInMillis(task.getDurationInMillis());
            flowTaskResp.setResult(task.getEndTime() == null ? 1 : 2);
            List<Comment> taskComments = taskService.getTaskComments(task.getId());
            if (CollUtil.isEmpty(taskComments)) {
                flowTaskResp.setReason("");
            } else {
                flowTaskResp.setReason(taskComments.get(0).getFullMessage());
            }
            if (processInstance != null) {
                String assignee = task.getAssignee();
                if (assignee != null) {
                    CscpUserDTO one = cscpUserService.findOne(Long.valueOf(assignee));
                    FlowTaskResp.User user = new FlowTaskResp.User();
                    user.setId(one.getId());
                    user.setNickname(one.getUsername());
                    flowTaskResp.setAssigneeUser(user);
                }

            }
            return flowTaskResp;
        });
        return flowTaskResps;
    }

    @Override
    public PageInfo<FlowMyProcessInstanceResp> getMyProcessPage(Long currentUserId, FlowTaskDonePageReq page) {

        HistoricProcessInstanceQuery processQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(currentUserId))
                .orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> historicProcessInstances = processQuery.listPage(PageUtils.getStart(page), page.getSize());
        long modelCount = processQuery.count();


        List<FlowMyProcessInstanceResp> flowMyProcessInstanceResps = CollectionUtils.convertList(historicProcessInstances, e -> {
            String processDefinitionKey = e.getProcessDefinitionKey();
            Model model = repositoryService.createModelQuery().modelKey(processDefinitionKey).singleResult();
            FlowMyProcessInstanceResp flowMyProcessInstanceResp = new FlowMyProcessInstanceResp();
            flowMyProcessInstanceResp.setName(e.getName());
            flowMyProcessInstanceResp.setCreateTime(e.getStartTime());
            if (model != null) {
                String metaInfo = model.getMetaInfo();
                FlowModelMetaInfoResp flowModelMetaInfoResp = JsonUtils.parseObject(metaInfo, FlowModelMetaInfoResp.class);
                flowMyProcessInstanceResp.setBack(e.getEndTime() == null && flowModelMetaInfoResp.getBackState() == 1);
            } else {
                flowMyProcessInstanceResp.setBack(false);
            }
            flowMyProcessInstanceResp.setCreateTime(e.getStartTime());
            flowMyProcessInstanceResp.setProcessInstanceId(e.getId());
            flowMyProcessInstanceResp.setProcessDeployId(e.getProcessDefinitionId());
            CscpFlowProcessDefExtExample extExample = new CscpFlowProcessDefExtExample();
            extExample.or().andProcessDefinitionIdEqualTo(e.getProcessDefinitionId());
            // 只会查询到一条数据
            // 只会查询到一条数据
            List<CscpFlowProcessDefExt> cscpFlowProcessDefExts = cscpFlowProcessDefExtRepository.selectByExample(extExample);
            flowMyProcessInstanceResp.setFormId(cscpFlowProcessDefExts.get(0).getFormId());

            flowMyProcessInstanceResp.setProcessDeployId(e.getProcessDefinitionId());
            return flowMyProcessInstanceResp;
        });
        PageInfo<FlowMyProcessInstanceResp> pageInfo = new PageInfo<>(flowMyProcessInstanceResps);
        PageHelperTool.initPageInfoObj(page.getPage(), (int) modelCount, page.getSize(), pageInfo);
        return pageInfo;
    }

    @Override
    public List<FlowOutLine> getOutLines(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        UserTask userTask = (UserTask) flowElement;
        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();

        // 判断出口路线是不是1条，不是一条选择出口直接遍历得到结果，一条出口路线看是不是排他网关，是的话查出后面的几个路线
        if (outgoingFlows.size() == 1) {
            SequenceFlow sequenceFlow = outgoingFlows.get(0);
            if (sequenceFlow.getTargetRef().contains("Gateway")) {
                FlowElement targetFlowElement = outgoingFlows.get(0).getTargetFlowElement();
                String id = targetFlowElement.getId();
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) bpmnModel.getFlowElement(id);
                List<SequenceFlow> outgoingFlows1 = exclusiveGateway.getOutgoingFlows();
                List<FlowOutLine> outLines = getOutLine(outgoingFlows1);
                if (outgoingFlows1.size() != outLines.size()) {
                    return null;
                } else {
                    return outLines;
                }
            } else {
                return null;
            }


        } else {
            List<FlowOutLine> outLines = getOutLine(outgoingFlows);
            if (outgoingFlows.size() != outLines.size()) {
                return null;
            } else {
                return outLines;
            }
        }
    }

    /**
     * create by: guoyanpei
     * description: 处理解析表达式
     * params: List<SequenceFlow>
     * return: List<FlowOutLine>
     */
    private List<FlowOutLine> getOutLine(List<SequenceFlow> outgoingFlows1) {

        ArrayList<FlowOutLine> resultMaps = Lists.newArrayList();
        outgoingFlows1.forEach(e -> {
            String name = e.getName();
            if (StringUtils.isNotEmpty(name)) {
                String conditionExpression = e.getConditionExpression();
                String condition = conditionExpression.replace("$", "")
                        .replace("{", "")
                        .replace("}", "").trim();
                if (condition.contains("==")) {
                    String key = condition.subSequence(0, condition.indexOf("=="))
                            .toString()
                            .replace("=", "")
                            .trim();
                    String value = condition.subSequence(condition.indexOf("=="), condition.length())
                            .toString()
                            .replace("=", "")
                            .replace("\"", "")
                            .replace("\"", "")
                            .trim();
                    FlowOutLine buildLine = FlowOutLine.builder().name(name)
                            .key(key)
                            .value(value)
                            .build();
                    resultMaps.add(buildLine);
                }
            }
        });
        return resultMaps;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Void backProcess(String processInstanceId) {

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        //  获取所有历史任务（按创建时间降序）
        List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        List<HistoricActivityInstance> hisActivityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();

        if (CollectionUtil.isEmpty(hisTaskList) || hisTaskList.size() < 2) {
            throw RunFlowExceptionUtil.exception(TASK_BACK_FAIL_CAN_NOT_BACK);
        }

        //  当前任务
        HistoricTaskInstance currentTask = hisTaskList.get(0);
        //  前一个任务
        HistoricTaskInstance lastTask = hisTaskList.get(1);

//        if (!lastTask.getAssignee().equals(String.valueOf(SecurityHxUtils.getCurrentUserId()))) {
//            throw RunFlowExceptionUtil.exception(TASK_BACK_FAIL_COMPARED);
//        }

        //  当前活动
        HistoricActivityInstance currentActivity = hisActivityList.stream()
                .filter(e -> currentTask.getId().equals(e.getTaskId()))
                .collect(Collectors.toList()).get(0);
        //  前一个活动
        HistoricActivityInstance lastActivity = hisActivityList.stream()
                .filter(e -> lastTask.getId().equals(e.getTaskId()))
                .collect(Collectors.toList()).get(0);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //  获取前一个活动节点
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(lastActivity.getActivityId());
        //  获取当前活动节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivity.getActivityId());

        //  临时保存当前活动的原始方向
        List<SequenceFlow> originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());


        handle(task, lastFlowNode, currentFlowNode);


        //  重新查询当前任务
        Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (null != nextTask) {
            taskService.setAssignee(nextTask.getId(), lastTask.getAssignee());
        }

        //  恢复原始方向
        currentFlowNode.setOutgoingFlows(originalSequenceFlowList);

        return null;
    }

    @Override
    public Void endProcess(String processInstanceId) {

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        handle(task, endFlowNode, currentFlowNode);

        return null;
    }

    private void handle(Task task, FlowNode endFlowNode, FlowNode currentFlowNode) {

        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());
//        taskService.deleteTask(task.getId());
        historyService.deleteHistoricTaskInstance(task.getId());
    }

    /**
     * @Description: 签收任务
     * @Author: sunsheng
     * @parm: [currentUserId, taskId]
     * @return: void
     **/
    @Override
    public void claimTask(Long currentUserId, String taskId) {
        taskService.claim(taskId, String.valueOf(currentUserId));
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private ProcessInstance checkTask(Task task, Long userId, String id) {


        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }
        if (!StringUtils.equals(task.getAssignee(), String.valueOf(userId))) {
            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
        }
        // 校验流程实例存在
        ProcessInstance instance = flowProcessInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }
        return instance;
    }
}
