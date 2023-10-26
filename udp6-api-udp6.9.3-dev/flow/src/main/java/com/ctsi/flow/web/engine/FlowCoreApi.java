package com.ctsi.flow.web.engine;

import com.ctsi.flow.param.FlowOutLine;
import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.FlowMyProcessInstanceResp;
import com.ctsi.flow.param.response.FlowTaskDonePageItemResp;
import com.ctsi.flow.param.response.FlowTaskResp;
import com.ctsi.flow.param.response.FlowTaskTodoPageItemResp;
import com.ctsi.flow.server.engine.FlowTaskService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-07 15:25
 * @description ：
 */

@Api(tags = "工作流-流程操作")
@RestController
@RequestMapping("/api/flow/core")
public class FlowCoreApi {

    @Resource
    private FlowTaskService taskService;

    @GetMapping("todo-page")
    @ApiOperation("获取 Todo 待办任务分页")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getTodoTaskPage")
    @Log
    public Response<PageInfo<FlowTaskTodoPageItemResp>> getTodoTaskPage(@Valid FlowTaskTodoPageReq page) {

        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        PageInfo<FlowTaskTodoPageItemResp> todoTaskPage = taskService.getTodoTaskPage(currentUserId, page);
        return Response.ok(todoTaskPage);
    }

    @GetMapping("done-page")
    @ApiOperation("获取 Done 已办任务分页")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getDoneTaskPage")
    @Log
    public Response<PageInfo<FlowTaskDonePageItemResp>> getDoneTaskPage(@Valid FlowTaskDonePageReq page) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        PageInfo<FlowTaskDonePageItemResp> downTaskPage = taskService.getDoneTaskPage(currentUserId, page);
        return Response.ok(downTaskPage);
    }

    @GetMapping("my-process")
    @ApiOperation("我的流程")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getMyProcessPage")
    @Log
    public Response<PageInfo<FlowMyProcessInstanceResp>> getMyProcessPage(@Valid FlowTaskDonePageReq page) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        PageInfo<FlowMyProcessInstanceResp> downTaskPage = taskService.getMyProcessPage(currentUserId, page);
        return Response.ok(downTaskPage);
    }

    @PutMapping("/approve")
    @ApiOperation("通过任务")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "approveTask")
    @Log
    public Response<Boolean> approveTask(@Valid @RequestBody FlowTaskApproveReq req) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        String procId = taskService.approveTask(currentUserId, req);
        taskService.nextAssigner(req.getNextHandler(),procId);
        return Response.ok();
    }


    @PutMapping("/update-assignee")
    @ApiOperation(value = "更新任务的负责人", notes = "用于【流程详情】的【转派】按钮")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "updateTaskAssignee")
    @Log
    public Response<Boolean> updateTaskAssignee(@Valid @RequestBody FlowTaskUpdateAssigneeReq req) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        taskService.updateTaskAssignee(currentUserId, req);
        return Response.ok();
    }

    @GetMapping("/list-by-process-instance-id")
    @ApiOperation(value = "获得指定流程实例的任务列表", notes = "包括完成的、未完成的")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getTaskListByProcessInstanceId")
    @Log
    public Response<List<FlowTaskResp>> getTaskListByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId) {
        List<FlowTaskResp> taskListByProcessInstanceId = taskService.getTaskListByProcessInstanceId(processInstanceId);
        return Response.ok(taskListByProcessInstanceId);
    }

    @GetMapping("/get-out-line")
    @ApiOperation(value = "获取下一步出口路线", notes = "出口选项")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getOutLines")
    @Log
    public Response<List<FlowOutLine>> getOutLines(@RequestParam("taskId") String taskId) {
        List<FlowOutLine> outLines = taskService.getOutLines(taskId);
        return Response.ok(outLines);
    }

    @GetMapping("/backProcess")
    @ApiOperation(value = "任务收回", notes = "处理过的任务收回到当前节点")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "backProcess")
    @Log
    public Response<Void> backProcess(@RequestParam("processInstanceId") String processInstanceId) {
        return Response.ok(taskService.backProcess(processInstanceId));
    }

    @GetMapping("/endProcess")
    @ApiOperation(value = "流程终止", notes = "终止当前流程")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "endProcess")
    @Log
    public Response<Void> endProcess(@RequestParam("processInstanceId") String processInstanceId) {
        return Response.ok(taskService.endProcess(processInstanceId));
    }

    @PostMapping("/claim/{taskId}")
    @ApiOperation("签收任务")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "claimTask")
    @Log
    public Response<Boolean> claimTask(@PathVariable("taskId") String taskId) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        taskService.claimTask(currentUserId, taskId);
        return Response.ok();
    }

}
