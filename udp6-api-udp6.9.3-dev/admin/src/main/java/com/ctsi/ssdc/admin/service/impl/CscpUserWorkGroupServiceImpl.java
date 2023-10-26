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

import com.ctsi.ssdc.admin.domain.CscpUserWorkGroup;
import com.ctsi.ssdc.admin.domain.CscpUserWorkGroupExample;
import com.ctsi.ssdc.admin.domain.CscpUserWorkGroupExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupDTO;
import com.ctsi.ssdc.admin.repository.CscpUserWorkGroupRepository;
import com.ctsi.ssdc.admin.service.CscpUserWorkGroupService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserWorkGroupMapper;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;

/**
 * Service Implementation for managing CscpUserWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpUserWorkGroupServiceImpl implements CscpUserWorkGroupService {

    private final Logger log = LoggerFactory.getLogger(CscpUserWorkGroupServiceImpl.class);

    @Autowired
    private CscpUserWorkGroupRepository cscpUserWorkGroupRepository;

    private final CscpUserWorkGroupMapper cscpUserWorkGroupMapper;
    

    public CscpUserWorkGroupServiceImpl( CscpUserWorkGroupMapper cscpUserWorkGroupMapper) {
        this.cscpUserWorkGroupMapper = cscpUserWorkGroupMapper;
    }

    /**
     * insert a cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpUserWorkGroupDTO insert(CscpUserWorkGroupDTO cscpUserWorkGroupDTO) {
        log.debug("Request to insert CscpUserWorkGroup : {}", cscpUserWorkGroupDTO);
        
        CscpUserWorkGroup cscpUserWorkGroup = cscpUserWorkGroupMapper.toEntity(cscpUserWorkGroupDTO);
        cscpUserWorkGroupRepository.insert(cscpUserWorkGroup);
        return cscpUserWorkGroupMapper.toDto(cscpUserWorkGroup);
    }
    
     /**
     * update a cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpUserWorkGroupDTO update(CscpUserWorkGroupDTO cscpUserWorkGroupDTO) {
        log.debug("Request to update CscpUserWorkGroup : {}", cscpUserWorkGroupDTO);
        
        CscpUserWorkGroup cscpUserWorkGroup = cscpUserWorkGroupMapper.toEntity(cscpUserWorkGroupDTO);
        cscpUserWorkGroupRepository.updateByPrimaryKeySelective(cscpUserWorkGroup);
        return cscpUserWorkGroupMapper.toDto(cscpUserWorkGroup);
    }
    
    

    /**
     * Get all the cscpUserWorkGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserWorkGroupDTO> findAll() {
        log.debug("Request to get all CscpUserWorkGroups");
        
        List<CscpUserWorkGroupDTO> data = cscpUserWorkGroupRepository.selectByExample(null).stream()
                .map(cscpUserWorkGroupMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserWorkGroupRepository.countByExample(null);
		}
		
		return new PageResult<CscpUserWorkGroupDTO>(data,count,count);        
        
    }

    /**
     * Get one cscpUserWorkGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpUserWorkGroupDTO findOne(Long id) {
        log.debug("Request to get CscpUserWorkGroup : {} ", id);
        
        CscpUserWorkGroup cscpUserWorkGroup = cscpUserWorkGroupRepository.selectByPrimaryKey(id);
        return cscpUserWorkGroupMapper.toDto(cscpUserWorkGroup);
    }

    /**
     * Delete the cscpUserWorkGroup .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpUserWorkGroup : {} ", id);
        
        cscpUserWorkGroupRepository.deleteByPrimaryKey(id);
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
     * Get the cscpUserWorkGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserWorkGroupDTO> findByCscpUserWorkGroupDTO(
            CscpUserWorkGroupDTO cscpUserWorkGroupDTO, Pageable page) {
    
        log.debug("Request to find CscpUserWorkGroups");
        
    	CscpUserWorkGroupExample example = new CscpUserWorkGroupExample();
    	
    	example.setPage(page);
		
		Criteria critieria = example.createCriteria();
		
		if(cscpUserWorkGroupDTO.getId() != null) {
			critieria.andIdEqualTo(cscpUserWorkGroupDTO.getId());
		}		
		if(cscpUserWorkGroupDTO.getUserId() != null) {
			critieria.andUserIdEqualTo(cscpUserWorkGroupDTO.getUserId());
		}		
		if(cscpUserWorkGroupDTO.getGroupId() != null) {
			critieria.andGroupIdEqualTo(cscpUserWorkGroupDTO.getGroupId());
		}		

		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}

		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserWorkGroupDTO> data = cscpUserWorkGroupMapper.
                toDto(cscpUserWorkGroupRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserWorkGroupRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserWorkGroupDTO>(data,count,count);       
		
    }
    
    /**
     * Get the cscpUserWorkGroups.
     *
     * @param cscpUserWorkGroupCriteria
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    @Override
	public PageResult<CscpUserWorkGroupDTO> findByCscpUserWorkGroupCriteria(
	        CscpUserWorkGroupCriteria cscpUserWorkGroupCriteria, Pageable page) {
	
		CscpUserWorkGroupExample example = new CscpUserWorkGroupExample();
    	
    	example.setPage(page);
		
		Criteria criteria = example.createCriteria();
		
		if(cscpUserWorkGroupCriteria != null) {
			cscpUserWorkGroupCriteria.buildCriteria(criteria);
		}		

		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserWorkGroupDTO> data = cscpUserWorkGroupMapper.
                toDto(cscpUserWorkGroupRepository.selectByExamplewithPage(example));
		
		long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserWorkGroupRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserWorkGroupDTO>(data,count,count);    
		
	}

    @Override
    public void deleteByWorkGroupId(Long wgId) {
        // TODO Auto-generated method stub
        CscpUserWorkGroupExample example = new CscpUserWorkGroupExample();
        Criteria critieria = example.createCriteria();
        critieria.andGroupIdEqualTo(wgId);
        cscpUserWorkGroupRepository.deleteByExample(example);
    }
    
    
}
