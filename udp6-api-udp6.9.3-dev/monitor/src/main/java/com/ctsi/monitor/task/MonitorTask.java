package com.ctsi.monitor.task;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.util.Server;
import com.ctsi.monitor.domain.MonitorJvm;
import com.ctsi.monitor.domain.MonitorMem;
import com.ctsi.monitor.service.MonitorCpuService;
import com.ctsi.monitor.service.MonitorJvmService;
import com.ctsi.monitor.service.MonitorMemService;
import com.ctsi.monitor.util.Arith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统监控定时任务
 *
 * */
@Component("monitorTask")
public class MonitorTask {

    private final Logger log = LoggerFactory.getLogger(MonitorTask.class);

    @Autowired
    private MonitorCpuService cpuService;
    @Autowired
    private MonitorJvmService jvmService;
    @Autowired
    private MonitorMemService memService;

    public void getMsg() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("系统监控定时任务开始 " + df.format(new Date()) );
        // 获取服务器相关信息
        Server server = new Server();
        server.copyTo();
        AjaxResult success = AjaxResult.success(server);
        JSONObject jsonObjects = new JSONObject(success);
        JSONObject data = jsonObjects.getJSONObject("data");

        //cpu数据入库
        JSONObject jsonObject = data.getJSONObject("cpu");
        Double sys = jsonObject.getDouble("sys");
        Double used = jsonObject.getDouble("used");
        Integer cpuNum = jsonObject.getInteger("cpuNum");
        Double total = jsonObject.getDouble("total");
        Double wait = jsonObject.getDouble("wait");
        Double free = jsonObject.getDouble("free");
        MonitorCpu cpu = new MonitorCpu();
        cpu.setCpunum(cpuNum);
        cpu.setTotal(total);
        cpu.setSys(sys);
        cpu.setUsed(used);
        cpu.setWait(wait);
        cpu.setFree(free);
        cpu.setUsages(100 - free);
        cpu.setTime(df.format(new Date()));
        cpuService.insert(cpu);

        //mem数据入库
        JSONObject mem = data.getJSONObject("mem");
        Double memUsed = mem.getDouble("used");
        Double memTotal = mem.getDouble("total");
        Double memUsage = mem.getDouble("usage");
        Double memFree = mem.getDouble("free");
        MonitorMem mems = new MonitorMem();
        mems.setFree(memFree);
        mems.setTotal(memTotal);
        mems.setUsages(memUsage);
        mems.setUsed(memUsed);
        mems.setTime(df.format(new Date()));
        memService.insert(mems);

        //jvm数据入库
        JSONObject jvm = data.getJSONObject("jvm");
        Double jvmTotal = jvm.getDouble("total");
        Double jvmMax = jvm.getDouble("max");
        Double jvmFree = jvm.getDouble("free");
        MonitorJvm jvms = new MonitorJvm();
        jvms.setFree(jvmFree);
        jvms.setTotal(jvmTotal);
        jvms.setMax(jvmMax);
        jvms.setTime(df.format(new Date()));
        jvms.setUsages(Arith.mul(Arith.div(jvmTotal - jvmFree, jvmTotal, 4), 100));
        jvms.setUsed(jvmTotal - jvmFree);
        jvmService.insert(jvms);

        log.debug("系统监控定时任务结束");
    }
}
