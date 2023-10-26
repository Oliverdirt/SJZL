package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpUserOrg.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserOrgService {

    /**
     * insert a cscpUserOrg.
     *
     * @param cscpUserOrgDTO the entity to insert
     * @return the persisted entity
     */
    CscpUserOrgDTO insert(CscpUserOrgDTO cscpUserOrgDTO);
    
   
    /**
     * update a cscpUserOrg.
     *
     * @param cscpUserOrgDTO the entity to update
     * @return the persisted entity
     */
    CscpUserOrgDTO update(CscpUserOrgDTO cscpUserOrgDTO); 

    /**
     * Get all the cscpUserOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpUserOrgDTO> findAll();

    /**
     * Get the  cscpUserOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpUserOrgDTO findOne(Long id);

    /**
     * Delete the  cscpUserOrg.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpUserOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpUserOrgDTO> findByCscpUserOrgDTO(CscpUserOrgDTO cscpUserOrgDTO, Pageable page);

 	/**
     * Get the cscpUserOrgs.
     *
     * @param cscpUserOrgCriteria
     * @param page
     * @return
     */
	PageResult<CscpUserOrgDTO> findByCscpUserOrgCriteria(CscpUserOrgCriteria cscpUserOrgCriteria, Pageable page);

    /**
     * 机构人员唯一校验
     * @param users
     * @return
     */
	int userOnlyOne(Long orgId,String[] users);
}
