package com.ctsi.flow.param.convert;

import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;
import com.ctsi.flow.param.model.FlowTaskAssignRuleDO;
import com.ctsi.flow.param.request.FlowTaskAssignRuleCreateReq;
import com.ctsi.flow.param.request.FlowTaskAssignRuleUpdateReq;
import com.ctsi.flow.param.response.FlowTaskRuleResp;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.ssdc.security.SecurityHxUtils;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.ctsi.flow.util.CollectionUtils.convertMap;

public class FlowTaskAssignRuleConvert {

    public static List<FlowTaskRuleResp> convertList(List<UserTask> tasks, List<CscpFlowTaskAssignRule> rules) {
        Map<String, CscpFlowTaskAssignRule> ruleMap = convertMap(rules, CscpFlowTaskAssignRule::getTaskDefinitionKey);
        // 以 UserTask 为主维度，原因是：流程图编辑后，一些规则实际就没用了。
        return CollectionUtils.convertList(tasks, task -> {
            FlowTaskRuleResp respVO = convert(ruleMap.get(task.getId()));
            if (respVO == null) {
                respVO = new FlowTaskRuleResp();
                respVO.setTaskDefinitionKey(task.getId());
            }
            respVO.setTaskDefinitionName(task.getName());
            return respVO;
        });
    }

    public static FlowTaskRuleResp convert(CscpFlowTaskAssignRule bean){
        if ( bean == null ) {
            return null;
        }

        FlowTaskRuleResp flowTaskRuleResp = new FlowTaskRuleResp();

        flowTaskRuleResp.setType( bean.getType() );
        String beanOptions = bean.getOptions();
        beanOptions = beanOptions.replace("[","").replace("]","");
        if (StringUtils.isBlank(beanOptions)) {
            flowTaskRuleResp.setOptions( new HashSet<Long>() );
        } else {
            String[] split = beanOptions.split(",");
            Set<Long> set = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toSet());
            if ( set != null ) {
                flowTaskRuleResp.setOptions( new HashSet<Long>( set ) );
            }
        }
        flowTaskRuleResp.setId( bean.getId() );
        flowTaskRuleResp.setModelId( bean.getModelId() );
        flowTaskRuleResp.setProcessDefinitionId( bean.getProcessDefinitionId() );
        flowTaskRuleResp.setTaskDefinitionKey( bean.getTaskDefinitionKey() );

        return flowTaskRuleResp;

    };

    public static CscpFlowTaskAssignRule convert(FlowTaskAssignRuleCreateReq bean) {
        if ( bean == null ) {
            return null;
        }

        CscpFlowTaskAssignRule rule = new CscpFlowTaskAssignRule();

        rule.setModelId(bean.getModelId());
        rule.setTaskDefinitionKey(bean.getTaskDefinitionKey());
        rule.setType(bean.getType());
        rule.setTenantId(SecurityHxUtils.getCurrentTenantId());
        Set<Long> set = bean.getOptions();
        if ( set != null ) {
            rule.setOptions(set.toString());
        }

        return rule;
    };

    public static CscpFlowTaskAssignRule convert(CscpFlowTaskAssignRule existRule, FlowTaskAssignRuleUpdateReq bean) {
        if ( bean == null ) {
            return null;
        }
//        BpmTaskAssignRule rule = new BpmTaskAssignRule();
//        FlowTaskAssignRuleDO.FlowTaskAssignRuleDOBuilder bpmTaskAssignRuleDO = FlowTaskAssignRuleDO.builder();

        existRule.setId(bean.getId());
        existRule.setType(bean.getType());
        Set<Long> set = bean.getOptions();
        if ( set != null ) {
            existRule.setOptions(set.toString());
        }

        return existRule;
    };

    static List<FlowTaskAssignRuleDO> convertList2(List<FlowTaskRuleResp> list) {
        if ( list == null ) {
            return null;
        }

        List<FlowTaskAssignRuleDO> list1 = new ArrayList<FlowTaskAssignRuleDO>( list.size() );
        for ( FlowTaskRuleResp bpmTaskAssignRuleRespVO : list ) {
            list1.add( bpmTaskAssignRuleRespVoToBpmTaskAssignRuleDO( bpmTaskAssignRuleRespVO ) );
        }

        return list1;
    };

    protected static FlowTaskAssignRuleDO bpmTaskAssignRuleRespVoToBpmTaskAssignRuleDO(FlowTaskRuleResp bpmTaskAssignRuleRespVO) {
        if ( bpmTaskAssignRuleRespVO == null ) {
            return null;
        }

        FlowTaskAssignRuleDO.FlowTaskAssignRuleDOBuilder bpmTaskAssignRuleDO = FlowTaskAssignRuleDO.builder();

        bpmTaskAssignRuleDO.id( bpmTaskAssignRuleRespVO.getId() );
        bpmTaskAssignRuleDO.modelId( bpmTaskAssignRuleRespVO.getModelId() );
        bpmTaskAssignRuleDO.processDefinitionId( bpmTaskAssignRuleRespVO.getProcessDefinitionId() );
        bpmTaskAssignRuleDO.taskDefinitionKey( bpmTaskAssignRuleRespVO.getTaskDefinitionKey() );
        bpmTaskAssignRuleDO.type( bpmTaskAssignRuleRespVO.getType() );
        Set<Long> set = bpmTaskAssignRuleRespVO.getOptions();
        if ( set != null ) {
            bpmTaskAssignRuleDO.options( new HashSet<Long>( set ) );
        }

        return bpmTaskAssignRuleDO.build();
    }

}
