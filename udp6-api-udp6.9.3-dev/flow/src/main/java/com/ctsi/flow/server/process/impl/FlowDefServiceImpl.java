package com.ctsi.flow.server.process.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ctsi.flow.constant.ErrorCodeConstants;
import com.ctsi.flow.param.convert.FlowProcessDefinitionConvert;
import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpFlowProcessDefExtExample;
import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.param.request.FlowProcessDefCreateReq;
import com.ctsi.flow.param.request.FlowProcessDefListReq;
import com.ctsi.flow.param.request.FlowProcessDefPageReq;
import com.ctsi.flow.param.response.FlowProcessDefPageItemResp;
import com.ctsi.flow.param.response.FlowProcessDefResp;
import com.ctsi.flow.repository.CscpFlowProcessDefExtRepository;
import com.ctsi.flow.server.form.FlowFormService;
import com.ctsi.flow.server.process.FlowDefService;
import com.ctsi.flow.util.RunFlowExceptionUtil;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.util.io.BytesStreamSource;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.*;

import static com.ctsi.flow.constant.ErrorCodeConstants.PROCESS_DEFINITION_KEY_NOT_MATCH;
import static com.ctsi.flow.util.CollectionUtils.*;
import static java.util.Collections.emptyList;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-14 15:49
 * @description ：
 */

@Service
@Validated
@Slf4j
public class FlowDefServiceImpl implements FlowDefService {


    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private FlowFormService formService;

    @Resource
    private CscpFlowProcessDefExtRepository cscpFlowProcessDefExtRepository;

    @Override
    public PageResult<FlowProcessDefPageItemResp> getProcessDefinitionPage(FlowProcessDefPageReq pageVO) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StrUtil.isNotBlank(pageVO.getKey())) {
            definitionQuery.processDefinitionKey(pageVO.getKey());
        }
        // 执行查询
        List<ProcessDefinition> processDefinitions = definitionQuery.orderByProcessDefinitionVersion().desc()
                .listPage(pageVO.getPage(), pageVO.getSize());
        if (CollUtil.isEmpty(processDefinitions)) {

            return new PageResult<>(emptyList(), definitionQuery.count(), definitionQuery.count());
        }

        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        processDefinitions.forEach(definition -> addIfNotNull(deploymentIds, definition.getDeploymentId()));
        Map<String, Deployment> deploymentMap = getDeploymentMap(deploymentIds);

        // 获得 FlowProcessDefinitionExtDO Map
        List<String> proDefIds = convertList(processDefinitions, ProcessDefinition::getId);
        List<CscpFlowProcessDefExt> processDefinitionDos =  cscpFlowProcessDefExtRepository.selectListByProcessDefinitionIds(proDefIds);
//        List<FlowProcessDefinitionExtDO> processDefinitionDOs = processDefinitionMapper.selectListByProcessDefinitionIds(proDefIds);
        Map<String, CscpFlowProcessDefExt> processDefinitionDoMap = convertMap(processDefinitionDos,
                CscpFlowProcessDefExt::getProcessDefinitionId);

        // 获得 Form Map
        Set<Long> formIds = convertSet(processDefinitionDos, CscpFlowProcessDefExt::getFormId);
        Map<Long, FlowFormDO> formMap = formService.getFormMap(formIds);

        // 拼接结果
        long definitionCount = definitionQuery.count();
        return new PageResult<>(FlowProcessDefinitionConvert.convertList(processDefinitions, deploymentMap,
                processDefinitionDoMap, formMap), definitionCount, definitionCount);
    }


    @Override
    public List<FlowProcessDefResp> getProcessDefinitionList(FlowProcessDefListReq listReqVO) {
        // 拼接查询条件
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.suspended();
        } else if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), listReqVO.getSuspensionState())) {
            definitionQuery.active();
        }
        // 执行查询
        List<ProcessDefinition> processDefinitions = definitionQuery.list();
        if (CollUtil.isEmpty(processDefinitions)) {
            return Collections.emptyList();
        }

        // 获得 FlowProcessDefinitionExtDO Map
