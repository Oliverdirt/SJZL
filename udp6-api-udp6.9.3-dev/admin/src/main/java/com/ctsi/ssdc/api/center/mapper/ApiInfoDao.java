package com.ctsi.ssdc.api.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsi.ssdc.api.center.domain.ApiInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description API信息
 * @Author Len
 * @Date 2023/6/5 16:12
 */
@Mapper
public interface ApiInfoDao extends BaseMapper<ApiInfo> {
}
