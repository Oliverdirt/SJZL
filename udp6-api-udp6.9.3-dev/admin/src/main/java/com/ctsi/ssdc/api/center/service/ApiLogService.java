package com.ctsi.ssdc.api.center.service;

import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description API调用日志
 * @Author Len
 * @Date 2023/6/5 16:13
 */
public interface ApiLogService {

    /**
     * 列表查询
     * @param apiLog
     * @param page
     * @return
     */
    PageResult<ApiLog> selectByPage(ApiLog apiLog, Pageable page);

    /**
     * 导出
     * @return
     */
    void download(ApiLog apiLog, HttpServletResponse response);
}
