package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpLogLogin.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpLogLoginService {
	
    /**
     * insert a cscpLogLogin.
     *
     * @param cscpLogLoginDTO the entity to insert
     * @return the persisted entity
     */
    CscpLogLoginDTO insert(CscpLogLoginDTO cscpLogLoginDTO);
   
    /**
     * update a cscpLogLogin.
     *
     * @param cscpLogLoginDTO the entity to update
     * @return the persisted entity
     */
    CscpLogLoginDTO update(CscpLogLoginDTO cscpLogLoginDTO); 
	
    /**
     * Get all the cscpLogLogins.
     *
     * @return the list of entities
     */
    PageResult<CscpLogLoginDTO> findAll();

    /**
     * Get the  cscpLogLogin.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpLogLoginDTO findOne(Long id);
	
    /**
     * Delete the  cscpLogLogin.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
     /**
     * Get the cscpLogLogins.
     *
     * @return the list of entities
     */
    PageResult<CscpLogLoginDTO> findByCscpLogLoginDTO(CscpLogLoginDTO cscpLogLoginDTO, Pageable page);

 	/**
     * Get the cscpLogLogins.
     *
     * @param cscpLogLoginCriteria
     * @param page
     * @return
     */
	PageResult<CscpLogLoginDTO> findByCscpLogLoginCriteria(
	        CscpLogLoginCriteria cscpLogLoginCriteria, Pageable page);

}
