package com.ctsi.flow.param.convert;

import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.param.FlowModelBase;
import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.param.request.FlowModeImportReq;
import com.ctsi.flow.param.request.FlowModelCreateReq;
import com.ctsi.flow.param.request.FlowModelUpdateReq;
import com.ctsi.flow.param.request.FlowProcessDefCreateReq;
import com.ctsi.flow.param.response.FlowModelMetaInfoResp;
import com.ctsi.flow.param.response.FlowModelPageItemResp;
import com.ctsi.flow.param.response.FlowModelResp;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.flow.util.JsonUtils;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 16:47
 * @description ：流程模型转换
 */
public class FlowModelConvert {

    public static List<FlowModelPageItemResp> convertList(List<Model> list, Map<Long, FlowFormDO> formMap,
                                                          Map<String, Deployment> deploymentMap,
                                                          Map<String, ProcessDefinition> processDefinitionMap) {
        return CollectionUtils.convertList(list, model -> {
            FlowModelMetaInfoResp metaInfo = JsonUtils.parseObject(model.getMetaInfo(), FlowModelMetaInfoResp.class);
            FlowFormDO form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            Integer backState = metaInfo != null && metaInfo.getBackState() != null ? metaInfo.getBackState() : 0;
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            return convert(model, form, deployment, processDefinition, backState);
        });
    }

    public static FlowModelPageItemResp convert(Model model, FlowFormDO form, Deployment deployment, ProcessDefinition processDefinition,
                                                Integer backState) {
        FlowModelPageItemResp modelRespVO = new FlowModelPageItemResp();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(model.getCreateTime());
        modelRespVO.setBackState(backState);
        // 通用 copy
        copyTo(model, modelRespVO);
        // Form
        if (form != null) {
            modelRespVO.setFormId(form.getId());
            modelRespVO.setFormName(form.getName());
            modelRespVO.setFormType(form.getFormType());
        }
        // ProcessDefinition
        FlowModelPageItemResp.ProcessDefinition processDef = new FlowModelPageItemResp.ProcessDefinition();
        if (processDefinition != null) {
            processDef.setId(processDefinition.getId());
            processDef.setVersion(processDefinition.getVersion());
            modelRespVO.setProcessDefinition(processDef);
        }
        if (modelRespVO.getProcessDefinition() != null) {
            modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.isSuspended() ?
                    SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
            modelRespVO.getProcessDefinition().setDeploymentTime(deployment.getDeploymentTime());
        }
        return modelRespVO;
    }

    public static FlowProcessDefCreateReq convert2(Model model, FlowFormDO form) {
        FlowProcessDefCreateReq createReqDTO = new FlowProcessDefCreateReq();
        createReqDTO.setModelId(model.getId());
        createReqDTO.setName(model.getName());
        createReqDTO.setKey(model.getKey());
        createReqDTO.setCategory(model.getCategory());
        FlowModelMetaInfoResp metaInfo = JsonUtils.parseObject(model.getMetaInfo(), FlowModelMetaInfoResp.class);
        // metaInfo
        copyTo(metaInfo, createReqDTO);
        // form
        if (form != null) {
            createReqDTO.setFormConf(form.getConf());
            createReqDTO.setFormFields(form.getFields());
        }
        return createReqDTO;
    }

    public static void copyTo(FlowModelMetaInfoResp from, FlowProcessDefCreateReq to) {

        to.setDescription(from.getDescription());
        to.setFormId(from.getFormId());
    }

    public static FlowModelResp convert(Model model) {

        FlowModelResp modelRespVO = new FlowModelResp();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(model.getCreateTime());
        // 通用 copy
        copyTo(model, modelRespVO);
        return modelRespVO;
    }

    public static FlowModelCreateReq convert(FlowModeImportReq bean) {

        FlowModelCreateReq bpmModelCreateReqVO = new FlowModelCreateReq();
//        bpmModelCreateReqVO.setKey( bean.getKey() );
//        bpmModelCreateReqVO.setName( bean.getName() );
        bpmModelCreateReqVO.setDescription(bean.getDescription());
        return bpmModelCreateReqVO;
    }


    public static void copyTo(Model model, FlowModelBase to) {

        to.setName(model.getName());
        to.setKey(model.getKey());
        // metaInfo
        FlowModelMetaInfoResp metaInfo = JsonUtils.parseObject(model.getMetaInfo(), FlowModelMetaInfoResp.class);

        to.setDescription(metaInfo.getDescription());
        to.setFormId(metaInfo.getFormId());

    }

    public static void copy(Model model, FlowModelCreateReq bean) {
        model.setName(bean.getName());
        model.setKey(bean.getKey());
        model.setMetaInfo(buildMetaInfoStr(null, bean.getDescription(), null, null));
    }

    public static void copy(Model model, FlowModeImportReq bean) {
        model.setMetaInfo(buildMetaInfoStr(null, bean.getDescription(), null, null));
    }

    public static void copy(Model model, FlowModelUpdateReq bean) {
        model.setName(bean.getName());
        model.setMetaInfo(buildMetaInfoStr(JsonUtils.parseObject(model.getMetaInfo(), FlowModelMetaInfoResp.class),
                bean.getDescription(), bean.getFormId(), bean.getBackState()));
    }

    public static String buildMetaInfoStr(FlowModelMetaInfoResp metaInfo, String description, Long formId, Integer backState) {
        if (metaInfo == null) {
            metaInfo = new FlowModelMetaInfoResp();
        }
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StrUtil.isNotEmpty(description)) {
            metaInfo.setDescription(description);
        }
        if (Objects.nonNull(formId)) {
            metaInfo.setFormId(formId);
        }
        if (Objects.nonNull(backState)) {
            metaInfo.setBackState(backState);
        }
        return JsonUtils.toJsonString(metaInfo);
    }


}
