package com.ctsi.ssdc.quartz.service.impl;

import com.ctsi.ssdc.quartz.domain.CscpJobLog;
import com.ctsi.ssdc.quartz.domain.CscpJobLogExample;
import com.ctsi.ssdc.quartz.repository.CscpJobLogRepository;
import com.ctsi.ssdc.quartz.service.CscpJobLogService;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;


import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing CscpJobLog.
 *
 * @author hx
 * @date 2022-05-30 17:05:20
 *
 */

@Service
public class CscpJobLogServiceImpl
	extends StrengthenBaseServiceImpl<CscpJobLogRepository, CscpJobLog, Long, CscpJobLogExample>
	implements CscpJobLogService {

    @Autowired
    private CscpJobLogRepository cscpJobLogRepository;


    /**
     * 批量删除
     * @param jobLogIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] jobLogIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(jobLogIds));
        // 批量删除
        cscpJobLogRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long jobLogId) {
        cscpJobLogRepository.deleteByPrimaryKey(jobLogId);
    }



}
