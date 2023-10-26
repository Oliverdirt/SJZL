package com.ctsi.ssdc.customize.service.impl;

import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelType;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelTypeExample;
import com.ctsi.ssdc.customize.service.CscpCustomizeModelTypeService;
import com.ctsi.ssdc.customize.repository.CscpCustomizeModelTypeRepository;

import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing CscpCustomizeModelType.
 *
 * @author hx
 * @date 2022-08-29 16:33:42
 *
 */

@Service
public class CscpCustomizeModelTypeServiceImpl
	extends StrengthenBaseServiceImpl<CscpCustomizeModelTypeRepository, CscpCustomizeModelType, Long, CscpCustomizeModelTypeExample>
	implements CscpCustomizeModelTypeService {

    @Autowired
    private CscpCustomizeModelTypeRepository cscpCustomizeModelTypeRepository;


    /**
     * 批量删除
     * @param modelTypeIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] modelTypeIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(modelTypeIds));
        // 批量删除
        cscpCustomizeModelTypeRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long modelTypeId) {
        cscpCustomizeModelTypeRepository.deleteByPrimaryKey(modelTypeId);
    }



}
