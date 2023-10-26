package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.criteria.LongCriteria;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformColumnExample;
import com.ctsi.ssdc.customize.service.CscpHxDformColumnService;
import com.ctsi.ssdc.customize.repository.CscpHxDformColumnRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service Implementation for managing CscpHxDformColumn.
 *
 * @author hx
 * @date 2022-11-14 14:49:28
 *
 */

@Service
public class CscpHxDformColumnServiceImpl
        extends StrengthenBaseServiceImpl<CscpHxDformColumnRepository, CscpHxDformColumn, Long, CscpHxDformColumnExample>
        implements CscpHxDformColumnService {

    @Resource
    private CscpHxDformColumnRepository cscpHxDformColumnRepository;


    /**
     * 批量删除
     * @param columnIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] columnIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(columnIds));
        // 批量删除
        cscpHxDformColumnRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long columnId) {
        cscpHxDformColumnRepository.deleteByPrimaryKey(columnId);
    }

    @Override
    public void deleteByTableIds(List<Long> tableIds) {
        cscpHxDformColumnRepository.deleteByTableIds(tableIds);
    }

    @Override
    public Integer deleteByTable(Long id) {
        CscpHxDformColumnExample columnExample = new CscpHxDformColumnExample();
        columnExample.setTableId((LongCriteria) (new LongCriteria()).setEquals(id));
        columnExample.buildCriteria();
        return r.deleteByExample(columnExample);
    }

    @Override
    public List<CscpHxDformColumn> getFieldListByTable(Long id) {
        return r.selectByTableId(id);
    }

}