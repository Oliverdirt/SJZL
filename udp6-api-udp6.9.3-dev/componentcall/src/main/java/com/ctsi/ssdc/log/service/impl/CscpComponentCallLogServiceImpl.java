package com.ctsi.ssdc.log.service.impl;

import com.ctsi.ssdc.log.domain.CscpComponentCallLog;
import com.ctsi.ssdc.log.domain.CscpComponentCallLogExample;
import com.ctsi.ssdc.log.repository.CscpComponentCallLogRepository;
import com.ctsi.ssdc.log.service.CscpComponentCallLogService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing CscpComponentCallLog.
 *
 * @author hx
 * @date 2022-03-15 21:35:20
 *
 */

@Service
public class CscpComponentCallLogServiceImpl
	extends StrengthenBaseServiceImpl<CscpComponentCallLogRepository, CscpComponentCallLog, Long, CscpComponentCallLogExample>
	implements CscpComponentCallLogService {

    @Autowired
    private CscpComponentCallLogRepository cscpComponentCallLogRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpComponentCallLogRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpComponentCallLogRepository.deleteByPrimaryKey(id);
    }
}
