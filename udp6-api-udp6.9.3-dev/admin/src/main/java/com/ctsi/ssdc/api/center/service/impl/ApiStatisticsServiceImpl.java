package com.ctsi.ssdc.api.center.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.api.center.mapper.ApiStatisticsDao;
import com.ctsi.ssdc.api.center.service.ApiStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description API统计
 * @Author Len
 * @Date 2023/6/13 10:05
 */
@Service
public class ApiStatisticsServiceImpl implements ApiStatisticsService {

    @Autowired
    private ApiStatisticsDao apiStatisticsDao;

    @Override
    public Long getUserSumByDay() {
        return apiStatisticsDao.getUserSumByDay(DateUtil.offsetDay(new Date(), -1).toString());
    }

    @Override
    public Long getRequestSumByDay() {
        return apiStatisticsDao.getRequestSumByDay(DateUtil.offsetDay(new Date(), -1).toString());
    }

    @Override
    public Long getMaxRequestNum() {
        return apiStatisticsDao.getMaxRequestNum(DateUtil.offsetDay(new Date(), -1).toString());
    }

    @Override
    public Map getMaxAndMinTime() {
        return apiStatisticsDao.getMaxAndMinTime(DateUtil.offsetDay(new Date(), -1).toString());
    }

    @Override
    public List<Map<String, Long>> getRequestTimeByDay() {
        return apiStatisticsDao.getRequestTimeByDay();
    }

    @Override
    public List<Map<String, Long>> getErrorRequestTimeBy24Hours() {
        return apiStatisticsDao.getErrorRequestTimeBy24Hours();
    }

    @Override
    public List<ApiLog> getAlarmRequestTimeBy24Hours() {
        List<ApiLog> logList = apiStatisticsDao
                .getAlarmRequestTimeBy24Hours(DateUtil.offsetDay(new Date(), -1).toString());
        if (!logList.isEmpty()) {
            for (int i = 0; i < logList.size(); i++) {
                String severityValue = null;
                if (logList.get(i).getSeverity() == null) {
                    continue;
                }
                switch (logList.get(i).getSeverity()) {
                    case 2:
                        severityValue = "警告";
                        break;
                    case 3:
                        severityValue = "一般严重";
                        break;
                    case 4:
                        severityValue = "严重";
                        break;
                    case 5:
                        severityValue = "灾难";
                        break;
                    default:
                        break;
                }
                logList.get(i).setSeverityValue(severityValue);
            }
        }
        return logList;
    }

    @Override
    public List<Map<String, Long>> getCodeNumBy24Hours() {
        return apiStatisticsDao.getCodeNumBy24Hours(DateUtil.offsetDay(new Date(), -1).toString());
    }

}
