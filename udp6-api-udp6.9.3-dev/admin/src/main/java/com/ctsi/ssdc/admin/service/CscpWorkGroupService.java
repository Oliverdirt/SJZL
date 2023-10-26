package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpWorkGroupService {

    /**
     * insert a cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the entity to insert
     * @return the persisted entity
     */
    CscpWorkGroupDTO insert(CscpWorkGroupDTO cscpWorkGroupDTO);
    
   
    /**
     * update a cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the entity to update
     * @return the persisted entity
     */
    CscpWorkGroupDTO update(CscpWorkGroupDTO cscpWorkGroupDTO); 

    /**
     * Get all the cscpWorkGroups.
     *
     * @return the list of entities
     */
    PageResult<CscpWorkGroupDTO> findAll();

    /**
     * Get the  cscpWorkGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpWorkGroupDTO findOne(Long id);

    /**
     * Delete the  cscpWorkGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpWorkGroups.
     *
     * @return the list of entities
     */
    PageResult<CscpWorkGroupDTO> findByCscpWorkGroupDTO(CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page);

 	/**
     * Get the cscpWorkGroups.
     *
     * @param cscpWorkGroupCriteria
     * @param page
     * @return
     */
	PageResult<CscpWorkGroupDTO> findByCscpWorkGroupCriteria(
	        CscpWorkGroupCriteria cscpWorkGroupCriteria, Pageable page);
	
	PageResult<CscpWorkGroupDTO> findByCscpWorkGroupDtoOr(
	        CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page);
	
}
