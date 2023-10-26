package com.ctsi.flow.web.process;

import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.FlowProcessDefPageItemResp;
import com.ctsi.flow.param.response.FlowProcessDefResp;
import com.ctsi.flow.server.process.FlowDefService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 10:19
 * @description ：
 */

@Api(tags = "工作流-流程定义操作")
@RestController
@RequestMapping("/api/flow/def")
public class FlowDefApi {

    @Autowired
    private FlowDefService flowDefService;

    @GetMapping ("/page")
    @ApiOperation(value = "获得流程定义分页")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getProcessDefinitionPage")
    @Log
    public Response<PageResult<FlowProcessDefPageItemResp>> getProcessDefinitionPage(
            FlowProcessDefPageReq pageReqVO) {
        PageResult<FlowProcessDefPageItemResp> page = flowDefService.getProcessDefinitionPage(pageReqVO);
        return Response.ok();
    }


    @GetMapping ("/get-bpmn-xml")
    @ApiOperation(value = "获得流程定义的 BPMN XML")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getProcessDefinitionBpmnXML")
    @Log
    public Response<String> getProcessDefinitionBpmnXml(@RequestParam("id") String id) {
        String bpmnXml = flowDefService.getProcessDefinitionBpmnXml(id);
        return Response.ok(bpmnXml);
    }

}
