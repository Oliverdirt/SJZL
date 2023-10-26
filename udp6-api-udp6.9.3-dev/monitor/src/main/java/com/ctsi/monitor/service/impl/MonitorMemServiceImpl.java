package com.ctsi.monitor.service.impl;


import org.springframework.stereotype.Service;
import com.ctsi.monitor.domain.MonitorMem;
import com.ctsi.monitor.domain.MonitorMemExample;
import com.ctsi.monitor.service.MonitorMemService;
import com.ctsi.monitor.repository.MonitorMemRepository;


import java.util.*;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing MonitorMem.
 *
 * @author hx
 * @date 2022-05-27 14:01:32
 *
 */

@Service
public class MonitorMemServiceImpl
	extends StrengthenBaseServiceImpl<MonitorMemRepository, MonitorMem, Long, MonitorMemExample>
	implements MonitorMemService {

    @Autowired
    private MonitorMemRepository monitorMemRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        monitorMemRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        monitorMemRepository.deleteByPrimaryKey(id);
    }

    @Override
    public List<MonitorMem> selectByTime(Date startTime, Date endTime) {
        List<MonitorMem> monitorMems = monitorMemRepository.selectByTime(startTime, endTime);
        return monitorMems;
    }

    @Override
    public List<MonitorMem> getMsgUp10() {
        return monitorMemRepository.getMsgUp10();
    }

    @Override
    public void truncateMem(){
        monitorMemRepository.truncateMem();
    }
}
