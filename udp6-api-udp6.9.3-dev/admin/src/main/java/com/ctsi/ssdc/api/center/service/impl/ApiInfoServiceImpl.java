package com.ctsi.ssdc.api.center.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctsi.ssdc.api.center.domain.ApiInfo;
import com.ctsi.ssdc.api.center.domain.ApiLog;
import com.ctsi.ssdc.api.center.domain.dto.DicInfoDTO;
import com.ctsi.ssdc.api.center.mapper.ApiInfoDao;
import com.ctsi.ssdc.api.center.mapper.ApiLogDao;
import com.ctsi.ssdc.api.center.mapper.SystemDao;
import com.ctsi.ssdc.api.center.service.ApiInfoService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.R;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HttpClientUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description API信息
 * @Author Len
 * @Date 2023/6/5 16:11
 */
@Service
@Transactional
public class ApiInfoServiceImpl implements ApiInfoService {

    @Autowired
    private ApiInfoDao apiInfoDao;

    @Autowired
    private SystemDao systemDao;

    @Autowired
    private ApiLogDao apiLogDao;

    @Override
    public Long save(ApiInfo apiInfo) {
        apiInfo.setCreateId(SecurityHxUtils.getCurrentUserId());
        apiInfo.setCreateTime(LocalDateTime.now());
        apiInfo.setUpdateId(apiInfo.getCreateId());
        apiInfo.setUpdateTime(apiInfo.getCreateTime());
        apiInfo.setConfirm(0);
        apiInfo.setSourceType(0);
        //新增
        int count = apiInfoDao.insert(apiInfo);
        if (count < 1) {
            return null;
        }
        return apiInfo.getId();
    }

    @Override
    public PageResult<ApiInfo> selectByPage(ApiInfo apiInfo, Pageable page) {
        //开启分页
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());

