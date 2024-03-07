package com.ctsi.ssdc.api.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.api.center.mapper.ApiLogDao;
import com.ctsi.ssdc.api.center.service.ApiLogService;
import com.ctsi.ssdc.api.center.util.ExcelUtils;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description API调用日志
 * @Author Len
 * @Date 2023/6/5 16:14
 */
@Service
public class ApiLogServiceImpl implements ApiLogService {

    @Autowired
    private ApiLogDao apiLogDao;


    @Override
    public PageResult<ApiLog> selectByPage(ApiLog apiLog, Pageable page) {

        //开启分页
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());

        //查询
        QueryWrapper<ApiLog> wrapper = new QueryWrapper<>();

        if (apiLog != null && apiLog.getSystemId() != null) {
            wrapper.eq("SYSTEM_ID", apiLog.getSystemId());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getApiName())) {
            wrapper.like("API_NAME", apiLog.getApiName());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getIpAddress())) {
            wrapper.like("IP_ADDRESS", apiLog.getIpAddress());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getInsideNo())) {
            wrapper.like("INSIDE_NO", apiLog.getInsideNo());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getOutsideNo())) {
            wrapper.like("OUTSIDE_NO", apiLog.getOutsideNo());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getStartTime())) {
            wrapper.ge("REQUEST_TIME", apiLog.getStartTime());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getEndTime())) {
            wrapper.le("REQUEST_TIME", apiLog.getEndTime());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getResponseCode())) {
            wrapper.like("RESPONSE_CODE", apiLog.getResponseCode());
        }

        wrapper.orderByDesc("REQUEST_TIME");

        List<ApiLog> result = apiLogDao.selectList(wrapper);

        PageInfo<ApiLog> info = new PageInfo<>(result);

        return new PageResult<>(result, info.getTotal(), info.getTotal());
    }


    @Override
    public void download(ApiLog apiLog, HttpServletResponse response) {
        //获取查询的数据
        QueryWrapper<ApiLog> wrapper = new QueryWrapper<>();

        if (apiLog != null && apiLog.getSystemId() != null) {
            wrapper.eq("SYSTEM_ID", apiLog.getSystemId());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getApiName())) {
            wrapper.like("API_NAME", apiLog.getApiName());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getIpAddress())) {
            wrapper.like("IP_ADDRESS", apiLog.getIpAddress());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getInsideNo())) {
            wrapper.like("INSIDE_NO", apiLog.getInsideNo());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getOutsideNo())) {
            wrapper.like("OUTSIDE_NO", apiLog.getOutsideNo());
        }
        if (apiLog != null && apiLog.getStartTime() != null) {
            wrapper.ge("REQUEST_TIME", apiLog.getStartTime());
        }
        if (apiLog != null && apiLog.getEndTime() != null) {
            wrapper.le("REQUEST_TIME", apiLog.getEndTime());
        }
        if (apiLog != null && !StringUtils.isEmpty(apiLog.getResponseCode())) {
            wrapper.like("RESPONSE_CODE", apiLog.getResponseCode());
        }

        if (apiLog != null && apiLog.getId() != null) {
            wrapper.eq("ID", apiLog.getId());
        }
        wrapper.orderByDesc("REQUEST_TIME");

        List<ApiLog> result = apiLogDao.selectList(wrapper);

        //添加列表数据
        List<Map<String, Object>> excelRecord = createExcelRecord(result);
        //设置列名
        String[] columnNames = {"内部流水号", "API名称", "API方法", "调用流程", "调用IP", "外部流水号", "调用时间",
                "返回信息编码", "请求参数", "响应结果"};
        //列名对应的key，与excelRecord的map中key相同
        String[] keys = {"insideNo", "apiName", "apiMethod", "process", "ipAddress", "outsideNo", "requestTime",
                "responseCode", "requestParams", "responseResult"};
        //定义Workbook
        Workbook workbook = null;
        try {
            //调用ExcelUtil创建excel表格并填入数据
            workbook = ExcelUtils.createWorkBook(excelRecord, keys, columnNames);
            // 设置response参数，可以打开下载页面
            //准备将Excel的输出流通过response输出到页面下载
            //八进制输出流
            response.setContentType("application/octet-stream");
            //设置导出Excel的名称
            response.setHeader("Content-disposition", "attachment;filename=hospital.xls");
            //刷新缓冲
            ServletOutputStream outputStream = response.getOutputStream();
            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Describe 添加列表数据
     */
    private List<Map<String, Object>> createExcelRecord(List<ApiLog> records) {
        List<Map<String, Object>> listmap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        //遍历查询出的数据并放入listmap中
        for (int j = 0; j < records.size(); j++) {
            ApiLog project = records.get(j);
            Map<String, Object> mapValue = new HashMap<>();
            mapValue.put("insideNo", project.getInsideNo());
            mapValue.put("apiName", project.getApiName());
            mapValue.put("apiMethod", project.getApiMethod());
            mapValue.put("process", project.getProcess());
            mapValue.put("ipAddress", project.getIpAddress());
            mapValue.put("outsideNo", project.getOutsideNo());
            mapValue.put("requestTime",
                    project.getRequestTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            mapValue.put("responseCode", project.getResponseCode());
            mapValue.put("requestParams", project.getRequestParams());
            mapValue.put("responseResult", project.getResponseResult());
            listmap.add(mapValue);
        }
        return listmap;
    }


}
