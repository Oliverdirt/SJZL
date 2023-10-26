package com.ctsi.ssdc.sysconfig.service.impl;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfig;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfigExample;
import com.ctsi.ssdc.sysconfig.repository.CscpSysConfigRepository;
import com.ctsi.ssdc.sysconfig.service.CscpSysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Service Implementation for managing CscpSysConfig.
 *
 * @author hx
 * @date 2022-08-24 14:40:57
 *
 */

@Service
public class CscpSysConfigServiceImpl
	extends StrengthenBaseServiceImpl<CscpSysConfigRepository, CscpSysConfig, Long, CscpSysConfigExample>
	implements CscpSysConfigService {

    @Autowired
    private CscpSysConfigRepository cscpSysConfigRepository;


    /**
     * 批量删除
     * @param configIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] configIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(configIds));
        // 批量删除
        cscpSysConfigRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long configId) {
        cscpSysConfigRepository.deleteByPrimaryKey(configId);
    }

    @Override
    public CscpSysConfig getCscpSysConfigByConfigKey(String configKey) {
        return cscpSysConfigRepository.getCscpSysConfigByConfigKey(configKey);
    }


}
