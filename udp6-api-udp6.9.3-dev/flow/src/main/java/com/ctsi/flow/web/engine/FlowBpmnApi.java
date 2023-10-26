package com.ctsi.flow.web.engine;

import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.FlowModelPageItemResp;
import com.ctsi.flow.param.response.FlowModelResp;
import com.ctsi.flow.server.engine.FlowBpmnService;
import com.ctsi.flow.server.roleact.CscpRoleActService;
import com.ctsi.flow.server.useract.CscpUserActService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-06 9:30
 * @description ：
 */

@Api(tags = "工作流-流程模型")
@RestController
@RequestMapping("/api/flow/bpmn")
public class FlowBpmnApi {

    @Resource
    private FlowBpmnService flowBpmnService;
    @Autowired
    private CscpRoleActService cscpRoleActService;
    @Autowired
    private CscpUserActService cscpUserActService;

    @PostMapping("/create")
    @ApiOperation(value = "新建模型")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "createModel")
    @Log
    public Response<String> createModel(@Valid @RequestBody FlowModelCreateReq createRet) {

        flowBpmnService.createModel(createRet, null);
        return Response.ok();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "deleteModel")
    @Log
    public Response<Boolean> deleteModel(@RequestParam("id") String id) {
        flowBpmnService.deleteModel(id);
        //删除该模型与角色的绑定
        cscpRoleActService.deleteByProcDefId(id);
        //删除该模型与用户的绑定
        cscpUserActService.deleteByProcDefId(id);
        return Response.ok();
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改模型")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "updateModel")
    @Log
    public Response<Boolean> updateModel(@Valid @RequestBody FlowModelUpdateReq updateRet) {
        flowBpmnService.updateModel(updateRet);
        return Response.ok();
    }

    @GetMapping("/page")
    @ApiOperation(value = "获得模型分页")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getModelPage")
    @Log
    public Response<PageInfo<FlowModelPageItemResp>> getModelPage(FlowModelPageReq page) {
        return Response.ok(flowBpmnService.getModelPage(page));
    }

    @GetMapping("/get")
    @ApiOperation("获得模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getModel")
    @Log
    public Response<FlowModelResp> getModel(@RequestParam("id") String id) {
        FlowModelResp model = flowBpmnService.getModel(id);
        return Response.ok(model);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入模型")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "importModel")
    @Log
    public Response<String> importModel(@Valid FlowModeImportReq importReqVO) throws IOException {

//        FlowModelCreateReq createReqVO = FlowModelConvert.convert(importReqVO);
        // 读取文件
        InputStream inputStream = importReqVO.getBpmnFile().getInputStream();
        return Response.ok(flowBpmnService.importModel(importReqVO,inputStream));
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "部署模型")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "deployModel")
    @Log
    public Response<Boolean> deployModel(@RequestParam("id") String id) {
        flowBpmnService.deployModel(id);
        return Response.ok();
    }

    @PutMapping("/update-state")
    @ApiOperation(value = "修改模型的状态", notes = "实际更新的部署的流程定义的状态")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "updateModelState")
    @Log
    public Response<Boolean> updateModelState(@Valid @RequestBody FlowModelUpdateStateReq reqVO) {
        flowBpmnService.updateModelState(reqVO.getId(), reqVO.getState());
        return Response.ok();
    }
}
