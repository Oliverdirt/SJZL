package com.ctsi.ssdc.admin.service.impl;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrg;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrgExample;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrgExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgDTO;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupOrgRepository;
import com.ctsi.ssdc.admin.service.CscpWorkGroupOrgService;
import com.ctsi.ssdc.admin.service.mapper.CscpWorkGroupOrgMapper;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;

/**
 * Service Implementation for managing CscpWorkGroupOrg.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpWorkGroupOrgServiceImpl implements CscpWorkGroupOrgService {

    private final Logger log = LoggerFactory.getLogger(CscpWorkGroupOrgServiceImpl.class);

    @Autowired
    private CscpWorkGroupOrgRepository cscpWorkGroupOrgRepository;

    private final CscpWorkGroupOrgMapper cscpWorkGroupOrgMapper;
    

    public CscpWorkGroupOrgServiceImpl(CscpWorkGroupOrgMapper cscpWorkGroupOrgMapper) {
        this.cscpWorkGroupOrgMapper = cscpWorkGroupOrgMapper;
    }

    /**
     * insert a cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpWorkGroupOrgDTO insert(CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO) {
        log.debug("Request to insert CscpWorkGroupOrg : {}", cscpWorkGroupOrgDTO);
        
        CscpWorkGroupOrg cscpWorkGroupOrg = cscpWorkGroupOrgMapper.toEntity(cscpWorkGroupOrgDTO);
        cscpWorkGroupOrgRepository.insert(cscpWorkGroupOrg);
        return cscpWorkGroupOrgMapper.toDto(cscpWorkGroupOrg);
    }
    
     /**
     * update a cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpWorkGroupOrgDTO update(CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO) {
        log.debug("Request to update CscpWorkGroupOrg : {}", cscpWorkGroupOrgDTO);
        
        CscpWorkGroupOrg cscpWorkGroupOrg = cscpWorkGroupOrgMapper.toEntity(cscpWorkGroupOrgDTO);
        cscpWorkGroupOrgRepository.updateByPrimaryKeySelective(cscpWorkGroupOrg);
        return cscpWorkGroupOrgMapper.toDto(cscpWorkGroupOrg);
    }
    
    

    /**
     * Get all the cscpWorkGroupOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpWorkGroupOrgDTO> findAll() {
        log.debug("Request to get all CscpWorkGroupOrgs");
        
        List<CscpWorkGroupOrgDTO> data = cscpWorkGroupOrgRepository.selectByExample(null).stream()
                .map(cscpWorkGroupOrgMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupOrgRepository.countByExample(null);
		}
		
		return new PageResult<CscpWorkGroupOrgDTO>(data,count,count);        
        
    }

    /**
     * Get one cscpWorkGroupOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpWorkGroupOrgDTO findOne(Long id) {
        log.debug("Request to get CscpWorkGroupOrg : {} ", id);
        
        CscpWorkGroupOrg cscpWorkGroupOrg = cscpWorkGroupOrgRepository.selectByPrimaryKey(id);
        return cscpWorkGroupOrgMapper.toDto(cscpWorkGroupOrg);
    }

    /**
     * Delete the cscpWorkGroupOrg .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpWorkGroupOrg : {} ", id);
        
        cscpWorkGroupOrgRepository.deleteByPrimaryKey(id);
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
     * Get the cscpWorkGroupOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpWorkGroupOrgDTO> findByCscpWorkGroupOrgDTO(
    		CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO, Pageable page) {
    
        log.debug("Request to find CscpWorkGroupOrgs");
        
    	CscpWorkGroupOrgExample example = new CscpWorkGroupOrgExample();
    	
    	example.setPage(page);
		
		Criteria critieria = example.createCriteria();
		
		if(cscpWorkGroupOrgDTO.getId() != null) {
			critieria.andIdEqualTo(cscpWorkGroupOrgDTO.getId());
		}		
		if(cscpWorkGroupOrgDTO.getGroupId() != null) {
			critieria.andGroupIdEqualTo(cscpWorkGroupOrgDTO.getGroupId());
		}		
		if(cscpWorkGroupOrgDTO.getOrgId() != null) {
			critieria.andOrgIdEqualTo(cscpWorkGroupOrgDTO.getOrgId());
		}		

		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpWorkGroupOrgDTO> data = cscpWorkGroupOrgMapper
				.toDto(cscpWorkGroupOrgRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupOrgRepository.countByExample(example);
		}
		
		return new PageResult<CscpWorkGroupOrgDTO>(data,count,count);       
		
    }
    
    /**
     * Get the cscpWorkGroupOrgs.
     *
     * @param cscpWorkGroupOrgCriteria
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
	@Override
	public PageResult<CscpWorkGroupOrgDTO> findByCscpWorkGroupOrgCriteria(
			CscpWorkGroupOrgCriteria cscpWorkGroupOrgCriteria, Pageable page) {
	
		CscpWorkGroupOrgExample example = new CscpWorkGroupOrgExample();
    	
    	example.setPage(page);
		
		Criteria criteria = example.createCriteria();
		
		if(cscpWorkGroupOrgCriteria != null) {
			cscpWorkGroupOrgCriteria.buildCriteria(criteria);
		}		

		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpWorkGroupOrgDTO> data = cscpWorkGroupOrgMapper.toDto(
				cscpWorkGroupOrgRepository.selectByExamplewithPage(example));
		
		long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupOrgRepository.countByExample(example);
		}
		
		return new PageResult<CscpWorkGroupOrgDTO>(data,count,count);    
		
	}
    
    
}
