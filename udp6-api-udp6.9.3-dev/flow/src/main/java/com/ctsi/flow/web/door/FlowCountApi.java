package com.ctsi.flow.web.door;

import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.request.FlowModelHotProcReq;
import com.ctsi.flow.param.response.FlowMessageResp;
import com.ctsi.flow.param.response.FlowModelHotProcResp;
import com.ctsi.flow.param.response.FlowPropMessageResp;
import com.ctsi.flow.param.response.FlowTrendMessageResp;
import com.ctsi.flow.server.door.FlowMessageDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-02 10:34
 * @description ：统计信息
 */

@Api(tags = "工作流-统计信息")
@RestController
@RequestMapping(value = "/api/flow/count")
public class FlowCountApi {


    @Resource
    private FlowMessageDetailService flowMessageDetailService;


    @GetMapping("/queryMessage")
    @ApiOperation(value = "流程概览统计信息")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "queryMessage")
    @Log
    public Response<FlowMessageResp> queryMessage(@Valid PageParam pageParam) {
        return Response.ok(flowMessageDetailService.queryMessage(pageParam));
    }

    @GetMapping("/queryTrendMessage")
    @ApiOperation(value = "流程任务一周趋势")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "queryTrendMessage")
    @Log
    public Response<FlowTrendMessageResp> queryTrendMessage() {
        return Response.ok(flowMessageDetailService.queryTrendMessage());
    }


    @GetMapping("/queryPropMessage")
    @ApiOperation(value = "流程任务占比图")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "queryPropMessage")
    @Log
    public Response<FlowPropMessageResp> queryPropMessage() {
        return Response.ok(flowMessageDetailService.queryPropMessage());
    }



    @GetMapping("/hotFlow")
    @ApiOperation(value = "热门流程分析")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "hotFlow")
    @Log
    public Response<FlowModelHotProcResp> hotFlow(FlowModelHotProcReq flowModelHotProcReq){
        return Response.ok(flowMessageDetailService.queryHotProcMessage(flowModelHotProcReq));
    }
}
