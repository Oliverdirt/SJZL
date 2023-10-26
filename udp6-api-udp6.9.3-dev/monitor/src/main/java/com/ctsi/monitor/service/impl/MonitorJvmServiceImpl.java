package com.ctsi.monitor.service.impl;

import org.springframework.stereotype.Service;
import com.ctsi.monitor.domain.MonitorJvm;
import com.ctsi.monitor.domain.MonitorJvmExample;
import com.ctsi.monitor.service.MonitorJvmService;
import com.ctsi.monitor.repository.MonitorJvmRepository;


import java.util.*;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing MonitorJvm.
 *
 * @author hx
 * @date 2022-05-27 11:49:49
 *
 */

@Service
public class MonitorJvmServiceImpl
	extends StrengthenBaseServiceImpl<MonitorJvmRepository, MonitorJvm, Long, MonitorJvmExample>
	implements MonitorJvmService {

    @Autowired
    private MonitorJvmRepository monitorJvmRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        monitorJvmRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        monitorJvmRepository.deleteByPrimaryKey(id);
    }

    @Override
    public List<MonitorJvm> selectByTime(Date startTime, Date endTime) {
        List<MonitorJvm> monitorJvm = monitorJvmRepository.selectByTime(startTime, endTime);
        return monitorJvm;
    }

    @Override
    public List<MonitorJvm> getMsgUp10() {
        List<MonitorJvm> monitorJvm = monitorJvmRepository.getMsgUp10();
        return monitorJvm;
    }


    @Override
    public void truncateJvm(){
        monitorJvmRepository.truncateJvm();
    }

}
