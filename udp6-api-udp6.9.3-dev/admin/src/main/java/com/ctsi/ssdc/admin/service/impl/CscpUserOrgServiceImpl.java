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

import com.ctsi.ssdc.admin.domain.CscpUserOrg;
import com.ctsi.ssdc.admin.domain.CscpUserOrgExample;
import com.ctsi.ssdc.admin.domain.CscpUserOrgExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgDTO;
import com.ctsi.ssdc.admin.repository.CscpUserOrgRepository;
import com.ctsi.ssdc.admin.service.CscpUserOrgService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserOrgMapper;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;

/**
 * Service Implementation for managing CscpUserOrg.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpUserOrgServiceImpl implements CscpUserOrgService {

    private final Logger log = LoggerFactory.getLogger(CscpUserOrgServiceImpl.class);

    @Autowired
    private CscpUserOrgRepository cscpUserOrgRepository;

    private final CscpUserOrgMapper cscpUserOrgMapper;
    

    public CscpUserOrgServiceImpl(CscpUserOrgMapper cscpUserOrgMapper) {
        this.cscpUserOrgMapper = cscpUserOrgMapper;
    }

    /**
     * insert a cscpUserOrg.
     *
     * @param cscpUserOrgDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpUserOrgDTO insert(CscpUserOrgDTO cscpUserOrgDTO) {
        log.debug("Request to insert CscpUserOrg : {}", cscpUserOrgDTO);
        
        CscpUserOrg cscpUserOrg = cscpUserOrgMapper.toEntity(cscpUserOrgDTO);
        cscpUserOrgRepository.insert(cscpUserOrg);
        return cscpUserOrgMapper.toDto(cscpUserOrg);
    }
    
     /**
     * update a cscpUserOrg.
     *
     * @param cscpUserOrgDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpUserOrgDTO update(CscpUserOrgDTO cscpUserOrgDTO) {
        log.debug("Request to update CscpUserOrg : {}", cscpUserOrgDTO);
        
        CscpUserOrg cscpUserOrg = cscpUserOrgMapper.toEntity(cscpUserOrgDTO);
        cscpUserOrgRepository.updateByPrimaryKeySelective(cscpUserOrg);
        return cscpUserOrgMapper.toDto(cscpUserOrg);
    }
    
    

    /**
     * Get all the cscpUserOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserOrgDTO> findAll() {
        log.debug("Request to get all CscpUserOrgs");
        
        List<CscpUserOrgDTO> data = cscpUserOrgRepository.selectByExample(null).stream()
                .map(cscpUserOrgMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserOrgRepository.countByExample(null);
		}
		
		return new PageResult<CscpUserOrgDTO>(data,count,count);        
        
    }

    /**
     * Get one cscpUserOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpUserOrgDTO findOne(Long id) {
        log.debug("Request to get CscpUserOrg : {} ", id);
        
        CscpUserOrg cscpUserOrg = cscpUserOrgRepository.selectByPrimaryKey(id);
        return cscpUserOrgMapper.toDto(cscpUserOrg);
    }

    /**
     * Delete the cscpUserOrg .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpUserOrg : {} ", id);
        
        cscpUserOrgRepository.deleteByPrimaryKey(id);
        //删除组织机构和用户，组织机构和工作组
        
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
     * Get the cscpUserOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserOrgDTO> findByCscpUserOrgDTO(CscpUserOrgDTO cscpUserOrgDTO, Pageable page) {
    
        log.debug("Request to find CscpUserOrgs");
        
    	CscpUserOrgExample example = new CscpUserOrgExample();
    	
    	example.setPage(page);
		
		Criteria critieria = example.createCriteria();
		
		if(cscpUserOrgDTO.getId() != null) {
			critieria.andIdEqualTo(cscpUserOrgDTO.getId());
		}		
		if(cscpUserOrgDTO.getUserId() != null) {
			critieria.andUserIdEqualTo(cscpUserOrgDTO.getUserId());
		}		
		if(cscpUserOrgDTO.getOrgId() != null) {
			critieria.andOrgIdEqualTo(cscpUserOrgDTO.getOrgId());
		}		

		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserOrgDTO> data = cscpUserOrgMapper.
				toDto(cscpUserOrgRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserOrgRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserOrgDTO>(data,count,count);       
		
    }
    
    /**
     * Get the cscpUserOrgs.
     *
     * @param cscpUserOrgCriteria
     * @param page
     * @return
     */
    @Override
	public PageResult<CscpUserOrgDTO> findByCscpUserOrgCriteria(
			CscpUserOrgCriteria cscpUserOrgCriteria, Pageable page) {
	
		CscpUserOrgExample example = new CscpUserOrgExample();
    	
    	example.setPage(page);
		
		Criteria criteria = example.createCriteria();
		
		if(cscpUserOrgCriteria != null) {
			cscpUserOrgCriteria.buildCriteria(criteria);
		}		

		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserOrgDTO> data = cscpUserOrgMapper.toDto(
				cscpUserOrgRepository.selectByExamplewithPage(example));
		
		long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserOrgRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserOrgDTO>(data,count,count);    
		
	}

	@Override
	public int userOnlyOne(Long orgId,String[] users) {
		for (String user : users) {
			int i = cscpUserOrgRepository.userOnlyOne(orgId,Long.valueOf(user));
			if(i>0){
				return 0;
			}
		}

		return 1;
	}


}
