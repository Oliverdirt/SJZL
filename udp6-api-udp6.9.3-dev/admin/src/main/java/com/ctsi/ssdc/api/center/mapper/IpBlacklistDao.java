package com.ctsi.ssdc.api.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsi.ssdc.api.center.domain.IpBlacklist;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description IP黑名单
 * @Author Len
 * @Date 2023/6/8 8:35
 */
@Mapper
public interface IpBlacklistDao extends BaseMapper<IpBlacklist> {
}