//        List<CscpFlowProcessDefExt> processDefinitionDOs = processDefinitionMapper.selectListByProcessDefinitionIds(
//                convertList(processDefinitions, ProcessDefinition::getId));
        List<String> proDefIds = convertList(processDefinitions, ProcessDefinition::getId);
        List<CscpFlowProcessDefExt> processDefinitionDos =  cscpFlowProcessDefExtRepository.selectListByProcessDefinitionIds(proDefIds);
        Map<String, CscpFlowProcessDefExt> processDefinitionDoMap = convertMap(processDefinitionDos,
                CscpFlowProcessDefExt::getProcessDefinitionId);
        // 执行查询，并返回
        return FlowProcessDefinitionConvert.convertList3(processDefinitions, processDefinitionDoMap);
    }

    @Override
    public String getProcessDefinitionBpmnXml(String id) {

        CscpFlowProcessDefExtExample extExample = new CscpFlowProcessDefExtExample();
        extExample.or().andProcessDefinitionIdEqualTo(id);
        // 只会查询到一条数据
        List<CscpFlowProcessDefExt> cscpFlowProcessDefs = cscpFlowProcessDefExtRepository.selectByExample(extExample);

        byte[] modelEditorSource = repositoryService.getModelEditorSource(cscpFlowProcessDefs.get(0).getModelId());
        if (modelEditorSource == null) {
            return null;
        }
        return StrUtil.utf8Str(modelEditorSource);
    }

    @Override
    public boolean isProcessDefinitionEquals(@Valid FlowProcessDefCreateReq createReqDTO) {
        // 校验 name、description 是否更新
        ProcessDefinition oldProcessDefinition = getActiveProcessDefinition(createReqDTO.getKey());
        if (oldProcessDefinition == null) {
            return false;
        }
        CscpFlowProcessDefExt oldProcessDefinitionExt = getProcessDefinitionExt(oldProcessDefinition.getId());
        if (!StrUtil.equals(createReqDTO.getName(), oldProcessDefinition.getName())
                || !StrUtil.equals(createReqDTO.getDescription(), oldProcessDefinitionExt.getDescription())
                || !StrUtil.equals(createReqDTO.getCategory(), oldProcessDefinition.getCategory())) {
            return false;
        }
        // 校验 form 信息是否更新
        if ( !ObjectUtil.equal(createReqDTO.getFormId(), oldProcessDefinitionExt.getFormId())) {
            return false;
        }
        // 校验 BPMN XML 信息
        BpmnModel newModel = buildBpmnModel(createReqDTO.getBpmnBytes());
        BpmnModel oldModel = getBpmnModel(oldProcessDefinition.getId());
        if (!equals(oldModel, newModel)) {
            return false;
        }
        // 最终发现都一致，则返回 true
        return true;
    }

    /**
     * 构建对应的 BPMN Model
     *
     * @param bpmnBytes 原始的 BPMN XML 字节数组
     * @return BPMN Model
     */
    public  BpmnModel buildBpmnModel(byte[] bpmnBytes) {
        // 转换成 BpmnModel 对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(new BytesStreamSource(bpmnBytes), true, true);
    }

    public  boolean equals(BpmnModel oldModel, BpmnModel newModel) {
        // 由于 BpmnModel 未提供 equals 方法，所以只能转成字节数组，进行比较
        return Arrays.equals(getBpmnBytes(oldModel), getBpmnBytes(newModel));
    }

    @Override
    public BpmnModel getBpmnModel(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String id) {
        return repositoryService.getProcessDefinition(id);
    }

    @Override
    public ProcessDefinition getProcessDefinition2(String id) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
    }

    @Override
    public ProcessDefinition getActiveProcessDefinition(String key) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).active().singleResult();
    }

    @Override
    public CscpFlowProcessDefExt getProcessDefinitionExt(String processDefinitionId) {

        CscpFlowProcessDefExt cscpFlowProcessDefExt =  cscpFlowProcessDefExtRepository.selectByProcessDefinitionId(processDefinitionId);
        return  cscpFlowProcessDefExt;
//        return processDefinitionMapper.selectByProcessDefinitionId(id);
    }

    @Override
    public Deployment getDeployment(String id) {
        if (StrUtil.isEmpty(id)) {
            return null;
        }
        return repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
    }

    @Override
    public List<Deployment> getDeployments(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return emptyList();
        }
        List<Deployment> list = new ArrayList<>(ids.size());
        for (String id : ids) {
            addIfNotNull(list, getDeployment(id));
        }
        return list;
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return null;
        }
        return repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds) {
        if (CollUtil.isEmpty(deploymentIds)) {
            return emptyList();
        }
        return repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
    }


    @Override
    @Transactional(rollbackFor = Exception.class) // 因为进行多个 activiti 操作，所以开启事务
    public String createProcessDefinition(FlowProcessDefCreateReq createReqDTO) {
        // 创建 Deployment 部署
        Deployment deploy = repositoryService.createDeployment()
                .key(createReqDTO.getKey()).name(createReqDTO.getName()).category(createReqDTO.getCategory())
                .addBytes(createReqDTO.getKey() + BPMN_FILE_SUFFIX, createReqDTO.getBpmnBytes())
                .deploy();

        // 设置 ProcessDefinition 的 category 分类
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), createReqDTO.getCategory());
        // 注意 1，ProcessDefinition 的 key 和 name 是通过 BPMN 中的 <bpmn2:process /> 的 id 和 name 决定
        // 注意 2，目前该项目的设计上，需要保证 Model、Deployment、ProcessDefinition 使用相同的 key，保证关联性。
        //          否则，会导致 ProcessDefinition 的分页无法查询到。
        if (!Objects.equals(definition.getKey(), createReqDTO.getKey())) {
            throw RunFlowExceptionUtil.exception(PROCESS_DEFINITION_KEY_NOT_MATCH, createReqDTO.getKey(), definition.getKey());
        }
        if (!Objects.equals(definition.getName(), createReqDTO.getName())) {
            throw RunFlowExceptionUtil.exception(ErrorCodeConstants.PROCESS_DEFINITION_NAME_NOT_MATCH, createReqDTO.getName(), definition.getName());
        }

        // 插入拓展表
        CscpFlowProcessDefExt definitionDO = FlowProcessDefinitionConvert.convert2(createReqDTO);
        definitionDO.setProcessDefinitionId(definition.getId());
        definitionDO.setCreator(SecurityHxUtils.getCurrentUserId()+"");
        definitionDO.setCreateTime(ZonedDateTime.now());
        cscpFlowProcessDefExtRepository.insert(definitionDO);
//        processDefinitionMapper.insert(definitionDO);
        return definition.getId();
    }

    @Override
    public void updateProcessDefinitionState(String id, Integer state) {
        // 激活
        if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), state)) {
            repositoryService.activateProcessDefinitionById(id, false, null);
            return;
        }
        // 挂起
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), state)) {
            // suspendProcessInstances = false，进行中的任务，不进行挂起。
            // 原因：只要新的流程不允许发起即可，老流程继续可以执行。
            repositoryService.suspendProcessDefinitionById(id, false, null);
            return;
        }
        log.error("[updateProcessDefinitionState][流程定义({}) 修改未知状态({})]", id, state);
    }

    public  byte[] getBpmnBytes(BpmnModel model) {
        if (model == null) {
            return new byte[0];
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToXML(model);
    }


}
