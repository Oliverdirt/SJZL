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

import com.ctsi.ssdc.admin.domain.CscpUserRole;
import com.ctsi.ssdc.admin.domain.CscpUserRoleExample;
import com.ctsi.ssdc.admin.domain.CscpUserRoleExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleDTO;
import com.ctsi.ssdc.admin.repository.CscpUserRoleRepository;
import com.ctsi.ssdc.admin.service.CscpUserRoleService;
import com.ctsi.ssdc.admin.service.mapper.CscpUserRoleMapper;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;

/**
 * Service Implementation for managing CscpUserRole.
 *
 * @author ctsi biyi generator
 *
 */
@Service
public class CscpUserRoleServiceImpl implements CscpUserRoleService {

    private final Logger log = LoggerFactory.getLogger(CscpUserRoleServiceImpl.class);

    @Autowired
    private CscpUserRoleRepository cscpUserRoleRepository;

    private final CscpUserRoleMapper cscpUserRoleMapper;
    

    public CscpUserRoleServiceImpl( CscpUserRoleMapper cscpUserRoleMapper) {
        this.cscpUserRoleMapper = cscpUserRoleMapper;
    }

    /**
     * insert a cscpUserRole.
     *
     * @param cscpUserRoleDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpUserRoleDTO insert(CscpUserRoleDTO cscpUserRoleDTO) {
        log.debug("Request to insert CscpUserRole : {}", cscpUserRoleDTO);
        
        CscpUserRole cscpUserRole = cscpUserRoleMapper.toEntity(cscpUserRoleDTO);
        cscpUserRoleRepository.insert(cscpUserRole);
        return cscpUserRoleMapper.toDto(cscpUserRole);
    }
    
     /**
     * update a cscpUserRole.
     *
     * @param cscpUserRoleDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpUserRoleDTO update(CscpUserRoleDTO cscpUserRoleDTO) {
        log.debug("Request to update CscpUserRole : {}", cscpUserRoleDTO);
        
        CscpUserRole cscpUserRole = cscpUserRoleMapper.toEntity(cscpUserRoleDTO);
        cscpUserRoleRepository.updateByPrimaryKeySelective(cscpUserRole);
        return cscpUserRoleMapper.toDto(cscpUserRole);
    }
    
    

    /**
     * Get all the cscpUserRoles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserRoleDTO> findAll() {
        log.debug("Request to get all CscpUserRoles");
        
        List<CscpUserRoleDTO> data = cscpUserRoleRepository.selectByExample(null).stream()
                .map(cscpUserRoleMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
                
        long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserRoleRepository.countByExample(null);
		}
		
		return new PageResult<CscpUserRoleDTO>(data,count,count);        
        
    }

    /**
     * Get one cscpUserRole.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpUserRoleDTO findOne(Long id) {
        log.debug("Request to get CscpUserRole : {} ", id);
        
        CscpUserRole cscpUserRole = cscpUserRoleRepository.selectByPrimaryKey(id);
        return cscpUserRoleMapper.toDto(cscpUserRole);
    }

    /**
     * Delete the cscpUserRole .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpUserRole : {} ", id);
        
        cscpUserRoleRepository.deleteByPrimaryKey(id);
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
     * Get the cscpUserRoles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpUserRoleDTO> findByCscpUserRoleDTO(CscpUserRoleDTO cscpUserRoleDTO, Pageable page) {
    
        log.debug("Request to find CscpUserRoles");
        
    	CscpUserRoleExample example = new CscpUserRoleExample();
    	
    	example.setPage(page);
		
		Criteria critieria = example.createCriteria();
		
		if(cscpUserRoleDTO.getId() != null) {
			critieria.andIdEqualTo(cscpUserRoleDTO.getId());
		}		
		if(cscpUserRoleDTO.getUserId() != null) {
			critieria.andUserIdEqualTo(cscpUserRoleDTO.getUserId());
		}		
		if(cscpUserRoleDTO.getRoleId() != null) {
			critieria.andRoleIdEqualTo(cscpUserRoleDTO.getRoleId());
		}		

		
		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserRoleDTO> data = cscpUserRoleMapper.
				toDto(cscpUserRoleRepository.selectByExamplewithPage(example));
    
     	long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserRoleRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserRoleDTO>(data,count,count);       
		
    }
    
    /**
     * Get the cscpUserRoles.
     *
     * @param cscpUserRoleCriteria
     * @param page
     * @return
     */
    @Override
	public PageResult<CscpUserRoleDTO> findByCscpUserRoleCriteria(
			CscpUserRoleCriteria cscpUserRoleCriteria, Pageable page) {
	
		CscpUserRoleExample example = new CscpUserRoleExample();
    	
    	example.setPage(page);
		
		Criteria criteria = example.createCriteria();
		
		if(cscpUserRoleCriteria != null) {
			cscpUserRoleCriteria.buildCriteria(criteria);
		}		

		String orderBy = getPageOrderBy(page);

		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		PageHelper.startPage(page.getPageNumber(), page.getPageSize());
		List<CscpUserRoleDTO> data = cscpUserRoleMapper.
				toDto(cscpUserRoleRepository.selectByExamplewithPage(example));
		
		long count = 0L;
		
		if(CollectionUtils.isNotEmpty(data)) {
			count = cscpUserRoleRepository.countByExample(example);
		}
		
		return new PageResult<CscpUserRoleDTO>(data,count,count);    
		
	}

	@Override
	@Transactional
	public String saveUserRoles(Long userId, String roles) {
		try {
			String[] ms = roles.split(",");

			CscpUserRoleExample example = new CscpUserRoleExample();
			example.or().andUserIdEqualTo(userId);
			cscpUserRoleRepository.deleteByExample(example);

			for (int i = 0; i < ms.length; i++) {
				CscpUserRole record = new CscpUserRole();
				record.setUserId(userId);
				record.setRoleId(Long.parseLong(ms[i]));
				cscpUserRoleRepository.insert(record);
			}
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	@Override
	public Long getUsersByRoleId(Long roleId) {

		CscpUserRoleExample example = new CscpUserRoleExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		long userNum = cscpUserRoleRepository.countByExample(example);

		return  Long.parseLong(String.valueOf(userNum));
	}

	/**
	 * 根据用户id查询对应角色id
	 * @param userId
	 */
    @Override
    public Long findRoleIdByUserId(Long userId) {
		CscpUserRole userRole = cscpUserRoleRepository.selectRoleIdByUserId(userId);
		return userRole.getRoleId();
	}

	@Override
	public List<CscpUserRole> selectByUserId(Long userId) {
		List<CscpUserRole> cscpUserRoles = cscpUserRoleRepository.selectByUserId(userId);
		return cscpUserRoles;
	}

	@Override
	public List<CscpUserRole> selectUserByRoleId(List<Long> list) {
		List<CscpUserRole> cscpUserRoles = cscpUserRoleRepository.selectUserByRoleId( list);
		return cscpUserRoles;
	}


}
