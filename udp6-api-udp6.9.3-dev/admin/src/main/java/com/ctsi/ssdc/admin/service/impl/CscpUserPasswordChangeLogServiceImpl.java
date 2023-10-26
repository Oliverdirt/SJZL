package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.CscpUserPasswordChangeLog;
import com.ctsi.ssdc.admin.repository.CscpUserPasswordChangeLogRepository;
import com.ctsi.ssdc.admin.service.CscpUserPasswordChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing CscpUserPasswordChangeLog.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpUserPasswordChangeLogServiceImpl
	/*extends StrengthenBaseServiceImpl<CscpUserPasswordChangeLogRepository, CscpUserPasswordChangeLog, Integer, CscpUserPasswordChangeLogExample>*/
	implements CscpUserPasswordChangeLogService {

	@Autowired
	private CscpUserPasswordChangeLogRepository cscpUserPasswordChangeLogRepository;

	@Override
	public int insert(CscpUserPasswordChangeLog cscpUserPasswordChangeLog) {
		return cscpUserPasswordChangeLogRepository.insert(cscpUserPasswordChangeLog);
	}
}
