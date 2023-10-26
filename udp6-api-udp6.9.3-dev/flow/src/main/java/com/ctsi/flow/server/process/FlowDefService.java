package com.ctsi.flow.server.process;

import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.request.FlowProcessDefCreateReq;
import com.ctsi.flow.param.request.FlowProcessDefListReq;
import com.ctsi.flow.param.request.FlowProcessDefPageReq;
import com.ctsi.flow.param.response.FlowProcessDefPageItemResp;
import com.ctsi.flow.param.response.FlowProcessDefResp;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.ssdc.model.PageResult;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-14 10:45
 * @description ：
 */

public interface FlowDefService {

    default Map<String, Deployment> getDeploymentMap(Set<String> ids) {
        return CollectionUtils.convertMap(getDeployments(ids), Deployment::getId);
    }

    PageResult<FlowProcessDefPageItemResp> getProcessDefinitionPage(FlowProcessDefPageReq pageReqVO);


    String createProcessDefinition(@Valid FlowProcessDefCreateReq createReqDTO);


    void updateProcessDefinitionState(String id, Integer state);


    String getProcessDefinitionBpmnXml(String id);


    boolean isProcessDefinitionEquals(@Valid FlowProcessDefCreateReq createReqDTO);



    List<FlowProcessDefResp> getProcessDefinitionList(FlowProcessDefListReq listReqVO);


    BpmnModel getBpmnModel(String processDefinitionId);


    ProcessDefinition getProcessDefinition(String id);


    ProcessDefinition getProcessDefinition2(String id);


    ProcessDefinition getActiveProcessDefinition(String key);

    /**
     * 获得编号对应的 BpmProcessDefinitionExtDO
     *
     * @param id 编号
     * @return 流程定义拓展
     */
    CscpFlowProcessDefExt getProcessDefinitionExt(String id);


    Deployment getDeployment(String id);


    List<Deployment> getDeployments(Set<String> ids);


    ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId);


    List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds);
}
