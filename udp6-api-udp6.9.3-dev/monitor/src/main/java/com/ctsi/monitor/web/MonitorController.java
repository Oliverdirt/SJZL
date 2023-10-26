package com.ctsi.monitor.web;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.domain.MonitorJvm;
import com.ctsi.monitor.domain.MonitorMem;
import com.ctsi.monitor.service.MonitorCpuService;
import com.ctsi.monitor.service.MonitorJvmService;
import com.ctsi.monitor.service.MonitorMemService;
import com.ctsi.monitor.util.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 服务器监控
 */
@RestController
@RequestMapping("/api/monitor/server")
@Api(value = "/api",tags = {"服务器监控"})
public class MonitorController {

    private final Logger log = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorCpuService cpuService;
    @Autowired
    private MonitorJvmService jvmService;
    @Autowired
    private MonitorMemService memService;

    /**
     * 获取服务器相关信息
     * @return
     * @throws Exception
     */
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getInfo")
    @GetMapping("/getInfo")
    @ApiOperation(value = "获取服务器相关信息",  httpMethod = "GET")
    public AjaxResult getInfo() throws Exception {
        log.debug("monitor/server/getInfo 请求参数信息：");
        // 获取服务器相关信息
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }

    /**
     * 根据时间查询cpu、jvm、mem使用情况
     * @return
     * @throws Exception
     */
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getMsg")
    @GetMapping("/getMsg")
    @ApiOperation(value = "根据时间查询cpu、jvm、mem使用情况",  httpMethod = "GET")
    public AjaxResult getMsg(String startTime, String endTime) throws ParseException {
        log.debug("monitor/server/getMsg 请求参数信息： " + startTime + " / " + endTime);
        Date startTimes =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
        Date endTimes =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
        JSONObject jsonObject = new JSONObject();
        List<MonitorCpu> monitorCpu = cpuService.selectByTime(startTimes, endTimes);
        List<MonitorJvm> monitorJvm = jvmService.selectByTime(startTimes, endTimes);
        List<MonitorMem> monitorMem = memService.selectByTime(startTimes, endTimes);
        jsonObject.put("cpu",monitorCpu);
        jsonObject.put("jvm",monitorJvm);
        jsonObject.put("mem",monitorMem);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 获取最近10条数据
     * @return
     *
     */
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getMsgUp10")
    @GetMapping("/getMsgUp10")
    @ApiOperation(value = "获取最近10条数据",  httpMethod = "GET")
    public AjaxResult getMsgUp10()  {
        log.debug("monitor/server/getMsgUp10");
        JSONObject jsonObject = new JSONObject();
        // 获取最近10条数据
        List<MonitorCpu> monitorCpu = cpuService.getMsgUp10();
        List<MonitorJvm> monitorJvm = jvmService.getMsgUp10();
        List<MonitorMem> monitorMem = memService.getMsgUp10();

        // 数据排序
        Collections.reverse(monitorCpu);
        Collections.reverse(monitorJvm);
        Collections.reverse(monitorMem);

        // 数据封装
        jsonObject.put("cpu",monitorCpu);
        jsonObject.put("jvm",monitorJvm);
        jsonObject.put("mem",monitorMem);
        return AjaxResult.success(jsonObject);
    }

}
