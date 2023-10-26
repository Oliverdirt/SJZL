package com.ctsi.ssdc.gen.service;


import com.ctsi.ssdc.gen.domain.CodeTemplateSuite;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuiteExample;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing CodeTemplateSuite.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CodeTemplateSuiteService 
	extends StrengthenBaseService<CodeTemplateSuite, Long, CodeTemplateSuiteExample>{

	CodeTemplateSuite insert(CodeTemplateSuite codeTemplateSuite, MultipartFile multipartFile);

	CodeTemplateSuite update(CodeTemplateSuite codeTemplateSuite, MultipartFile multipartFile);

	void deleteByIds(Long[] ids);
}
