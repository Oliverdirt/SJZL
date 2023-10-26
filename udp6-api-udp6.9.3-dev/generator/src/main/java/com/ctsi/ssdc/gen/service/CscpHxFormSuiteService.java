package com.ctsi.ssdc.gen.service;



import com.ctsi.ssdc.gen.domain.CscpHxFormSuite;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuiteExample;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing CodeTemplateSuite.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxFormSuiteService
	extends StrengthenBaseService<CscpHxFormSuite, Long, CscpHxFormSuiteExample> {

	CscpHxFormSuite insert(CscpHxFormSuite codeTemplateSuite, MultipartFile multipartFile);

	CscpHxFormSuite update(CscpHxFormSuite codeTemplateSuite, MultipartFile multipartFile);

}
