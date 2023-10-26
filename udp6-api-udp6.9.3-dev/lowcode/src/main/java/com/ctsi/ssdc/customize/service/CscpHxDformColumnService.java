package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformColumnExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpHxDformColumn.
 *
 * @author hx
 * @date 2022-11-14 14:49:28
 *
 */

public interface CscpHxDformColumnService
        extends StrengthenBaseService<CscpHxDformColumn,Long , CscpHxDformColumnExample>{

    /**
     * 批量删除
     * @param columnIds
     */
    void deleteByIds(Long[] columnIds);

    void deleteById(Long columnId);

    void deleteByTableIds(List<Long> delList);

    Integer deleteByTable(Long id);

    List<CscpHxDformColumn> getFieldListByTable(Long id);
}
