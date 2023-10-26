package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPage;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPageExample;
import com.ctsi.ssdc.customize.repository.CscpCustomizeVfieldPageRepository;
import com.ctsi.ssdc.customize.service.CscpCustomizeVfieldPageService;
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
public class CscpCustomizeVfieldPageServiceImpl
	extends StrengthenBaseServiceImpl<CscpCustomizeVfieldPageRepository, CscpCustomizeVfieldPage, Long, CscpCustomizeVfieldPageExample>
	implements CscpCustomizeVfieldPageService {

    @Autowired
    private CscpCustomizeVfieldPageRepository cscpCustomizeVfieldPageRepository;


    /**
     * 批量删除
     * @param fieldIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] fieldIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(fieldIds));
        // 批量删除
        cscpCustomizeVfieldPageRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long fieldId) {
        cscpCustomizeVfieldPageRepository.deleteByPrimaryKey(fieldId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCscpCustomizeVfieldPages(List<CscpCustomizeVfieldPage> cscpCustomizeVfieldPages) {
        for (CscpCustomizeVfieldPage cscpCustomizeVfieldPage : cscpCustomizeVfieldPages) {
            cscpCustomizeVfieldPageRepository.insert(cscpCustomizeVfieldPage);
        }
    }

    @Override
    public void updateCscpCustomizeVfieldPages(List<CscpCustomizeVfieldPage> cscpCustomizeVfieldPages) {
        for (CscpCustomizeVfieldPage cscpCustomizeVfieldPage : cscpCustomizeVfieldPages) {
            cscpCustomizeVfieldPageRepository.updateByPrimaryKey(cscpCustomizeVfieldPage);
        }
    }


    @Override
    public List<CscpCustomizeVfieldPage> getListByPageId(Long pageId) {
        return cscpCustomizeVfieldPageRepository.getListByPageId(pageId);
    }

    @Override
    public List<CscpCustomizeVfieldPage> getListByPageIds(Long[] pageIds) {
        List<Long> pageIdList = Arrays.asList(pageIds);
        if(CollectionUtils.isEmpty(pageIdList)){
            return new ArrayList<>();
        }
        return cscpCustomizeVfieldPageRepository.getListByPageIds(pageIdList);
    }

    @Override
    public void deleteByPageIdBatch(List<Long> pageIds) {
        cscpCustomizeVfieldPageRepository.deleteByPageIdBatch(pageIds);
    }

}
