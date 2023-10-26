package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.CscpLogLogin;
import com.ctsi.ssdc.admin.domain.CscpLogLoginExample;
import com.ctsi.ssdc.admin.domain.CscpLogLoginExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginDTO;
import com.ctsi.ssdc.admin.repository.CscpLogLoginRepository;
import com.ctsi.ssdc.admin.service.CscpLogLoginService;
import com.ctsi.ssdc.admin.service.mapper.CscpLogLoginMapper;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpLogLogin.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpLogLoginServiceImpl implements CscpLogLoginService {

    private final Logger log = LoggerFactory.getLogger(CscpLogLoginServiceImpl.class);

    @Autowired
    private CscpLogLoginRepository cscpLogLoginRepository;

    private final CscpLogLoginMapper cscpLogLoginMapper;
    

    public CscpLogLoginServiceImpl(CscpLogLoginMapper cscpLogLoginMapper) {
         this.cscpLogLoginMapper = cscpLogLoginMapper;
    }

    /**
     * insert a cscpLogLogin.
     *
     * @param cscpLogLoginDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpLogLoginDTO insert(CscpLogLoginDTO cscpLogLoginDTO) {
        log.debug("Request to insert CscpLogLogin : {}", cscpLogLoginDTO);
        
        CscpLogLogin cscpLogLogin = cscpLogLoginMapper.toEntity(cscpLogLoginDTO);
        cscpLogLoginRepository.insert(cscpLogLogin);
        return cscpLogLoginMapper.toDto(cscpLogLogin);
    }
    
     /**
     * update a cscpLogLogin.
     *
     * @param cscpLogLoginDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpLogLoginDTO update(CscpLogLoginDTO cscpLogLoginDTO) {
        log.debug("Request to update CscpLogLogin : {}", cscpLogLoginDTO);
        
        CscpLogLogin cscpLogLogin = cscpLogLoginMapper.toEntity(cscpLogLoginDTO);
        cscpLogLoginRepository.updateByPrimaryKeySelective(cscpLogLogin);
        return cscpLogLoginMapper.toDto(cscpLogLogin);
    }
    

    /**
     * Get all the cscpLogLogins.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpLogLoginDTO> findAll() {
        log.debug("Request to get all CscpLogLogins");
        
        List<CscpLogLoginDTO> data = cscpLogLoginRepository.selectByExample(null).stream()
                .map(cscpLogLoginMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogLoginRepository.countByExample(null);
        }
        
        return new PageResult<>(data,count,count);        
        
    }

    /**
     * Get one cscpLogLogin.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpLogLoginDTO findOne(Long id) {
        log.debug("Request to get CscpLogLogin : {} ", id);
        
        CscpLogLogin cscpLogLogin = cscpLogLoginRepository.selectByPrimaryKey(id);
        return cscpLogLoginMapper.toDto(cscpLogLogin);
    }

    /**
     * Delete the cscpLogLogin .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpLogLogin : {} ", id);
        
        cscpLogLoginRepository.deleteByPrimaryKey(id);
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
     * Get the cscpLogLogins.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpLogLoginDTO> findByCscpLogLoginDTO(CscpLogLoginDTO cscpLogLoginDTO, Pageable page) {
    	String likeFormat = "%%%s%%";
    	
        log.debug("Request to find CscpLogLogins");
        
        CscpLogLoginExample example = new CscpLogLoginExample();
        
        example.setPage(page);
        
        Criteria critieria = example.createCriteria();
        
        if(cscpLogLoginDTO.getId() != null) {
            critieria.andIdEqualTo(cscpLogLoginDTO.getId());
        }
        if(cscpLogLoginDTO.getTeantId() != null) {
            critieria.andTenantIdEqualTo(cscpLogLoginDTO.getTeantId());
        }

        if(StringUtils.isNotBlank(cscpLogLoginDTO.getUserName())) {
            critieria.andUserNameLike(String.format(likeFormat, cscpLogLoginDTO.getUserName()));
        }		
        if(StringUtils.isNotBlank(cscpLogLoginDTO.getIp())) {
            critieria.andIpLike(String.format(likeFormat, cscpLogLoginDTO.getIp()));
        }		
        if(StringUtils.isNotBlank(cscpLogLoginDTO.getMessage())) {
            critieria.andMessageLike(String.format(likeFormat, cscpLogLoginDTO.getMessage()));
        }		
        if(StringUtils.isNotBlank(cscpLogLoginDTO.getStatus())) {
            critieria.andStatusLike(String.format(likeFormat, cscpLogLoginDTO.getStatus()));
        }		
        
        String orderBy = getPageOrderBy(page);

        if(StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        
        if(page != null) {
            PageHelper.startPage(page.getPageNumber()+1, page.getPageSize());
        }

        List<CscpLogLoginDTO> data = cscpLogLoginMapper.toDto(cscpLogLoginRepository.selectByExamplewithPage(example));
    
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogLoginRepository.countByExample(example);
        }
        
        return new PageResult<>(data,count,count);       
        
    }
    
    /**
     * Get the cscpLogLogins.
     *
     * @param cscpLogLoginCriteria
     * @param page
     * @return
     */
    @Override
    public PageResult<CscpLogLoginDTO> findByCscpLogLoginCriteria(
            CscpLogLoginCriteria cscpLogLoginCriteria, Pageable page) {
    
        CscpLogLoginExample example = new CscpLogLoginExample();
        
        example.setPage(page);
        
        Criteria criteria = example.createCriteria();
        
        if(cscpLogLoginCriteria != null) {
            cscpLogLoginCriteria.buildCriteria(criteria);
        }
        LongCriteria longCriteria = new LongCriteria();
        longCriteria.setEquals(SecurityHxUtils.getCurrentTenantId());
        example.setTenantId(longCriteria);
        String orderBy = getPageOrderBy(page);

        if(StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        if(page != null) {
            PageHelper.startPage(page.getPageNumber()+1, page.getPageSize());
        }
        
        List<CscpLogLoginDTO> data = cscpLogLoginMapper.toDto(cscpLogLoginRepository.selectByExamplewithPage(example));
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogLoginRepository.countByExample(example);
        }
        
        return new PageResult<>(data,count,count);    
        
    }
    
    
}
