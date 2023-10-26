package com.ctsi.ssdc.gen.service.impl;

import com.ctsi.ssdc.gen.domain.CscpCustomizeVfield;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVfieldExample;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVfieldRepository;
import com.ctsi.ssdc.gen.service.CscpCustomizeVfieldService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Service Implementation for managing CscpCustomizeVfield.
 *
 * @author hx
 * @date 2022-05-23 09:59:35
 *
 */

@Service
public class CscpCustomizeVfieldServiceImpl
	extends StrengthenBaseServiceImpl<CscpCustomizeVfieldRepository, CscpCustomizeVfield, Long, CscpCustomizeVfieldExample>
	implements CscpCustomizeVfieldService {

    @Autowired
    private CscpCustomizeVfieldRepository cscpCustomizeVfieldRepository;


    /**
     * 批量删除
     * @param fieldIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] fieldIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(fieldIds));
        // 批量删除
        cscpCustomizeVfieldRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long fieldId) {
        cscpCustomizeVfieldRepository.deleteByPrimaryKey(fieldId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCscpCustomizeVfields(List<CscpCustomizeVfield> cscpCustomizeVfields) {
        for (CscpCustomizeVfield cscpCustomizeVfield : cscpCustomizeVfields) {
            cscpCustomizeVfieldRepository.insert(cscpCustomizeVfield);
        }
    }

    @Override
    public void updateCscpCustomizeVfields(List<CscpCustomizeVfield> cscpCustomizeVfields) {
        for (CscpCustomizeVfield cscpCustomizeVfield : cscpCustomizeVfields) {
            cscpCustomizeVfieldRepository.updateByPrimaryKey(cscpCustomizeVfield);
        }
    }

    @Override
    public List<CscpCustomizeVfield> getListByFormId(Long formId) {
        return cscpCustomizeVfieldRepository.getListByFormId(formId);
    }

    @Override
    public List<CscpCustomizeVfield> getListByFormIds(Long[] formIds) {
        List<Long> formIdList = Arrays.asList(formIds);
        if(CollectionUtils.isEmpty(formIdList)){
            return new ArrayList<>();
        }
        return cscpCustomizeVfieldRepository.getListByFormIds(formIdList);
    }

}
