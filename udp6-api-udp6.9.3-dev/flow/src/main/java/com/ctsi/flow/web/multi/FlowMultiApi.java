package com.ctsi.flow.web.multi;

import com.ctsi.flow.multi.loader.MultiStrategyDetail;
import com.ctsi.flow.multi.loader.MultiStrategyLoader;
import com.ctsi.flow.multi.service.MultiInstanceService;
import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.response.FlowMessageResp;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-10-26 14:21
 * @description ：
 */

@Api(tags = "多实例-API")
@RestController
@RequestMapping(value = "/api/flow/multi")
public class FlowMultiApi {


    @Resource
    private MultiInstanceService multiInstanceService;

    @Resource
    private MultiStrategyLoader multiStrategyLoader;

    @GetMapping("/isMulti")
    @ApiOperation(value = "判断节点是否多实例节点")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "isMulti")
    @Log
    public Response<Boolean> isMulti(@RequestParam String taskKey) {

        return Response.ok(multiInstanceService.isMulti(taskKey));
    }

    @GetMapping("/listAllMultiStrategy")
    @ApiOperation(value = "查询所有多实例策略")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "listAllMultiStrategy")
    @Log
    public Response<List<MultiStrategyDetail>> listAllMultiStrategy() {

        return Response.ok(multiStrategyLoader.getMultiInstanceStrategyList());
    }

}
