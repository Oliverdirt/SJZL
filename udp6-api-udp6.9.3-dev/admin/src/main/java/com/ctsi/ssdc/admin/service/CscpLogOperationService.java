package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpLogOperation.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpLogOperationService {
	
    /**
     * insert a cscpLogOperation.
     *
     * @param cscpLogOperationDTO the entity to insert
     * @return the persisted entity
     */
    CscpLogOperationDTO insert(CscpLogOperationDTO cscpLogOperationDTO);
   
    /**
     * update a cscpLogOperation.
     *
     * @param cscpLogOperationDTO the entity to update
     * @return the persisted entity
     */
    CscpLogOperationDTO update(CscpLogOperationDTO cscpLogOperationDTO); 
	
    /**
     * Get all the cscpLogOperations.
     *
     * @return the list of entities
     */
    PageResult<CscpLogOperationDTO> findAll();

    /**
     * Get the  cscpLogOperation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpLogOperationDTO findOne(Long id);
	
    /**
     * Delete the  cscpLogOperation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
     /**
     * Get the cscpLogOperations.
     *
     * @return the list of entities
     */
    PageResult<CscpLogOperationDTO> findByCscpLogOperationDTO(CscpLogOperationDTO cscpLogOperationDTO, Pageable page);

 	/**
     * Get the cscpLogOperations.
     *
     * @param cscpLogOperationCriteria
     * @param page
     * @return
     */
	PageResult<CscpLogOperationDTO> findByCscpLogOperationCriteria(
	        CscpLogOperationCriteria cscpLogOperationCriteria, Pageable page);

}
