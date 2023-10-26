package com.ctsi.ssdc.errorcode.service;

import com.ctsi.ssdc.errorcode.domain.CscpErrorCode;
import com.ctsi.ssdc.errorcode.domain.CscpErrorCodeExample;
import com.ctsi.ssdc.errorcode.vo.ErrorCodeCreateReqVO;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CscpErrorCode.
 *
 * @author hx
 * @date 2022-09-05 15:50:09
 *
 */

public interface CscpErrorCodeService
        extends StrengthenBaseService<CscpErrorCode,Long , CscpErrorCodeExample>{

    /**
     * 批量删除
     * @param codeIds
     */
    void deleteByIds(Long[] codeIds);

    void deleteById(Long codeId);

    PageResult<CscpErrorCode> getCscpErrorCodes(CscpErrorCode cscpErrorCode, Pageable pageable);

    int checkErrorCode(CscpErrorCode cscpErrorCode);

    int deleteErrorCodeByIds(Long[] codeIds);


}
