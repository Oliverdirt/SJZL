package com.ctsi.monitor.service.impl;

import org.springframework.stereotype.Service;
import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.domain.MonitorCpuExample;
import com.ctsi.monitor.service.MonitorCpuService;
import com.ctsi.monitor.repository.MonitorCpuRepository;


import java.util.*;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing MonitorCpu.
 *
 * @author hx
 * @date 2022-05-27 11:21:43
 *
 */

@Service
public class MonitorCpuServiceImpl
	extends StrengthenBaseServiceImpl<MonitorCpuRepository, MonitorCpu, Long, MonitorCpuExample>
	implements MonitorCpuService {

    @Autowired
    private MonitorCpuRepository monitorCpuRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        monitorCpuRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        monitorCpuRepository.deleteByPrimaryKey(id);
    }

    @Override
    public List<MonitorCpu> selectByTime(Date startTime, Date endTime) {
        List<MonitorCpu> monitorCpus = monitorCpuRepository.selectByTime(startTime, endTime);
        return monitorCpus;
    }

    @Override
    public List<MonitorCpu> getMsgUp10() {
        List<MonitorCpu> monitorCpus = monitorCpuRepository.getMsgUp10();
        return monitorCpus;
    }

    @Override
    public void truncateCpu(){
        monitorCpuRepository.truncateCpu();
    }
}
