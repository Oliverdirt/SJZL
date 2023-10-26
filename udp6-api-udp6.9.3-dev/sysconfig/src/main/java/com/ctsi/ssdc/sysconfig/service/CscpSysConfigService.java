package com.ctsi.ssdc.sysconfig.service;

import com.ctsi.ssdc.sysconfig.domain.CscpSysConfig;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfigExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpSysConfig.
 *
 * @author hx
 * @date 2022-08-24 14:40:57
 *
 */

public interface CscpSysConfigService
	extends StrengthenBaseService<CscpSysConfig,Long , CscpSysConfigExample>{

	/**
	* 批量删除
	* @param configIds
	*/
	void deleteByIds(Long[] configIds);

	void deleteById(Long configId);

	CscpSysConfig getCscpSysConfigByConfigKey(String configKey);
}
