package com.ctsi.ssdc.base.commonfunction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctsi.ssdc.api.center.domain.ApiInfo;
import com.ctsi.ssdc.base.commonfunction.domain.CommonFunction;
import com.ctsi.ssdc.base.commonfunction.domain.vo.CommonFunctionVo;
import com.ctsi.ssdc.base.commonfunction.mapper.CommonFunctionMapper;
import com.ctsi.ssdc.base.commonfunction.service.CommonFunctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;


@Service
public class CommonFunctionServiceImpl extends ServiceImpl<CommonFunctionMapper, CommonFunctionVo> implements CommonFunctionService {

    @Resource
    private CommonFunctionMapper commonFunctionMapper;

    /**
     * 根据条件分页查询和重置
     * @param commonFunction
     * @param page
     * @return
     */
    @Override
    public PageResult<CommonFunctionVo> queryPageByCondition(CommonFunctionVo commonFunction, Pageable page){
//        System.out.println("&");
//        System.out.println("&");
//        System.out.println(commonFunction);
//        System.out.println(page);
//        System.out.println("&");
//        System.out.println("&");
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        QueryWrapper<CommonFunctionVo> queryWrapper=new QueryWrapper<>();
        if(commonFunction.getName()!=""&&commonFunction.getName()!=null){
            queryWrapper.eq("NAME",commonFunction.getName());
        }
        if(commonFunction.getModelId()!=null){
            queryWrapper.eq("MODEL_ID",commonFunction.getModelId());
        }
        if(commonFunction.getSystemId()!=null){
            queryWrapper.eq("SYSTEM_ID",commonFunction.getSystemId());
        }
        if(commonFunction.getStatus()!=null){
            queryWrapper.eq("STATUS",commonFunction.getStatus());
        }
        if(commonFunction.getTitle()!=""&&commonFunction.getTitle()!=null){
            queryWrapper.eq("TITLE",commonFunction.getTitle());
        }

//        System.out.println("&");
//        System.out.println("&");
//        System.out.println("&");
//        System.out.println(queryWrapper);
//        System.out.println("&");
//        System.out.println("&");
//        System.out.println("&");
        List<CommonFunctionVo> result=commonFunctionMapper.selectList(queryWrapper);
//        System.out.println("*");
//        System.out.println("*");
//        System.out.println("*");
//        System.out.println("*");
//        System.out.println("*");
//        System.out.println("*");
        PageInfo<CommonFunctionVo> info = new PageInfo<>(result);
        return new PageResult<>(result, info.getTotal(), info.getTotal());

    };

    /**
     * 分页重置
     *
     * @return
     */
    @Override
    public PageResult<CommonFunctionVo> reset(Pageable page){
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CommonFunctionVo> result=commonFunctionMapper.getCommonFunctionVoAll();
        PageInfo<CommonFunctionVo> info = new PageInfo<>(result);
        return new PageResult<>(result, info.getTotal(), info.getTotal());
    }

    /**
     * 查询某条常用功能
     * @param id
     * @return
     */
    public CommonFunctionVo queryOne(Long id){
        return commonFunctionMapper.getCommonFunctionVoOne(id);
    };


    @Override
    @Transactional
    public AjaxResult removeCommonFunctionById(Long id) {
        // 删除主表信息
        this.removeById(id);
        return AjaxResult.success("删除成功！！");
    }

}
