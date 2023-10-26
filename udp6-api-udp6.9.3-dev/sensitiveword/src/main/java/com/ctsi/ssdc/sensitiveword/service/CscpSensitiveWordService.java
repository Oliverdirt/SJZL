package com.ctsi.ssdc.sensitiveword.service;

import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordExample;
import com.ctsi.ssdc.service.StrengthenBaseService;
import java.io.InputStream;

/**
 * Service Interface for managing CscpSensitiveWord.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpSensitiveWordService 
	extends StrengthenBaseService<CscpSensitiveWord, Long, CscpSensitiveWordExample>{

    void saveUploadFile(InputStream inputStream);

    AjaxResult deleteByIds(Long[] ids);
}
