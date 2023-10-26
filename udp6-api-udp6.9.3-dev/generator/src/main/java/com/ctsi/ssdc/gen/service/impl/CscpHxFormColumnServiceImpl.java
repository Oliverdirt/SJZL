package com.ctsi.ssdc.gen.service.impl;


import com.ctsi.ssdc.criteria.LongCriteria;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumnExample;
import com.ctsi.ssdc.gen.service.CscpHxFormColumnService;
import com.ctsi.ssdc.gen.repository.CscpHxFormColumnRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CscpHxFormColumn.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxFormColumnServiceImpl 
	extends StrengthenBaseServiceImpl<CscpHxFormColumnRepository, CscpHxFormColumn, Long, CscpHxFormColumnExample> 
	implements CscpHxFormColumnService {

    @Autowired
    CscpHxFormColumnRepository cscpHxFormColumnRepository;


    /**
    * GET  /cscpHxFormColumns : get the cscpHxFormColumns firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpHxFormColumn> findFirstStringColumn(String columnName,Pageable pageable){
        String str = columnName;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpHxFormColumnExample cscpHxFormColumnExample = new CscpHxFormColumnExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpHxFormColumnExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpHxFormColumnExample.createCriteria();
        } else{
            cscpHxFormColumnExample.createCriteria().andColumnNameLike("%" + str +"%");
        }
        List<CscpHxFormColumn>  data = cscpHxFormColumnRepository.selectByExample(cscpHxFormColumnExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpHxFormColumnRepository.countByExample(cscpHxFormColumnExample);
        }
        return new PageResult<CscpHxFormColumn>(data,count,count);
    }

    /**
     * 根据表单id删除字段
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteByTable(Long id) {
        CscpHxFormColumnExample columnExample = new CscpHxFormColumnExample();
        columnExample.setTableId((LongCriteria) (new LongCriteria()).setEquals(id));
        columnExample.buildCriteria();
        return r.deleteByExample(columnExample);
    }

    /**
     * 根据表单id查找表单字段列表
     *
     * @param id
     * @return
     */
    @Override
    public List<CscpHxFormColumn> getFieldListByTable(Long id) {
        return r.selectByTableId(id);
    }

    private String getPageOrderBy(Pageable page) {
        if(page!= null && page.getSort() != null) {
            StringBuilder sb = new StringBuilder();
            page.getSort().forEach(sort -> sb.append(sort.getProperty())
            .append(" ").append(sort.getDirection()).append(","));
            if(sb.length() > 1) {
                return (sb.substring(0,sb.length()-1));
             }
        }
        return null;
    }

    /**
     * 根据表单id批量删除column
     *
     * @param tableIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableIds(List<Long> tableIds) {
        cscpHxFormColumnRepository.deleteByTableIds(tableIds);
    }

}
