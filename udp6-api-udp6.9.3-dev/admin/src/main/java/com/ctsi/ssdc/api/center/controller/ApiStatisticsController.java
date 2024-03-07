package com.ctsi.ssdc.api.center.controller;

import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.api.center.service.ApiStatisticsService;
import com.ctsi.ssdc.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description API统计
 * @Author Len
 * @Date 2023/6/13 10:04
 */
@RestController
@RequestMapping("/api/statistics")
public class ApiStatisticsController {

    @Autowired
    private ApiStatisticsService apiStatisticsService;

    /**
     * 获取当日请求用户总数
     * @return
     */
    @GetMapping("/getUserSumByDay")
    public R<Long> getUserSumByDay() {
        return R.ok(apiStatisticsService.getUserSumByDay());
    }

    /**
     * 获取当日请求次数
     * @return
     */
    @GetMapping("/getRequestSumByDay")
    public R<Long> getRequestSumByDay() {
        return R.ok(apiStatisticsService.getRequestSumByDay());
    }

    /**
     * 最大并发请求次数
     * @return
     */
    @GetMapping("/getMaxRequestNum")
    public R<Long> getMaxRequestNum() {
        return R.ok(apiStatisticsService.getMaxRequestNum());
    }


    /**
     * 获取最大最小响应时间
     * @return
     */
    @GetMapping("/getMaxAndMinTime")
    public R<Map<String, Long>> getMaxAndMinTime() {
        return R.ok(apiStatisticsService.getMaxAndMinTime());
    }

    /**
     * API调用次数
     * @return
     */
    @GetMapping("/getRequestTimeByDay")
    public R<List<Map<String, Long>>> getRequestTimeByDay() {
        return R.ok(apiStatisticsService.getRequestTimeByDay());
    }

    /**
     * 24小时错误率统计
     * @return
     */
    @GetMapping("/getErrorRequestTimeBy24Hours")
    public R<List<Map<String, Long>>> getErrorRequestTimeBy24Hours() {
        return R.ok(apiStatisticsService.getErrorRequestTimeBy24Hours());
    }

    /**
     * 24小时告警信息
     * @return
     */
    @GetMapping("/getAlarmRequestTimeBy24Hours")
    public R<List<ApiLog>> getAlarmRequestTimeBy24Hours() {
        return R.ok(apiStatisticsService.getAlarmRequestTimeBy24Hours());
    }

    /**
     * 24小时状态码占比
     * @return
     */
    @GetMapping("/getCodeNumBy24Hours")
    public R<List<Map<String, Long>>> getCodeNumBy24Hours() {
        return R.ok(apiStatisticsService.getCodeNumBy24Hours());
    }



}
