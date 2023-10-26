package com.ctsi.flow.server.engine.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.param.convert.FlowModelConvert;
import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.FlowModelMetaInfoResp;
import com.ctsi.flow.param.response.FlowModelPageItemResp;
import com.ctsi.flow.param.response.FlowModelResp;
import com.ctsi.flow.server.engine.FlowBpmnService;
import com.ctsi.flow.server.form.FlowFormService;
import com.ctsi.flow.server.process.FlowDefService;
import com.ctsi.flow.server.rule.FlowRuleService;
import com.ctsi.flow.util.*;
import com.ctsi.ssdc.gen.service.CscpCustomizeVformService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.util.io.BytesStreamSource;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.ctsi.flow.constant.ErrorCodeConstants.*;
import static com.ctsi.flow.util.CollectionUtils.convertMap;
import static com.ctsi.flow.util.RunFlowExceptionUtil.exception;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 15:30
 * @description ：模型service层
 */

@Service
@Slf4j
public class FlowBpmnServiceImpl implements FlowBpmnService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private FlowDefService flowDefService;

    @Resource
    private FlowFormService flowFormService;

    @Resource
    private FlowRuleService flowRuleService;

    @Resource
    private CscpCustomizeVformService cscpCustomizeVformService;

    @Resource(name = "flow-extra-executor")
    private ThreadPoolTaskExecutor flowExtraExecutor;

    @Override
    public String createModel(FlowModelCreateReq createRet, String bpmnXml) {

        checkKeyNcName(createRet.getKey());
        // 校验流程标识已经存在
        Model keyModel = this.getModelByKey(createRet.getKey());
        if (keyModel != null) {
            throw exception(MODEL_KEY_EXISTS, createRet.getKey());
        }

        // 创建流程定义
        Model model = repositoryService.newModel();
        FlowModelConvert.copy(model, createRet);
        // 保存流程定义
        repositoryService.saveModel(model);
        // 保存 BPMN XML
        saveModelBpmnXml(model, bpmnXml);
        return model.getId();

    }

    @Override
    public String importModel(FlowModeImportReq importReq, InputStream inputStream) {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xtr = null;
        try {
            xtr = xif.createXMLStreamReader(inputStream);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            String key = bpmnModel.getProcesses().get(0).getId();
            String name = bpmnModel.getProcesses().get(0).getName();
            checkKeyNcName(key);
            String bpmnXml = IoUtils.readUtf8(importReq.getBpmnFile().getInputStream());
            // 校验流程标识已经存在
            Model keyModel = this.getModelByKey(key);
            if (keyModel != null) {
                throw exception(MODEL_KEY_EXISTS, key);
            }
            // 创建流程定义
            Model model = repositoryService.newModel();
            model.setKey(key);
            model.setName(name);
            FlowModelConvert.copy(model, importReq);
            // 保存流程定义
            repositoryService.saveModel(model);
            // 保存 BPMN XML
            saveModelBpmnXml(model, bpmnXml);
            return model.getId();
        } catch (XMLStreamException | IOException e) {
            throw exception(MODEL_FILE_WRONG);
        }
    }

    @Override
    public void deleteModel(String id) {

        // 校验流程模型存在
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 执行删除
        repositoryService.deleteModel(id);
        // 禁用流程实例
        updateProcessDefinitionSuspended(model.getDeploymentId());

    }

    @Override
    public void updateModel(@Valid FlowModelUpdateReq updateRet) {

        // 校验流程模型存在
        Model model = repositoryService.getModel(updateRet.getId());
        if (model == null) {
            throw exception(MODEL_NOT_EXISTS);
        }

        // 修改流程定义
        FlowModelConvert.copy(model, updateRet);
        // 更新模型
        repositoryService.saveModel(model);
        // 更新 BPMN XML
        saveModelBpmnXml(model, updateRet.getBpmnXml());

        // 更新表单的是否可删除状态
        CompletableFuture.runAsync(() ->
                cscpCustomizeVformService.updateDelFlagByFormId(updateRet.getFormId()), flowExtraExecutor);
    }

    @Override
    public PageInfo<FlowModelPageItemResp> getModelPage(FlowModelPageReq page) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (StrUtil.isNotBlank(page.getKey())) {
            modelQuery.modelKey(page.getKey());
        }
        if (StrUtil.isNotBlank(page.getName())) {
            modelQuery.modelNameLike("%" + page.getName() + "%"); // 模糊匹配
        }

        // 执行查询
        List<Model> models = modelQuery.orderByCreateTime().desc()
                .listPage(PageUtils.getStart(page), page.getSize());

        // 获得 Form Map
        Set<Long> formIds = CollectionUtils.convertSet(models, model -> {
            FlowModelMetaInfoResp metaInfo = JsonUtils.parseObject(model.getMetaInfo(), FlowModelMetaInfoResp.class);
            return metaInfo != null ? metaInfo.getFormId() : null;
        });
        Map<Long, FlowFormDO> formMap = flowFormService.getFormMap(formIds);

        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        models.forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = flowDefService.getDeploymentMap(deploymentIds);
        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = flowDefService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);

        // 拼接结果
        long modelCount = modelQuery.count();
        PageInfo<FlowModelPageItemResp> pageInfo = new PageInfo<>(FlowModelConvert.convertList(models, formMap, deploymentMap, processDefinitionMap));
        PageHelperTool.initPageInfoObj(page.getPage(), (int) modelCount, page.getSize(), pageInfo);
        return pageInfo;

    }

    @Override
    public FlowModelResp getModel(String id) {

        Model model = repositoryService.getModel(id);
        if (model == null) {
            return null;
        }
        FlowModelResp modelRespVO = FlowModelConvert.convert(model);
        // 拼接 bpmn XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        modelRespVO.setBpmnXml(StrUtil.utf8Str(bpmnBytes));
        return modelRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deployModel(String id) {

        // 校验流程模型存在
        Model model = repositoryService.getModel(id);
        if (ObjectUtils.isEmpty(model)) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 校验流程图
        byte[] bpmnBytes = repositoryService.getModelEditorSource(model.getId());
        if (bpmnBytes == null) {
            throw exception(MODEL_NOT_EXISTS);
        }

        // 校验任务分配规则已配置
//        flowRuleService.checkTaskAssignRuleAllConfig(id);

        // 校验模型是否发生修改。如果未修改，则不允许创建
        FlowProcessDefCreateReq flowProcessDefCreateReq = FlowModelConvert.convert2(model, null);
        flowProcessDefCreateReq.setBpmnBytes(bpmnBytes);
        if (flowDefService.isProcessDefinitionEquals(flowProcessDefCreateReq)) { // 流程定义的信息相等
            ProcessDefinition oldProcessInstance = flowDefService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
            // 校验任务分配规则已配置
            if (oldProcessInstance != null && flowRuleService.isTaskAssignRulesEquals(model.getId(), oldProcessInstance.getId())) {
                throw exception(MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS);
            }
        }
        // 创建流程定义
        String definitionId = flowDefService.createProcessDefinition(flowProcessDefCreateReq);

        // 将老的流程定义进行挂起。也就是说，只有最新部署的流程定义，才可以发起任务。
        updateProcessDefinitionSuspended(model.getDeploymentId());

        // 更新 model 的 deploymentId，进行关联
        ProcessDefinition definition = flowDefService.getProcessDefinition(definitionId);
        model.setDeploymentId(definition.getDeploymentId());
        repositoryService.saveModel(model);

        // 复制任务分配规则 todo
//         taskAssignRuleService.copyTaskAssignRules(id, definition.getId());

    }

    @Override
    public void updateModelState(String id, Integer state) {

        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 校验流程定义存在
        ProcessDefinition definition = flowDefService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }

        // 更新状态
        flowDefService.updateProcessDefinitionState(definition.getId(), state);

    }

    /**
     * 获得流程模型编号对应的 BPMN Model
     *
     * @param id 流程模型编号
     * @return BPMN Model
     */
    @Override
    public BpmnModel getBpmnModel(String id) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        if (ArrayUtil.isEmpty(bpmnBytes)) {
            return null;
        }
        return buildBpmnModel(bpmnBytes);
    }

    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = flowDefService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        if (oldDefinition.isSuspended()) {
            return;
        }
        flowDefService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery().modelKey(key).singleResult();
    }

    private void checkKeyNcName(String key) {
        if (!ValidationUtils.isXmlNcName(key)) {
            throw exception(MODEL_KEY_VALID);
        }
    }

    private void saveModelBpmnXml(Model model, String bpmnXml) {
        if (StrUtil.isEmpty(bpmnXml)) {
            return;
        }
        String xml = bpmnXml.replaceAll("><", ">\n<");
        repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(xml));
    }


    /**
     * 构建对应的 BPMN Model
     *
     * @param bpmnBytes 原始的 BPMN XML 字节数组
     * @return BPMN Model
     */
    public BpmnModel buildBpmnModel(byte[] bpmnBytes) {
        // 转换成 BpmnModel 对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }
}
