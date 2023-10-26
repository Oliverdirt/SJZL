package com.ctsi.flow.server.process;

import com.ctsi.flow.param.request.FlowProcessInstanceCancelReq;
import com.ctsi.flow.param.request.FlowProcessInstanceCreateReq;
import com.ctsi.flow.param.response.FlowActivityResp;
import com.ctsi.flow.param.response.FlowProcessInstanceResp;
import com.ctsi.flow.util.CollectionUtils;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 流程实例 Service 接口
 * @Author: sunsheng
 **/
public interface FlowProcessInstanceService {

    /**
     * 创建流程实例
     *
     * @param userId      用户编号
     * @param createReqVO 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(Long userId, @Valid FlowProcessInstanceCreateReq createReqVO);


    /**
     * 取消流程实例
     *
     * @param userId      用户编号
     * @param cancelReqVO 取消信息
     */
    void cancelProcessInstance(Long userId, @Valid FlowProcessInstanceCancelReq cancelReqVO);


    /**
     * 获得流程实例 VO 信息
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    FlowProcessInstanceResp getProcessInstanceVO(String id);

    /**
     * 获得流程实例
     *
     * @param id 流程实例的编号
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String id);

    /**
     * 获得流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表
     */
    List<ProcessInstance> getProcessInstances(Set<String> ids);

    /**
     * 获得流程实例 Map
     *
     * @param ids 流程实例的编号集合
     * @return 流程实例列表 Map
     */
    default Map<String, ProcessInstance> getProcessInstanceMap(Set<String> ids) {
        return CollectionUtils.convertMap(getProcessInstances(ids), ProcessInstance::getProcessInstanceId);
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    HistoricProcessInstance getHistoricProcessInstance(String id);

    /**
     * 获得历史的流程实例列表
     *
     * @param ids 流程实例的编号集合
     * @return 历史的流程实例列表
     */
    List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids);

    /**
     * 获得历史的流程实例 Map
     *
     * @param ids 流程实例的编号集合
     * @return 历史的流程实例列表 Map
     */
    default Map<String, HistoricProcessInstance> getHistoricProcessInstanceMap(Set<String> ids) {
        return CollectionUtils.convertMap(getHistoricProcessInstances(ids), HistoricProcessInstance::getId);
    }



    /**
     * 查询历史活动
     * params: 流程实例ID
     * return: 流程列表
     */
    List<FlowActivityResp> getActivityListByProcessInstanceId(String processInstanceId);
}
