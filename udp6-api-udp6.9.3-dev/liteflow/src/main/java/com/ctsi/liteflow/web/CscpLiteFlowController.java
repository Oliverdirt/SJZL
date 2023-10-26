package com.ctsi.liteflow.web;

import com.ctsi.liteflow.vo.LiteflowCompBeanRes;
import com.ctsi.liteflow.vo.LiteflowCompMethodRes;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: sunsheng
 **/
@Api(value = "/api", tags = {"cscp-lite-flow-controller"})
@RestController
@RequestMapping("/api/liteflow")
public class CscpLiteFlowController {
    @Autowired
    private ApplicationContext applicationContext;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "fieldId",value = "the fieldId of the cscpCustomizeVfield to retrieve")
    })
    @ApiOperation(value = "GET  /getAllSpringBeans : get all beans with annotation  LiteflowComponent.",notes = "GET  /getAllSpringBeans : get all beans with annotation  LiteflowComponent.",httpMethod = "GET")
    @GetMapping("/getAllSpringBeans")
    public List<LiteflowCompBeanRes> getAllSpringBeans(){
        ArrayList<LiteflowCompBeanRes> list = new ArrayList<>();
        Map<String, Object> liteFlowBeansMap = applicationContext.getBeansWithAnnotation(LiteflowComponent.class);
        for (Map.Entry<String, Object> entry : liteFlowBeansMap.entrySet()) {
            LiteflowCompBeanRes compBeanRes = new LiteflowCompBeanRes();
            Object value = entry.getValue();
            Class<?> targetClass = AopUtils.getTargetClass(value);
            LiteflowComponent liteflowComponent = targetClass.getDeclaredAnnotation(LiteflowComponent.class);
            compBeanRes.setId(liteflowComponent.id());
            compBeanRes.setName(liteflowComponent.name());
            compBeanRes.setValue(liteflowComponent.value());
            list.add(compBeanRes);
        }
        return list;
    }
}
