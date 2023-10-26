package com.ctsi.flow.web.rule;

import com.ctsi.flow.server.rule.CscpTableOperationService;
import com.ctsi.flow.param.model.CscpTableOperation;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * REST controller for managing CscpTableOperation.
 * 表单字段权限
 * @author hx
 * @date 2022-07-26 10:58:11
 *
 */

@Api(value = "/api/lowcode/customize",tags = {"cscp-table-operation-api"})
@RestController
@RequestMapping("/api/lowcode/customize")
public class CscpTableOperationApi {
    @Resource
    private CscpTableOperationService cscpTableOperationService;


    @ApiOperation(value = "根据任务标识修改字段权限",httpMethod = "POST")
    @PostMapping("updateTableFieldPerm")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "updateTableFieldPerm")
    @Log
    public AjaxResult updateTableFieldPerm(@RequestBody CscpTableOperation cscpTableOperation){
        cscpTableOperationService.updateTableFieldPerm(cscpTableOperation);
        return AjaxResult.success();
    }

    @ApiOperation(value = "根据流程标识和任务标识查询字段权限",httpMethod = "GET")
    @GetMapping("qryTableFieldPerm")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "qryTableFieldPerm")
    @Log
    public AjaxResult qryTableFieldPerm(CscpTableOperation cscpTableOperation){
        return AjaxResult.success(cscpTableOperationService.qryTableFieldPerm(cscpTableOperation));
    }

}
