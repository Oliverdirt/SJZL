package com.ctsi.ssdc.api.center.service;

import com.ctsi.ssdc.api.center.domain.ApiInfo;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.R;
import org.springframework.data.domain.Pageable;

/**
 * @Description API信息
 * @Author Len
 * @Date 2023/6/5 16:11
 */
public interface ApiInfoService {

    /**
     * 新增
     * @param apiInfo
     * @return
     */
    Long save(ApiInfo apiInfo);

    /**
     * 列表查询
     * @param apiInfo
     * @param page
     * @return
     */
    PageResult<ApiInfo> selectByPage(ApiInfo apiInfo, Pageable page);

    /**
     * 编辑
     * @param apiInfo
     * @return
     */
    Integer editApiInfo(ApiInfo apiInfo);

    /**
     * 详情
     * @param id
     * @return
     */
    ApiInfo selectDetail(Long id);

    /**
     * 调试
     * @param id
     * @param requestParam
     * @return
     */
    R<Object> test(Long id, String requestParam);

    /**
     * 自动采集
     */
    void autoCollection();

}
