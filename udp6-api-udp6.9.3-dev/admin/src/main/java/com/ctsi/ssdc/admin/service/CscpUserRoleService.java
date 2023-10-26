package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleDTO;
import com.ctsi.ssdc.model.PageResult;

import java.util.List;

/**
 * Service Interface for managing CscpUserRole.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserRoleService {

    /**
     * insert a cscpUserRole.
     *
     * @param cscpUserRoleDTO the entity to insert
     * @return the persisted entity
     */
    CscpUserRoleDTO insert(CscpUserRoleDTO cscpUserRoleDTO);
    
   
    /**
     * update a cscpUserRole.
     *
     * @param cscpUserRoleDTO the entity to update
     * @return the persisted entity
     */
    CscpUserRoleDTO update(CscpUserRoleDTO cscpUserRoleDTO); 

    /**
     * Get all the cscpUserRoles.
     *
     * @return the list of entities
     */
    PageResult<CscpUserRoleDTO> findAll();

    /**
     * Get the  cscpUserRole.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpUserRoleDTO findOne(Long id);

    /**
     * Delete the  cscpUserRole.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpUserRoles.
     *
     * @return the list of entities
     */
    PageResult<CscpUserRoleDTO> findByCscpUserRoleDTO(CscpUserRoleDTO cscpUserRoleDTO, Pageable page);

 	/**
     * Get the cscpUserRoles.
     *
     * @param cscpUserRoleCriteria
     * @param page
     * @return
     */
	PageResult<CscpUserRoleDTO> findByCscpUserRoleCriteria(
	        CscpUserRoleCriteria cscpUserRoleCriteria, Pageable page);
	
	public String saveUserRoles(Long userId, String roles);

    Long getUsersByRoleId(Long roleId);

    Long findRoleIdByUserId(Long userId);

    List<CscpUserRole> selectByUserId(Long userId);
    List<CscpUserRole> selectUserByRoleId(List<Long> list );
}
