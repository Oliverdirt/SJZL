package com.ctsi.flow.param.convert;

import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.param.request.FlowProcessDefCreateReq;
import com.ctsi.flow.param.response.FlowProcessDefPageItemResp;
import com.ctsi.flow.param.response.FlowProcessDefResp;
import com.ctsi.flow.util.CollectionUtils;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

/**
 * @Description: 流程定义的 Convert
 * @Author: sunsheng
 **/
public class FlowProcessDefinitionConvert {


    public static List<FlowProcessDefPageItemResp> convertList(List<ProcessDefinition> list, Map<String, Deployment> deploymentMap,
                                                               Map<String, CscpFlowProcessDefExt> processDefinitionDoMap, Map<Long, FlowFormDO> formMap) {
        return CollectionUtils.convertList(list, definition -> {
            Deployment deployment = definition.getDeploymentId() != null ? deploymentMap.get(definition.getDeploymentId()) : null;
            CscpFlowProcessDefExt definitionDO = processDefinitionDoMap.get(definition.getId());
            FlowFormDO form = definitionDO != null ? formMap.get(definitionDO.getFormId()) : null;
            return convert(definition, deployment, definitionDO, form);
        });
    }

    public static FlowProcessDefPageItemResp convert(ProcessDefinition bean, Deployment deployment,
                                                     CscpFlowProcessDefExt processDefinitionExtDO, FlowFormDO form) {
        FlowProcessDefPageItemResp respVO = convert(bean);
        respVO.setSuspensionState(bean.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        if (deployment != null) {
            respVO.setDeploymentTime(deployment.getDeploymentTime());
        }
        if (form != null) {
            respVO.setFormName(form.getName());
        }
        // 复制通用属性
        copyTo(processDefinitionExtDO, respVO);
        return respVO;
    }

    public static List<FlowProcessDefResp> convertList3(List<ProcessDefinition> list,
                                                          Map<String, CscpFlowProcessDefExt> processDefinitionDoMap) {
        return CollectionUtils.convertList(list, processDefinition -> {
            FlowProcessDefResp respVO = convert3(processDefinition);
            CscpFlowProcessDefExt processDefinitionExtDO = processDefinitionDoMap.get(processDefinition.getId());
            // 复制通用属性
            copyTo(processDefinitionExtDO, respVO);
            return respVO;
        });
    }



    public static FlowProcessDefPageItemResp convert(ProcessDefinition bean) {
        if ( bean == null ) {
            return null;
        }

        FlowProcessDefPageItemResp flowProcessDefPageItemResp = new FlowProcessDefPageItemResp();

        flowProcessDefPageItemResp.setId( bean.getId() );
        flowProcessDefPageItemResp.setVersion( bean.getVersion() );
        flowProcessDefPageItemResp.setName( bean.getName() );
        flowProcessDefPageItemResp.setDescription( bean.getDescription() );
        flowProcessDefPageItemResp.setCategory( bean.getCategory() );

        return flowProcessDefPageItemResp;
    }


    public static CscpFlowProcessDefExt convert2(FlowProcessDefCreateReq bean) {
        if ( bean == null ) {
            return null;
        }

//        FlowProcessDefinitionExtDO.FlowProcessDefinitionExtDOBuilder flowProcessDefinitionExtDO = FlowProcessDefinitionExtDO.builder();
        CscpFlowProcessDefExt cscpFlowProcessDefExt = new CscpFlowProcessDefExt();
        cscpFlowProcessDefExt.setModelId( bean.getModelId() );
        cscpFlowProcessDefExt.setProcessDefinitionId( bean.getDescription() );
        cscpFlowProcessDefExt.setFormId( bean.getFormId() );
//        List<String> list = bean.getFormFields();
//        if ( list != null ) {
//            flowProcessDefinitionExtDO.formFields( new ArrayList<String>( list ) );
//        }
//        flowProcessDefinitionExtDO.formCustomCreatePath( bean.getFormCustomCreatePath() );
//        flowProcessDefinitionExtDO.formCustomViewPath( bean.getFormCustomViewPath() );

        return cscpFlowProcessDefExt;
    }


    public static FlowProcessDefResp convert3(ProcessDefinition bean) {
        if ( bean == null ) {
            return null;
        }

        FlowProcessDefResp bpmProcessDefinitionRespVO = new FlowProcessDefResp();

        bpmProcessDefinitionRespVO.setSuspensionState( convertSuspendedToSuspensionState( bean.isSuspended() ) );
        bpmProcessDefinitionRespVO.setId( bean.getId() );
        bpmProcessDefinitionRespVO.setVersion( bean.getVersion() );
        bpmProcessDefinitionRespVO.setName( bean.getName() );
        bpmProcessDefinitionRespVO.setDescription( bean.getDescription() );
        bpmProcessDefinitionRespVO.setCategory( bean.getCategory() );

        return bpmProcessDefinitionRespVO;
    }

    @Named("convertSuspendedToSuspensionState")
    public static Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() :
                SuspensionState.ACTIVE.getStateCode();
    }


    public static void copyTo(CscpFlowProcessDefExt from, FlowProcessDefResp to) {
        if ( from == null ) {
            return;
        }
        to.setDescription( from.getDescription() );
        to.setFormId( from.getFormId() );
    }


}
