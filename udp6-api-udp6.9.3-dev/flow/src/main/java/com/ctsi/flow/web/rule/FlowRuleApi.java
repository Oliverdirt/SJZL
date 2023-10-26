package com.ctsi.flow.web.rule;

import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.model.ActOperatePerm;
import com.ctsi.flow.param.request.FlowTaskAssignRuleCreateReq;
import com.ctsi.flow.param.request.FlowTaskAssignRuleUpdateReq;
import com.ctsi.flow.param.response.FlowTaskRuleResp;
import com.ctsi.flow.server.rule.FlowRuleService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 9:42
 * @description ：流程规则API接口
 */

@Api(tags = "工作流-流程规则")
@RestController
@RequestMapping("/api/flow/rule")
public class FlowRuleApi {
    @Resource
    private FlowRuleService ruleService;

    @GetMapping("/list")
    @ApiOperation(value = "获得任务分配规则列表")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "getTaskRuleList")
    @Log
    public Response<List<FlowTaskRuleResp>> getTaskRuleList(
            @RequestParam(value = "modelId", required = false) String modelId,
            @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId) {
        List<FlowTaskRuleResp> ruleList = ruleService.getTaskAssignRuleList(modelId, processDefinitionId);
        return Response.ok(ruleList);
    }

    /*人员规则开始*/
    @GetMapping("/list-rule-all")
    @ApiOperation(value = "获取人员规则选项")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "getAssignRuleList")
    @Log
    public Response<List<FlowTaskRuleResp>> getAssignRuleList() {
        return Response.ok();
    }

    @PostMapping("/assign-create")
    @ApiOperation(value = "创建任务分配规则")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "createTaskAssignRule")
    @Log
    public Response<Long> createTaskAssignRule(@Valid @RequestBody FlowTaskAssignRuleCreateReq reqVO) {
        Long ruleId = ruleService.createTaskAssignRule(reqVO);
        return Response.ok(ruleId);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新任务分配规则")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "updateTaskAssignRule")
    @Log
    public Response<Boolean> updateTaskAssignRule(@Valid @RequestBody FlowTaskAssignRuleUpdateReq reqVO) {
        ruleService.updateTaskAssignRule(reqVO);
        return Response.ok();
    }

    /*人员规则结束*/


    /*节点操作规则开始*/

    @PostMapping("/taskPerm-update")
    @ApiOperation(value = "修改任务操作权限")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "updateTaskPerm")
    @Log
    public Response<Boolean> updateTaskPerm(@RequestBody ActOperatePerm actOperatePerm) {
        ruleService.updateTaskPerm(actOperatePerm);
        return Response.ok();
    }

    @GetMapping("/taskPerm-select")
    @ApiOperation(value = "查询任务操作权限")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "selectTaskPerm")
    @Log
    public Response selectTaskPerm(ActOperatePerm actOperatePerm) {
        return Response.ok(ruleService.selectTaskPerm(actOperatePerm));
    }

    /*节点操作规则结束*/


    /*表单字段规则开始*/

    /*表单字段规则结束*/
}
