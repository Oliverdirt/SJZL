package com.ctsi.ssdc.api.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsi.ssdc.api.center.domain.ApiLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description API调用日志
 * @Author Len
 * @Date 2023/6/5 16:13
 */
@Mapper
public interface ApiLogDao extends BaseMapper<ApiLog> {
}
