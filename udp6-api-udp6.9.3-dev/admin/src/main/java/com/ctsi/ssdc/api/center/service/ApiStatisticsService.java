package com.ctsi.ssdc.api.center.service;

import com.ctsi.ssdc.api.center.domain.ApiLog;

import java.util.List;
import java.util.Map;

/**
 * @Description API统计
 * @Author Len
 * @Date 2023/6/13 10:05
 */
public interface ApiStatisticsService {

    /**
     * 获取当日请求用户总数
     * @return
     */
    Long getUserSumByDay();

    /**
     * 获取当日请求次数
     * @return
     */
    Long getRequestSumByDay();

    /**
     * 最大并发请求次数
     * @return
     */
    Long getMaxRequestNum();

    /**
     * 获取最大最小响应时间
     * @return
     */
    Map<String, Long> getMaxAndMinTime();

    /**
     * API调用次数
     * @return
     */
    List<Map<String, Long>> getRequestTimeByDay();

    /**
     * 24小时错误率统计
     * @return
     */
    List<Map<String, Long>> getErrorRequestTimeBy24Hours();

    /**
     * 24小时告警信息
     * @return
     */
    List<ApiLog> getAlarmRequestTimeBy24Hours();

    /**
     * 24小时状态码占比
     * @return
     */
    List<Map<String, Long>> getCodeNumBy24Hours();
}
