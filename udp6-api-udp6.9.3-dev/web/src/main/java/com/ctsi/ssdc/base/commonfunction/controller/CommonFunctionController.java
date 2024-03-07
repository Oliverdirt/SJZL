package com.ctsi.ssdc.base.commonfunction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.base.commonfunction.domain.CommonFunction;
import com.ctsi.ssdc.base.commonfunction.domain.vo.CommonFunctionVo;
import com.ctsi.ssdc.base.commonfunction.service.CommonFunctionService;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.R;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/commonFunc")
public class CommonFunctionController {
    private final Logger log = LoggerFactory.getLogger(CommonFunctionController.class);
    @Autowired
    CommonFunctionService commonFunctionService;

    /**
     * 条件分页条件查询
     *
     * @return
     */
    @GetMapping("/queryPageCommonFunctionByCondition")
    public R<PageResult<CommonFunctionVo>> queryPageCommonFuncByCondition(CommonFunctionVo commonFunction, Pageable page) {
        return R.ok(commonFunctionService.queryPageByCondition(commonFunction,page));
    }

    /**
     * 分页重置
     * @param
     * @return
     */
    @GetMapping("/resetPageCommonFunction")
    public R<PageResult<CommonFunctionVo>> queryAll(Pageable page) {
        return R.ok(commonFunctionService.reset(page));
    }

    /**
     * 查询某条记录
     * @param id
     * @return
     */
    @GetMapping("/queryOne/{id}")
    public R<CommonFunctionVo> queryOne(@PathVariable("id") Long id){
        return R.ok(commonFunctionService.queryOne(id));
    }

    /**
     * 新增
     * @param commonFunction
     * @return
     */
    @RequestMapping(value = "/insertOrUpdateCommonFunctionInfo",  method = RequestMethod.POST)
    public R<String> insertCommonFunctionInfo(@RequestBody CommonFunctionVo commonFunction){
        if(commonFunction.getMenuId()==null||commonFunction.getName()==null||commonFunction.getSystemId()==null){
            return R.failed("操作失败");
        }
        else{
            commonFunctionService.save(commonFunction);
            return R.ok("操作成功");
        }
    }
    /**
     * 更新
     * @param commonFunction
     * @return
     */
    @RequestMapping(value="/insertOrUpdateCommonFunctionInfo",method =RequestMethod.PUT)
    public R<String> updateCommonFunctionInfo(@RequestBody CommonFunctionVo commonFunction){
        commonFunctionService.saveOrUpdate(commonFunction);
        return R.ok("操作成功");
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCommonFunctionInfo/{postId}")
    public R<String> deleteCommonFunctionInfo(@PathVariable("postId") Long id){
        commonFunctionService.removeCommonFunctionById(id);
        return R.ok("删除成功");
    }

    @Autowired
    CscpMenusService cscpMenusService;
}

