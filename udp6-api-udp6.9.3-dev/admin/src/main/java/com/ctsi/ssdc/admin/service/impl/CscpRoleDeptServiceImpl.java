package com.ctsi.ssdc.admin.service.impl;


import org.springframework.stereotype.Service;

import java.lang.Long;

import com.ctsi.ssdc.admin.domain.CscpRoleDept;
import com.ctsi.ssdc.admin.domain.CscpRoleDeptExample;
import com.ctsi.ssdc.admin.service.CscpRoleDeptService;
import com.ctsi.ssdc.admin.repository.CscpRoleDeptRepository;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;

/**
 * Service Implementation for managing CscpRoleDept.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpRoleDeptServiceImpl 
	extends StrengthenBaseServiceImpl<CscpRoleDeptRepository, CscpRoleDept, Long, CscpRoleDeptExample> 
	implements CscpRoleDeptService {

}
