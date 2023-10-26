package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.CscpLogOperation;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationDTO;
import com.ctsi.ssdc.admin.repository.CscpLogOperationRepository;
import com.ctsi.ssdc.admin.service.CscpLogOperationService;
import com.ctsi.ssdc.admin.service.mapper.CscpLogOperationMapper;
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
 * Service Implementation for managing CscpLogOperation.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpLogOperationServiceImpl implements CscpLogOperationService {

    private final Logger log = LoggerFactory.getLogger(CscpLogOperationServiceImpl.class);

    @Autowired
    private CscpLogOperationRepository cscpLogOperationRepository;

    private final CscpLogOperationMapper cscpLogOperationMapper;
    

    public CscpLogOperationServiceImpl(CscpLogOperationMapper cscpLogOperationMapper) {
        this.cscpLogOperationMapper = cscpLogOperationMapper;
    }

    /**
     * insert a cscpLogOperation.
     *
     * @param cscpLogOperationDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpLogOperationDTO insert(CscpLogOperationDTO cscpLogOperationDTO) {
        log.debug("Request to insert CscpLogOperation : {}", cscpLogOperationDTO);
        
        CscpLogOperation cscpLogOperation = cscpLogOperationMapper.toEntity(cscpLogOperationDTO);
        cscpLogOperationRepository.insert(cscpLogOperation);
        return cscpLogOperationMapper.toDto(cscpLogOperation);
    }
    
     /**
     * update a cscpLogOperation.
     *
     * @param cscpLogOperationDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpLogOperationDTO update(CscpLogOperationDTO cscpLogOperationDTO) {
        log.debug("Request to update CscpLogOperation : {}", cscpLogOperationDTO);
        
        CscpLogOperation cscpLogOperation = cscpLogOperationMapper.toEntity(cscpLogOperationDTO);
        cscpLogOperationRepository.updateByPrimaryKeySelective(cscpLogOperation);
        return cscpLogOperationMapper.toDto(cscpLogOperation);
    }
    

    /**
     * Get all the cscpLogOperations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpLogOperationDTO> findAll() {
        log.debug("Request to get all CscpLogOperations");
        
        List<CscpLogOperationDTO> data = cscpLogOperationRepository.selectByExample(null).stream()
                .map(cscpLogOperationMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogOperationRepository.countByExample(null);
        }
        
        return new PageResult<>(data,count,count);        
        
    }

    /**
     * Get one cscpLogOperation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpLogOperationDTO findOne(Long id) {
        log.debug("Request to get CscpLogOperation : {} ", id);
        
        CscpLogOperation cscpLogOperation = cscpLogOperationRepository.selectByPrimaryKey(id);
        return cscpLogOperationMapper.toDto(cscpLogOperation);
    }

    /**
     * Delete the cscpLogOperation .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpLogOperation : {} ", id);
        
        cscpLogOperationRepository.deleteByPrimaryKey(id);
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
     * Get the cscpLogOperations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpLogOperationDTO> findByCscpLogOperationDTO(
            CscpLogOperationDTO cscpLogOperationDTO, Pageable page) {
    	String likeFormat = "%%%s%%";
    	
        log.debug("Request to find CscpLogOperations");
        
        CscpLogOperationExample example = new CscpLogOperationExample();
        
        example.setPage(page);
        
        Criteria critieria = example.createCriteria();
        
        if(cscpLogOperationDTO.getId() != null) {
            critieria.andIdEqualTo(cscpLogOperationDTO.getId());
        }		
        if(cscpLogOperationDTO.getUserid() != null) {
            critieria.andUseridEqualTo(cscpLogOperationDTO.getUserid());
        }
        if(cscpLogOperationDTO.getTime() != null) {
            critieria.andTenantIdEqualTo(cscpLogOperationDTO.getTeantId());
        }
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getUsername())) {
            critieria.andUsernameLike(String.format(likeFormat, cscpLogOperationDTO.getUsername()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getUri())) {
            critieria.andUriLike(String.format(likeFormat, cscpLogOperationDTO.getUri()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getIp())) {
            critieria.andIpLike(String.format(likeFormat, cscpLogOperationDTO.getIp()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getParams())) {
            critieria.andParamsLike(String.format(likeFormat, cscpLogOperationDTO.getParams()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getMethod())) {
            critieria.andMethodLike(String.format(likeFormat, cscpLogOperationDTO.getMethod()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getMessage())) {
            critieria.andMessageLike(String.format(likeFormat, cscpLogOperationDTO.getMessage()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getStatus())) {
            critieria.andStatusLike(String.format(likeFormat, cscpLogOperationDTO.getStatus()));
        }		
        if(StringUtils.isNotBlank(cscpLogOperationDTO.getError())) {
            critieria.andErrorLike(String.format(likeFormat, cscpLogOperationDTO.getError()));
        }		
        
        String orderBy = getPageOrderBy(page);

        if(StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        
        if(page != null) {
            PageHelper.startPage(page.getPageNumber()+1, page.getPageSize());
        }

        List<CscpLogOperationDTO> data = cscpLogOperationMapper
                .toDto(cscpLogOperationRepository.selectByExamplewithPage(example));
    
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogOperationRepository.countByExample(example);
        }
        
        return new PageResult<>(data,count,count);       
        
    }
    
    /**
     * Get the cscpLogOperations.
     *
     * @param cscpLogOperationCriteria
     * @param page
     * @return
     */
    @Override
    public PageResult<CscpLogOperationDTO> findByCscpLogOperationCriteria(
            CscpLogOperationCriteria cscpLogOperationCriteria, Pageable page) {
    
        CscpLogOperationExample example = new CscpLogOperationExample();
        
        example.setPage(page);
        
        Criteria criteria = example.createCriteria();
        
        if(cscpLogOperationCriteria != null) {
            cscpLogOperationCriteria.buildCriteria(criteria);
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
        
        List<CscpLogOperationDTO> data = cscpLogOperationMapper
                .toDto(cscpLogOperationRepository.selectByExamplewithPage(example));
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = cscpLogOperationRepository.countByExample(example);
        }
        
        return new PageResult<>(data,count,count);    
        
    }
    
    
}
