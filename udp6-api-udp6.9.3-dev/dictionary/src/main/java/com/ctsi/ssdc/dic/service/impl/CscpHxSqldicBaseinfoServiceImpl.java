package com.ctsi.ssdc.dic.service.impl;


import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfo;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfoExample;
import com.ctsi.ssdc.dic.service.CscpHxSqldicBaseinfoService;
import com.ctsi.ssdc.dic.repository.CscpHxSqldicBaseinfoRepository;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
/**
 * Service Implementation for managing CscpHxSqldicBaseinfo.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxSqldicBaseinfoServiceImpl 
	extends StrengthenBaseServiceImpl<CscpHxSqldicBaseinfoRepository, CscpHxSqldicBaseinfo, Long, CscpHxSqldicBaseinfoExample> 
	implements CscpHxSqldicBaseinfoService {

    @Autowired
    CscpHxSqldicBaseinfoRepository cscpHxSqldicBaseinfoRepository;


    /**
    * GET  /cscpHxSqldicBaseinfos : get the cscpHxSqldicBaseinfos firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpHxSqldicBaseinfo> findFirstStringColumn(String dicSelectSql,Pageable pageable){
        String str = dicSelectSql;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpHxSqldicBaseinfoExample cscpHxSqldicBaseinfoExample = new CscpHxSqldicBaseinfoExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpHxSqldicBaseinfoExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpHxSqldicBaseinfoExample.createCriteria();
        } else{
            cscpHxSqldicBaseinfoExample.createCriteria().andDicSelectSqlLike("%" + str +"%");
        }
        List<CscpHxSqldicBaseinfo>  data = cscpHxSqldicBaseinfoRepository.selectByExample(cscpHxSqldicBaseinfoExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpHxSqldicBaseinfoRepository.countByExample(cscpHxSqldicBaseinfoExample);
        }
        return new PageResult<CscpHxSqldicBaseinfo>(data,count,count);
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
}
