package com.ctsi.ssdc.base.commonfunction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsi.ssdc.api.center.domain.ApiInfo;
import com.ctsi.ssdc.base.commonfunction.domain.CommonFunction;
import com.ctsi.ssdc.base.commonfunction.domain.vo.CommonFunctionVo;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;

public interface CommonFunctionService extends IService<CommonFunctionVo> {
    /**
     * 查询
     * @param commonFunction
     * @param page
     * @return
     */
    PageResult<CommonFunctionVo> queryPageByCondition(CommonFunctionVo commonFunction, Pageable page);

    /**
     * 重置
     * @param page
     * @return
     */
    PageResult<CommonFunctionVo> reset(Pageable page);

    /**
     * 查询某条记录
     * @param id
     * @return
     */
    CommonFunctionVo queryOne(Long id);

    /**
     * 删除功能
     * @param id
     * @return
     */
     AjaxResult removeCommonFunctionById(Long id);
}
