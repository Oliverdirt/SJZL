package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpUserWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserWorkGroupService {

    /**
     * insert a cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the entity to insert
     * @return the persisted entity
     */
    CscpUserWorkGroupDTO insert(CscpUserWorkGroupDTO cscpUserWorkGroupDTO);
    
   
    /**
     * update a cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the entity to update
     * @return the persisted entity
     */
    CscpUserWorkGroupDTO update(CscpUserWorkGroupDTO cscpUserWorkGroupDTO); 

    /**
     * Get all the cscpUserWorkGroups.
     *
     * @return the list of entities
     */
    PageResult<CscpUserWorkGroupDTO> findAll();

    /**
     * Get the  cscpUserWorkGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpUserWorkGroupDTO findOne(Long id);

    /**
     * Delete the  cscpUserWorkGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
    /**
     * delete by workgroup id;
     * edit by bodhi
     * @param wgId
     */
    void deleteByWorkGroupId(Long wgId);
    
    
     /**
     * Get the cscpUserWorkGroups.
     *
     * @return the list of entities
     */
    PageResult<CscpUserWorkGroupDTO> findByCscpUserWorkGroupDTO(
            CscpUserWorkGroupDTO cscpUserWorkGroupDTO, Pageable page);

 	/**
     * Get the cscpUserWorkGroups.
     *
     * @param cscpUserWorkGroupCriteria
     * @param page
     * @return
     */
	PageResult<CscpUserWorkGroupDTO> findByCscpUserWorkGroupCriteria(
	        CscpUserWorkGroupCriteria cscpUserWorkGroupCriteria, Pageable page);

}
