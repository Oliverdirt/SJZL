package com.ctsi.ssdc.base.commonfunction.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ctsi.ssdc.base.commonfunction.domain.CommonFunction;
import com.ctsi.ssdc.base.commonfunction.domain.vo.CommonFunctionVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommonFunctionMapper extends BaseMapper<CommonFunctionVo> {

    List<CommonFunctionVo> getCommonFunctionVo(@Param(Constants.WRAPPER) QueryWrapper<CommonFunctionVo> queryWrapper);



    List<CommonFunctionVo> getCommonFunctionVoAll();
    CommonFunctionVo getCommonFunctionVoOne(Long id);


}
