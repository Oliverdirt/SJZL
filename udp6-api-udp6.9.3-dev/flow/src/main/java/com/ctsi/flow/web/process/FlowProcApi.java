package com.ctsi.flow.web.process;

import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.request.FlowProcessInstanceCancelReq;
import com.ctsi.flow.param.request.FlowProcessInstanceCreateReq;
import com.ctsi.flow.param.response.FlowActivityResp;
import com.ctsi.flow.param.response.FlowProcessInstanceResp;
import com.ctsi.flow.server.process.FlowProcessInstanceService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.security.SecurityHxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-07 17:11
 * @description ：
 */

@Api(tags = "工作流-流程实例")
@RestController
@RequestMapping("/api/flow/process")
public class FlowProcApi {

    @Resource
    private FlowProcessInstanceService flowProcessInstanceService;

    @PostMapping("/create")
    @ApiOperation("新建流程实例")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "createProcessInstance")
    @Log
    public Response<String> createProcessInstance(@Valid @RequestBody FlowProcessInstanceCreateReq createReqVO) {
        Long userId = SecurityHxUtils.getCurrentUserId();
        String instanceId = flowProcessInstanceService.createProcessInstance(userId, createReqVO);
        return Response.ok(instanceId);
    }

    @DeleteMapping("/cancel")
    @ApiOperation(value = "取消流程实例", notes = "撤回发起的流程")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "cancelProcessInstance")
    @Log
    public Response<Boolean> cancelProcessInstance(@Valid @RequestBody FlowProcessInstanceCancelReq cancelReqVO) {
        Long userId = SecurityHxUtils.getCurrentUserId();
        flowProcessInstanceService.cancelProcessInstance(userId, cancelReqVO);
        return Response.ok(true);
    }


    @GetMapping("/get")
    @ApiOperation(value = "获得指定流程实例", notes = "在【流程详细】界面中，进行调用")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "getProcessInstance")
    @Log
    public Response<FlowProcessInstanceResp> getProcessInstance(@RequestParam("id") String id) {
        FlowProcessInstanceResp instance = flowProcessInstanceService.getProcessInstanceVO(id);
        return Response.ok(instance);
    }


    @GetMapping("/list")
    @ApiOperation(value = "生成指定流程实例的高亮流程图")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "getActivityList")
    @Log
    public Response<List<FlowActivityResp>> getActivityList(@RequestParam("processInstanceId")
                                                                    String processInstanceId) {
        List<FlowActivityResp> activityList = flowProcessInstanceService.getActivityListByProcessInstanceId(processInstanceId);
        return Response.ok(activityList);
    }
}
