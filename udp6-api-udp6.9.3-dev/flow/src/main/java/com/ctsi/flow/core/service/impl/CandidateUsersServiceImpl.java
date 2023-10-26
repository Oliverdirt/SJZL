package com.ctsi.flow.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.ctsi.flow.core.behavior.script.FlowTaskAssignScript;
import com.ctsi.flow.core.service.CandidateUsersService;
import com.ctsi.flow.enums.FlowTaskAssignRuleTypeEnum;
import com.ctsi.flow.param.model.CscpFlowTaskAssignRule;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.flow.util.StringExtUtil;
import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.ctsi.flow.constant.ErrorCodeConstants.TASK_ASSIGN_SCRIPT_NOT_EXISTS;
import static com.ctsi.flow.util.RunFlowExceptionUtil.exception;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-10-24 14:45
 * @description ：
 */

@Service
@Slf4j
public class CandidateUsersServiceImpl implements CandidateUsersService {

    @Resource
    private CscpRolesService cscpRolesService;

    @Resource
    private CscpUserDetailService cscpUserDetailService;

    /**
     * 任务分配脚本
     */
    @Resource
    @Lazy
    private List<FlowTaskAssignScript> scripts;


    @Override
    public Set<Long> calculateTaskCandidateUsers(String processId, CscpFlowTaskAssignRule rule) {
        Set<Long> assigneeUserIds = null;

        if (Objects.equals(FlowTaskAssignRuleTypeEnum.USER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUser(rule);
        } else if (Objects.equals(FlowTaskAssignRuleTypeEnum.ROLE.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByRole(rule);
        } else if (Objects.equals(FlowTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptMember(rule);
        } else if (Objects.equals(FlowTaskAssignRuleTypeEnum.SCRIPT.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByScript(processId, rule);
        }
        return assigneeUserIds;
    }

    /**
     * @Description: 根据自定义脚本计算任务节点候选人
     * @Author: sunsheng
     **/
    private Set<Long> calculateTaskCandidateUsersByScript(String processId, CscpFlowTaskAssignRule rule) {
        String beanOptions = rule.getOptions();
        Set<String> scriptIds = StringExtUtil.convertToSet(beanOptions);
        // 获得对应的脚本
        Map<String, FlowTaskAssignScript> scriptMap = CollectionUtils.convertMap(scripts, script -> script.getEnum().getId());
        List<FlowTaskAssignScript> scripts = new ArrayList<>(scriptIds.size());
        scriptIds.forEach(id -> {
            FlowTaskAssignScript script = scriptMap.get(id);
            if (script == null) {
                throw exception(TASK_ASSIGN_SCRIPT_NOT_EXISTS, id);
            }
            scripts.add(script);
        });
        // 逐个计算任务
        Set<Long> userIds = new HashSet<>();
        scripts.forEach(script -> CollUtil.addAll(userIds, script.calculateTaskCandidateUsers(processId)));
        return userIds;
    }

    /**
     * @Description: 计算任务节点候选人
     * @Author: sunsheng
     **/
    private Set<Long> calculateTaskCandidateUser(CscpFlowTaskAssignRule rule) {
        String beanOptions = rule.getOptions();
        Set<String> userIds = StringExtUtil.convertToSet(beanOptions);
        Set<Long> idSet = userIds.stream().map(e -> Long.parseLong(e.trim())).collect(Collectors.toSet());
        return idSet;
    }

    /**
     * @Description: 根据角色计算任务节点候选人
     * @Author: sunsheng
     **/
    private Set<Long> calculateTaskCandidateUsersByRole(CscpFlowTaskAssignRule rule) {
        String beanOptions = rule.getOptions();
        Set<String> roleIds = StringExtUtil.convertToSet(beanOptions);
        Set<Long> idSet = roleIds.stream().map(e -> Long.parseLong(e.trim())).collect(Collectors.toSet());
        List<CscpUser> usersByRoleIds = cscpRolesService.getUsersByRoleIds(idSet);
        Set<Long> idSets = usersByRoleIds.stream().map(e -> e.getId()).collect(Collectors.toSet());
        return idSets;
    }

    /**
     * @Description: 部门人员配置规则
     * @Author: sunsheng
     **/
    private Set<Long> calculateTaskCandidateUsersByDeptMember(CscpFlowTaskAssignRule rule) {
        String beanOptions = rule.getOptions();
        Set<String> deptIds = StringExtUtil.convertToSet(beanOptions);
        Set<Long> idSet = deptIds.stream().map(e -> Long.parseLong(e.trim())).collect(Collectors.toSet());
        List<CscpUser> users = cscpUserDetailService.getUsersByDeptIds(idSet);
        Set<Long> idSets = users.stream().map(e -> e.getId()).collect(Collectors.toSet());
        return idSets;
    }

    // 随机获取节点审批人员
    private Long chooseTaskAssignee(Set<Long> candidateUserIds) {
        int index = RandomUtil.randomInt(candidateUserIds.size());
        return CollUtil.get(candidateUserIds, index);
    }
}
