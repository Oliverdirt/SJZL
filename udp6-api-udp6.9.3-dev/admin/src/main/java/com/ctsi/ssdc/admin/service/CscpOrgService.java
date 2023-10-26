package com.ctsi.ssdc.admin.service;


import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpOrgDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpOrgParamDTO;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpOrg.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpOrgService {

    /**
     * insert a cscpOrg.
     *
     * @param cscpOrgDTO the entity to insert
     * @return the persisted entity
     */
    CscpOrgDTO insert(CscpOrgDTO cscpOrgDTO);
    
   
    /**
     * update a cscpOrg.
     *
     * @param cscpOrgDTO the entity to update
     * @return the persisted entity
     */
    CscpOrgDTO update(CscpOrgDTO cscpOrgDTO);
    
    /**
     * save cscporg and cscporg_user cscp_org_workGroup
     */
    Long save(CscpOrgParamDTO cscpOrgParamDTO);

    //新增删除信息
    void deleteAll(CscpOrgParamDTO cscpOrgParamDTO);


    CscpOrgParamDTO fetchCscpOrgsUpdate(Long parentId);
    /**
     * Get all the cscpOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpOrgDTO> findAll();

    /**
     * Get the  cscpOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpOrgDTO findOne(Long id);

    /**
     * Delete the  cscpOrg.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpOrgs.
     *
     * @return the list of entities
     */
    PageResult<CscpOrgDTO> findByCscpOrgDTO(CscpOrgDTO cscpOrgDTO, Pageable page);

 	/**
     * Get the cscpOrgs.
     *
     * @param cscpOrgCriteria
     * @param page
     * @return
     */
	PageResult<CscpOrgDTO> findByCscpOrgCriteria(CscpOrgCriteria cscpOrgCriteria, Pageable page);

    Integer getOrgOrderBy(CscpOrgDTO cscpOrgDTO);

}
