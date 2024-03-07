package com.ctsi.ssdc.api.center.controller;

import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.api.center.service.ApiLogService;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description API调用日志
 * @Author Len
 * @Date 2023/6/5 16:11
 */
@RestController
@RequestMapping("/api/center/log")
public class ApiLogController {


    @Autowired
    private ApiLogService apiLogService;

    /**
     * 列表查询
     *
     * @param apiLog
     * @param page
     * @return
     */
    @GetMapping("/selectByPage")
    public PageResult<ApiLog> selectByPage(ApiLog apiLog, Pageable page) {
        return apiLogService.selectByPage(apiLog, page);
    }

    /**
     * 导出
     *
     * @return
     */
    @PostMapping("/download")
    public void download(@RequestBody ApiLog apiLog, HttpServletResponse response) {
        apiLogService.download(apiLog, response);
    }


}
