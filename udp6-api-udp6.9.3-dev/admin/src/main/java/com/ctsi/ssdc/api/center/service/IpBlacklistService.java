package com.ctsi.ssdc.api.center.service;

import com.ctsi.ssdc.api.center.domain.IpBlacklist;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

/**
 * @Description IP黑名单
 * @Author Len
 * @Date 2023/6/8 8:35
 */
public interface IpBlacklistService {

    /**
     * 新增
     * @param ipBlacklist
     * @return
     */
    Long save(IpBlacklist ipBlacklist);

    /**
     * 编辑
     * @param ipBlacklist
     * @return
     */
    Integer editById(IpBlacklist ipBlacklist);

    /**
     * 详情
     * @param id
     * @return
     */
    IpBlacklist selectById(Long id);

    /**
     * 列表查询
     * @param ipBlacklist
     * @param page
     * @return
     */
    PageResult<IpBlacklist> selectByPage(IpBlacklist ipBlacklist, Pageable page);
}
