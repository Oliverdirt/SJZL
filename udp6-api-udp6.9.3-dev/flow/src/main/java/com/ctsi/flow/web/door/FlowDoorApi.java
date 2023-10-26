package com.ctsi.flow.web.door;

import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.model.ActRdTurnTask;
import com.ctsi.flow.server.door.FlowDoorService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/19 14:36  by xx
 */
@Api(tags = "工作流-个人门户")
@RestController
@RequestMapping("/api/flow/door")
public class FlowDoorApi {
    @Resource
    private FlowDoorService flowDoorService;

    /*查询传阅或转办信息*/
    @GetMapping("/rdTurnTask-select")
    @ApiOperation(value = "查询传阅或转办信息")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "qryRdTurnTaskInfo")
    @Log
    public Response<PageInfo> qryRdTurnTaskInfo(PageParam pageParam, String type){
        return Response.ok(flowDoorService.qryRdTurnTaskInfo(pageParam,type));
    }

    /*新增传阅或转办信息*/
    @PostMapping("/rdTurnTask-create")
    @ApiOperation(value = "新增传阅或转办信息")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "createRdTurnTask")
    @Log
    public Response createRdTurnTask(@RequestBody ActRdTurnTask actRdTurnTask){
        flowDoorService.createRdTurnTask(actRdTurnTask);
        return Response.ok();
    }

}
