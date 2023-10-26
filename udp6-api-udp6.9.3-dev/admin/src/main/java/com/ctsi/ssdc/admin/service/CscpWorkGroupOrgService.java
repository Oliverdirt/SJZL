package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpWorkGroupOrg.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpWorkGroupOrgService {

    /**
     * insert a cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the entity to insert
     * @return the persisted entity
     */
    CscpWorkGroupOrgDTO insert(CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO);
    
   
    /**
     * update a cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the entity to update
     * @return the persisted entity
     */
    CscpWorkGroupOrgDTO update(CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO); 

    /**
     * Get all the cscpWorkGroupOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpWorkGroupOrgDTO> findAll();

    /**
     * Get the  cscpWorkGroupOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpWorkGroupOrgDTO findOne(Long id);

    /**
     * Delete the  cscpWorkGroupOrg.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpWorkGroupOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpWorkGroupOrgDTO> findByCscpWorkGroupOrgDTO(CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO, Pageable page);

 	/**
     * Get the cscpWorkGroupOrgs.
     *
     * @param cscpWorkGroupOrgCriteria
     * @param page
     * @return
     */
	PageResult<CscpWorkGroupOrgDTO> findByCscpWorkGroupOrgCriteria(
	        CscpWorkGroupOrgCriteria cscpWorkGroupOrgCriteria, Pageable page);

}
