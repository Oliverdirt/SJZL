package com.ctsi.ssdc.admin.service.impl;

import com.ctsi.ssdc.admin.domain.CscpNotice;
import com.ctsi.ssdc.admin.domain.CscpNoticeExample;
import com.ctsi.ssdc.admin.repository.CscpNoticeRepository;
import com.ctsi.ssdc.admin.service.CscpNoticeService;
import com.ctsi.ssdc.annotation.DataScope;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service Implementation for managing CscpNotice.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpNoticeServiceImpl 
	extends StrengthenBaseServiceImpl<CscpNoticeRepository, CscpNotice, Long, CscpNoticeExample> 
	implements CscpNoticeService {

    @Autowired
    CscpNoticeRepository cscpNoticeRepository;


    @Override
    @DataScope(tenantAlias = "t",userAlias = "u",deptAlias = "d")
    public PageResult<CscpNotice> getCscpNotices(CscpNotice notice, Pageable pageable) {
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        List<CscpNotice> noticeList = cscpNoticeRepository.getCscpNotices(notice);
        PageInfo<CscpNotice> pageInfo = new PageInfo<>(noticeList);
        PageResult<CscpNotice> noticeResult = new PageResult<>();
        noticeResult.setData(pageInfo.getList());
        noticeResult.setRecordsTotal(pageInfo.getTotal());
        return noticeResult;
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        List<Long> noticeList = new ArrayList<>(Arrays.asList(noticeIds));
        return cscpNoticeRepository.deleteNoticeByIds(noticeList);
    }
}
