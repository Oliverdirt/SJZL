package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpNotice;
import com.ctsi.ssdc.admin.domain.CscpNoticeExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CscpNotice.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpNoticeService 
	extends StrengthenBaseService<CscpNotice, Long, CscpNoticeExample>{


    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    PageResult<CscpNotice> getCscpNotices(CscpNotice notice, Pageable pageable);
    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    int deleteNoticeByIds(Long[] noticeIds);
}
