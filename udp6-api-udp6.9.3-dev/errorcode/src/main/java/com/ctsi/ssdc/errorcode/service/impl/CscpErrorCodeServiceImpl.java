package com.ctsi.ssdc.errorcode.service.impl;

import com.ctsi.ssdc.errorcode.domain.CscpErrorCode;
import com.ctsi.ssdc.errorcode.domain.CscpErrorCodeExample;
import com.ctsi.ssdc.errorcode.exception.enums.ErrorCodeTypeEnum;
import com.ctsi.ssdc.errorcode.repository.CscpErrorCodeRepository;
import com.ctsi.ssdc.errorcode.service.CscpErrorCodeService;
import com.ctsi.ssdc.errorcode.vo.ErrorCodeCreateReqVO;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Service Implementation for managing CscpErrorCode.
 *
 * @author hx
 * @date 2022-09-05 15:56:17
 *
 */

@Service
public class CscpErrorCodeServiceImpl
        extends StrengthenBaseServiceImpl<CscpErrorCodeRepository, CscpErrorCode, Long, CscpErrorCodeExample>
        implements CscpErrorCodeService {

    @Autowired
    private CscpErrorCodeRepository cscpErrorCodeRepository;


    /**
     * 批量删除
     * @param codeIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] codeIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(codeIds));
        // 批量删除
        cscpErrorCodeRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long codeId) {
        cscpErrorCodeRepository.deleteByPrimaryKey(codeId);
    }

    @Override
    public PageResult<CscpErrorCode> getCscpErrorCodes(CscpErrorCode cscpErrorCode, Pageable pageable) {
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        List<CscpErrorCode> codeList = cscpErrorCodeRepository.getErrorCodes(cscpErrorCode);
        PageInfo<CscpErrorCode> pageInfo = new PageInfo<>(codeList);
        PageResult<CscpErrorCode> errorCodeResult = new PageResult<>();
        errorCodeResult.setData(pageInfo.getList());
        errorCodeResult.setRecordsTotal(pageInfo.getTotal());
        return errorCodeResult;
    }

    @Override
    public int checkErrorCode(CscpErrorCode cscpErrorCode) {
        if (cscpErrorCode.getCodeId() == null){
            return cscpErrorCodeRepository.checkErrorCode(cscpErrorCode);
        }else {
            CscpErrorCode cscpErrorCode1 = cscpErrorCodeRepository.selectByPrimaryKey(cscpErrorCode.getCodeId());
            if (cscpErrorCode1.getCode().equals(cscpErrorCode.getCode())){
                return 0;
            }else {
                return cscpErrorCodeRepository.checkErrorCode(cscpErrorCode);
            }
        }
    }

    @Override
    public int deleteErrorCodeByIds(Long[] codeIds) {
        List<Long> errorCodeList = new ArrayList<>(Arrays.asList(codeIds));
        return cscpErrorCodeRepository.deleteErrorCodeByIds(errorCodeList);
    }



}
