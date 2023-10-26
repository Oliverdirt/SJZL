package com.ctsi.flow.server.rule;

import com.ctsi.flow.param.model.ActOperatePerm;
import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;
import com.ctsi.flow.param.request.FlowTaskAssignRuleCreateReq;
import com.ctsi.flow.param.request.FlowTaskAssignRuleUpdateReq;
import com.ctsi.flow.param.response.FlowTaskRuleResp;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import java.util.List;


/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 *
 * @author cczz
 * @version 1.0  2022/7/14 16:51  by xx
 */
public interface FlowRuleService {
    /*修改任务操作权限*/
    void updateTaskPerm(ActOperatePerm actOperatePerm);

    /*查询任务操作权限*/
    List<ActOperatePerm> selectTaskPerm(ActOperatePerm actOperatePerm);
    /**报错分配权限规则*/


    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param modelId             流程模型的编号
     * @param processDefinitionId 流程定义的编号
     * @return 任务规则数组
     */
    List<FlowTaskRuleResp> getTaskAssignRuleList(String modelId, String processDefinitionId);


    /**
     * 创建任务分配规则
     *
     * @param reqVO 创建信息
     * @return 规则编号
     */
    Long createTaskAssignRule(@Valid FlowTaskAssignRuleCreateReq reqVO);


    /**
     * 更新任务分配规则
     *
     * @param reqVO 创建信息
     */
    void updateTaskAssignRule(@Valid FlowTaskAssignRuleUpdateReq reqVO);


    /**
     * 获得流程模型的任务规则数组
     *
     * @param modelId 流程模型的编号
     * @return 任务规则数组
     */
    List<CscpFlowTaskAssignRule> getTaskAssignRuleListByModelId(String modelId);


    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param processDefinitionId 流程定义的编号
     * @param taskDefinitionKey   流程任务定义的 Key。允许空
     * @return 任务规则数组
     */
    List<CscpFlowTaskAssignRule> getTaskAssignRuleListByProcessDefinitionId(String processDefinitionId,
                                                                            @Nullable String taskDefinitionKey);

    /**
     * 获得流程定义的任务分配规则数组
     *
     * @param taskDefinitionKey 流程定义的编号
     * @return 任务规则数组
     */
    CscpFlowTaskAssignRule getTaskAssignRuleListByTaskDefinitionKey(String taskDefinitionKey);

    /**
     * description:校验人工任务配置
     */
    void checkTaskAssignRuleAllConfig(String id);

    /**
     * 判断指定流程模型和流程定义的分配规则是否相等
     */
    boolean isTaskAssignRulesEquals(String modelId, String processDefinitionId);
}