        QueryWrapper<ApiInfo> wrapper = new QueryWrapper<>();
        if (apiInfo != null && !StringUtils.isEmpty(apiInfo.getApiName())) {
            wrapper.like("API_NAME", apiInfo.getApiName());
        }
        if (apiInfo != null && apiInfo.getSystemId() != null) {
            wrapper.eq("SYSTEM_ID", apiInfo.getSystemId());
        }
        if (apiInfo != null && apiInfo.getApiType() != null) {
            wrapper.eq("API_TYPE", apiInfo.getApiType());
        }
        if (apiInfo != null && apiInfo.getApiStatus() != null) {
            wrapper.eq("API_STATUS", apiInfo.getApiStatus());
        }
        wrapper.eq("DEL_FLAG", 0);
        wrapper.orderByDesc("UPDATE_TIME");
        List<ApiInfo> result = apiInfoDao.selectList(wrapper);
        PageInfo<ApiInfo> info = new PageInfo<>(result);
        if (!result.isEmpty()) {
            List<DicInfoDTO> dicInfoDTOList = systemDao.getDicInfoByDicType("apiType");
            for (int i = 0; i < result.size(); i++) {
                Integer apiType = result.get(i).getApiType();
                //查询接口类型
                DicInfoDTO dicItem = dicInfoDTOList.stream().filter(item -> item.getDicCode().equals(String.valueOf(apiType))).findAny().orElse(null);
                if (dicItem != null) {
                    result.get(i).setApiTypeValue(dicItem.getDicValue());
                }
                //修改来源
                result.get(i).setSourceTypeValue(result.get(i).getSourceType() == 0 ? "手动录入" : "自动采集");
            }
        }
        return new PageResult<>(result, info.getTotal(), info.getTotal());
    }

    @Override
    public Integer editApiInfo(ApiInfo apiInfo) {

        ApiInfo info = apiInfoDao.selectById(apiInfo.getId());

        if (apiInfo.getApiStatus() != null) {
            info.setApiStatus(apiInfo.getApiStatus());
        }

        if (!StringUtils.isEmpty(apiInfo.getApiName())) {
            info.setApiName(apiInfo.getApiName());
        }

        if (apiInfo.getSystemId() != null) {
            info.setSystemId(apiInfo.getSystemId());
        }

        if (apiInfo.getDeptId() != null) {
            info.setDeptId(apiInfo.getDeptId());
        }

        if (apiInfo.getGroupId() != null) {
            info.setGroupId(apiInfo.getGroupId());
        }

        if (apiInfo.getApiType() != null) {
            info.setApiType(apiInfo.getApiType());
        }

        if (!StringUtils.isEmpty(apiInfo.getApiAddress())) {
            info.setApiAddress(apiInfo.getApiAddress());
        }

        if (!StringUtils.isEmpty(apiInfo.getApiDesc())) {
            info.setApiDesc(apiInfo.getApiDesc());
        }

        if (!StringUtils.isEmpty(apiInfo.getRequestParams())) {
            info.setRequestParams(apiInfo.getRequestParams());
        }

        if (!StringUtils.isEmpty(apiInfo.getRequestMethod())) {
            info.setRequestMethod(apiInfo.getRequestMethod());
        }

        if (!StringUtils.isEmpty(apiInfo.getResponseParams())) {
            info.setResponseParams(apiInfo.getResponseParams());
        }

        if (!StringUtils.isEmpty(apiInfo.getResponseCode())) {
            info.setResponseCode(apiInfo.getResponseCode());
        }

        if (!StringUtils.isEmpty(apiInfo.getProtocol())) {
            info.setProtocol(apiInfo.getProtocol());
        }

        if (apiInfo.getDelFlag() != null) {
            info.setDelFlag(apiInfo.getDelFlag());
        }

        if (apiInfo.getConfirm() != null) {
            info.setConfirm(apiInfo.getConfirm());
        }

        info.setUpdateTime(LocalDateTime.now());
        info.setUpdateId(SecurityHxUtils.getCurrentUserId());
        return apiInfoDao.updateById(info);
    }

    @Override
    public ApiInfo selectDetail(Long id) {
        ApiInfo apiInfo = apiInfoDao.selectById(id);
        if (apiInfo == null || StringUtils.isEmpty(apiInfo.getRequestParams())) {
            return apiInfo;
        }
        List<Map> mapList = JSON.parseArray(apiInfo.getRequestParams(), Map.class);
        if (!mapList.isEmpty()) {
            Map<String, Object> map = getStringObjectMap(mapList);
            apiInfo.setRequestParam(JSON.toJSONString(map));
        }
        List<DicInfoDTO> apiTypeList = systemDao.getDicInfoByDicType("apiType");
        //查询接口类型
        Integer apiType = apiInfo.getApiType();
        DicInfoDTO dicItem = null;
        if (apiType != null && (dicItem = apiTypeList.stream().filter(item -> item.getDicCode()
                .equals(String.valueOf(apiType))).findAny().orElse(null)) != null) {
            apiInfo.setApiTypeValue(dicItem.getDicValue());
        }
        return apiInfo;
    }

    private Map<String, Object> getStringObjectMap(List<Map> mapList) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < mapList.size(); i++) {
            Object formatValue = "";
            switch (String.valueOf(mapList.get(i).get("format"))) {
                case "int":
                    formatValue = 0;
                    break;
                case "number":
                    formatValue = 0;
                    break;
                case "array":
                    formatValue = new ArrayList<>();
                    break;
                case "time":
                    formatValue = new Date();
                    break;
                case "object":
                    formatValue = new Object();
                    break;
                default:
                    break;
            }
            map.put(String.valueOf(mapList.get(i).get("param_name")), formatValue);
        }
        return map;
    }

    @Override
    public R<Object> test(Long id, String requestParam) {
        Map<String, String> requestParamMap = JSON.parseObject(requestParam, new TypeReference<HashMap<String, String>>() {
        });
        ApiInfo apiInfo = apiInfoDao.selectById(id);
        if (apiInfo == null) {
            return R.failed("接口不存在");
        }

        if (apiInfo.getApiStatus() != 0 || apiInfo.getConfirm() != 0) {
            return R.failed("接口不可用");
        }

        Boolean flag = true;
        Map<String, String> params = new HashMap<>();
        String requestParams = apiInfo.getRequestParams();
        List<Map> mapList = JSON.parseArray(requestParams, Map.class);
        if (!mapList.isEmpty()) {
            for (int i = 0; i < mapList.size(); i++) {
                String required = mapList.get(i).get("required").toString();
                String param_name = mapList.get(i).get("param_name").toString();
                if (!StringUtils.isEmpty(requestParam) && required.equals("Y") && StringUtils.isEmpty(requestParamMap.get(param_name))) {
                    flag = false;
                    break;
                }
                params.put(param_name, requestParamMap.get(param_name));
            }
        }
        if (!flag) {
            return R.failed("缺失必填参数");
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + SecurityHxUtils.getCurrentUserJwt().get());
        try {
            String request = HttpClientUtil.request(apiInfo.getApiAddress(), apiInfo.getRequestMethod().toLowerCase(),
                    params, headers, "UTF-8");
            return R.ok(request);
        } catch (IOException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    @Override
    public void autoCollection() {

        QueryWrapper<ApiLog> wrapper = new QueryWrapper<>();
        wrapper.eq("RESPONSE_CODE", "200");
        wrapper.isNotNull("API_ADDRESS");
        List<ApiLog> selectList = apiLogDao.selectList(wrapper);

        if (selectList.isEmpty()) {
            return;
        }

        for (int i = 0; i < selectList.size(); i++) {
            //查询是否存在
            QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("API_ADDRESS", selectList.get(i).getApiAddress());
            Integer count = apiInfoDao.selectCount(queryWrapper);
            //如果不存在，则新增
            if (count < 1) {
                ApiInfo apiInfo = new ApiInfo();
                apiInfo.setApiAddress(selectList.get(i).getApiAddress());
                apiInfo.setUpdateTime(LocalDateTime.now());
                apiInfo.setSourceType(1);
                apiInfo.setConfirm(1);
                apiInfo.setApiStatus(0);
                apiInfo.setRequestMethod(selectList.get(i).getRequestMethod());
                apiInfo.setProtocol(selectList.get(i).getProtocol());
                apiInfoDao.insert(apiInfo);
            }
        }
    }
}
