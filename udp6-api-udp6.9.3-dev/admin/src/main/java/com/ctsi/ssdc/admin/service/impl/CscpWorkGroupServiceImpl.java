package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.CscpUserWorkGroupExample;
import com.ctsi.ssdc.admin.domain.CscpWorkGroup;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupExample;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupExample.Criteria;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrgExample;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupDTO;
import com.ctsi.ssdc.admin.repository.CscpUserWorkGroupRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupOrgRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupRepository;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.admin.service.CscpUserWorkGroupService;
import com.ctsi.ssdc.admin.service.CscpWorkGroupService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserWorkGroupMapper;
import com.ctsi.ssdc.admin.service.mapper.CscpWorkGroupMapper;
import com.ctsi.ssdc.model.PageResult;
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
 * Service Implementation for managing CscpWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpWorkGroupServiceImpl implements CscpWorkGroupService {

    private final Logger log = LoggerFactory.getLogger(CscpWorkGroupServiceImpl.class);

    @Autowired
    private CscpWorkGroupRepository cscpWorkGroupRepository;

    private final CscpWorkGroupMapper cscpWorkGroupMapper;
    
    private final CscpUserWorkGroupMapper cscpUserWorkGroupMapper;
    
    //private final CscpUserWorkGroupService cscpUserWorkGroupService;
    
    private final CscpUserDetailService cscpUserDetailService;
    
    @Autowired
    private CscpUserWorkGroupRepository cscpUserWorkGroupRepository;
    
    @Autowired
    private CscpWorkGroupOrgRepository cscpWorkGroupOrgRepository;
    

    public CscpWorkGroupServiceImpl(CscpWorkGroupMapper cscpWorkGroupMapper,
                                    CscpUserWorkGroupMapper cscpUserWorkGroupMapper,
            CscpUserWorkGroupService cscpUserWorkGroupService, CscpUserDetailService cscpUserDetailService) {
        this.cscpWorkGroupMapper = cscpWorkGroupMapper;
        //this.cscpUserWorkGroupService = cscpUserWorkGroupService;
        this.cscpUserDetailService = cscpUserDetailService;
        this.cscpUserWorkGroupMapper = cscpUserWorkGroupMapper;
    }

    /**
     * insert a cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    @Transactional
    public CscpWorkGroupDTO insert(CscpWorkGroupDTO cscpWorkGroupDTO) {
        log.debug("Request to insert CscpWorkGroup : {}", cscpWorkGroupDTO);
        
        CscpWorkGroup cscpWorkGroup = cscpWorkGroupMapper.toEntity(cscpWorkGroupDTO);
        cscpWorkGroupRepository.insert(cscpWorkGroup);
//        CscpWorkGroupDTO dto = cscpWorkGroupMapper.toDto(cscpWorkGroup);
        
      //保存cscpuserworkgroup
        //cscpUserWorkGroupService.deleteByWorkGroupId(cscpWorkGroup.getId());

        for(int i=0; i<cscpWorkGroupDTO.getUserDetail().length; i++){
            CscpUserWorkGroupDTO cscpUserWorkGroupDTO = new CscpUserWorkGroupDTO();
            cscpUserWorkGroupDTO.setUserId(cscpWorkGroupDTO.getUserDetail()[i].getUserId());
            cscpUserWorkGroupDTO.setGroupId(cscpWorkGroup.getId());
            cscpUserWorkGroupRepository.insert(cscpUserWorkGroupMapper.toEntity(cscpUserWorkGroupDTO));
        }
        
        return cscpWorkGroupMapper.toDto(cscpWorkGroup);
    }
    
     /**
     * update a cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the entity to update
     * @return the persisted entity
     */
    @Override
    @Transactional
    public CscpWorkGroupDTO update(CscpWorkGroupDTO cscpWorkGroupDTO) {
        log.debug("Request to update CscpWorkGroup : {}", cscpWorkGroupDTO);
        
        CscpWorkGroup cscpWorkGroup = cscpWorkGroupMapper.toEntity(cscpWorkGroupDTO);
        cscpWorkGroupRepository.updateByPrimaryKeySelective(cscpWorkGroup);
        
        //保存cscpuserworkgroup
        CscpUserWorkGroupExample example = new CscpUserWorkGroupExample();
        example.createCriteria().andGroupIdEqualTo(cscpWorkGroup.getId());
        cscpUserWorkGroupRepository.deleteByExample(example);

        //保存userDetail
        for(int i=0; i<cscpWorkGroupDTO.getUserDetail().length; i++){
            CscpUserWorkGroupDTO cscpUserWorkGroupDTO = new CscpUserWorkGroupDTO();
            cscpUserWorkGroupDTO.setUserId(cscpWorkGroupDTO.getUserDetail()[i].getUserId());
            cscpUserWorkGroupDTO.setGroupId(cscpWorkGroupDTO.getId());
            //cscpUserWorkGroupService.insert(cscpUserWorkGroupDTO);
            cscpUserWorkGroupRepository.insert(cscpUserWorkGroupMapper.toEntity(cscpUserWorkGroupDTO));
        }
        
        return cscpWorkGroupMapper.toDto(cscpWorkGroup);
    }
    
    

    /**
     * Get all the cscpWorkGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpWorkGroupDTO> findAll() {
        log.debug("Request to get all CscpWorkGroups");
        
        List<CscpWorkGroupDTO> data = cscpWorkGroupRepository.selectByExample(null).stream()
                .map(cscpWorkGroupMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupRepository.countByExample(null);
		}
		
		return new PageResult<CscpWorkGroupDTO>(data,count,count);        
        
    }

    /**
     * Get one cscpWorkGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpWorkGroupDTO findOne(Long id) {
        log.debug("Request to get CscpWorkGroup : {} ", id);
        
        CscpWorkGroup cscpWorkGroup = cscpWorkGroupRepository.selectByPrimaryKey(id);
        
        CscpWorkGroupDTO dto = cscpWorkGroupMapper.toDto(cscpWorkGroup);
        //查出相关的userdetail
        //bodhi
        List<CscpUserDetailDTO> list = cscpUserDetailService.selectByWorkGroupId(cscpWorkGroup.getId());
        dto.setUserDetail(list.toArray(new CscpUserDetailDTO[]{}));
        return dto;
    }

    /**
     * Delete the cscpWorkGroup .
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CscpWorkGroup : {} ", id);
        
        cscpWorkGroupRepository.deleteByPrimaryKey(id);
        
        //删除对应的用户；删除对应的org配置关系
        CscpUserWorkGroupExample example = new CscpUserWorkGroupExample();
        com.ctsi.ssdc.admin.domain.CscpUserWorkGroupExample.Criteria cx = example.createCriteria();
        cx.andGroupIdEqualTo(id);
        cscpUserWorkGroupRepository.deleteByExample(example );
        
        //删除与组织机构的关联关系
        CscpWorkGroupOrgExample example2 = new CscpWorkGroupOrgExample();
        com.ctsi.ssdc.admin.domain.CscpWorkGroupOrgExample.Criteria cx2 = example2.createCriteria();
        cx2.andGroupIdEqualTo(id);
        cscpWorkGroupOrgRepository.deleteByExample(example2);
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
     * Get the cscpWorkGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpWorkGroupDTO> findByCscpWorkGroupDTO(CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page) {
    
        log.debug("Request to find CscpWorkGroups");
        
    	CscpWorkGroupExample example = new CscpWorkGroupExample();
    	
    	example.setPage(page);
		
		Criteria critieria = example.createCriteria();
		
		if(cscpWorkGroupDTO.getId() != null) {
			critieria.andIdEqualTo(cscpWorkGroupDTO.getId());
		}		
		if(cscpWorkGroupDTO.getOrgId() != null) {
			critieria.andOrgIdEqualTo(cscpWorkGroupDTO.getOrgId());
		}		

		if(StringUtils.isNotBlank(cscpWorkGroupDTO.getGroupName())) {
			critieria.andGroupNameLike(String.format("%%%s%%", cscpWorkGroupDTO.getGroupName()));
		}		
		if(StringUtils.isNotBlank(cscpWorkGroupDTO.getDescription())) {
			critieria.andDescriptionLike(String.format("%%%s%%", cscpWorkGroupDTO.getDescription()));
		}		
		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpWorkGroupDTO> data = cscpWorkGroupMapper.toDto(
		        cscpWorkGroupRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupRepository.countByExample(example);
		}
		
		return new PageResult<CscpWorkGroupDTO>(data,count,count);       
		
    }
    
    /**
     * Get the cscpWorkGroups.
     *
     * @param cscpWorkGroupCriteria
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
	@Override
	public PageResult<CscpWorkGroupDTO> findByCscpWorkGroupCriteria(
	        CscpWorkGroupCriteria cscpWorkGroupCriteria, Pageable page) {
	
		CscpWorkGroupExample example = new CscpWorkGroupExample();
    	
    	example.setPage(page);
		
		Criteria criteria = example.createCriteria();
		
		if(cscpWorkGroupCriteria != null) {
			cscpWorkGroupCriteria.buildCriteria(criteria);
		}		

		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpWorkGroupDTO> data = cscpWorkGroupMapper.toDto(
		        cscpWorkGroupRepository.selectByExamplewithPage(example));
		
		long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupRepository.countByExample(example);
		}
		
		return new PageResult<CscpWorkGroupDTO>(data,count,count);    
		
	}

	@Override
	@Transactional(readOnly = true)
	public PageResult<CscpWorkGroupDTO> findByCscpWorkGroupDtoOr(CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page) {
		log.debug("Request to find CscpWorkGroups");
        
    	CscpWorkGroupExample example = new CscpWorkGroupExample();
    	
    	example.setPage(page);
		
		if(cscpWorkGroupDTO.getId() != null) {
			example.or().andIdEqualTo(cscpWorkGroupDTO.getId());
		}		
		if(cscpWorkGroupDTO.getOrgId() != null) {
			example.or().andOrgIdEqualTo(cscpWorkGroupDTO.getOrgId());
		}		

		if(StringUtils.isNotBlank(cscpWorkGroupDTO.getGroupName())) {
			example.or().andGroupNameLike(String.format("%%%s%%", cscpWorkGroupDTO.getGroupName()));
		}		
		if(StringUtils.isNotBlank(cscpWorkGroupDTO.getDescription())) {
			example.or().andDescriptionLike(String.format("%%%s%%", cscpWorkGroupDTO.getDescription()));
		}		
		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpWorkGroupDTO> data = cscpWorkGroupMapper.toDto(
		        cscpWorkGroupRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpWorkGroupRepository.countByExample(example);
		}
		
		return new PageResult<CscpWorkGroupDTO>(data,count,count);       
	}

	
}
