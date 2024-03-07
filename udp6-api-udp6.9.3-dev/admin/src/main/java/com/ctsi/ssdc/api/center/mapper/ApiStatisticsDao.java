package com.ctsi.ssdc.api.center.mapper;

import com.ctsi.ssdc.api.center.domain.ApiLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description API统计
 * @Author Len
 * @Date 2023/6/13 10:06
 */
@Mapper
public interface ApiStatisticsDao {

    /**
     * 获取当日请求用户总数
     * @param dateFormat
     * @return
     */
    Long getUserSumByDay(String dateFormat);

    /**
     * 获取当日请求次数
     * @param dateFormat
     * @return
     */
    Long getRequestSumByDay(String dateFormat);

    /**
     * 最大并发请求次数
     * @param dateFormat
     * @return
     */
    Long getMaxRequestNum(String dateFormat);

    /**
     * 获取最大最小响应时间
     * @param dateFormat
     * @return
     */
    Map<String, Long> getMaxAndMinTime(String dateFormat);

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
     * @param dateFormat
     * @return
     */
    List<ApiLog> getAlarmRequestTimeBy24Hours(String dateFormat);

    /**
     * 24小时状态码占比
     * @param dateFormat
     * @return
     */
    List<Map<String, Long>> getCodeNumBy24Hours(String dateFormat);
}
