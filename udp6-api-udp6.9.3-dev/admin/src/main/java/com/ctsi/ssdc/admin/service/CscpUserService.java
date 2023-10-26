package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.service.impl.CscpUserServiceImpl;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.dto.CscpUserCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserPasswordUpdate;
import com.ctsi.ssdc.model.PageResult;

/**
 * Service Interface for managing CscpUser.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserService {

    /**
     * insert a cscpUser.
     *
     * @param cscpUserDTO the entity to insert
     * @return the persisted entity
     */
    CscpUserDTO insert(CscpUserDTO cscpUserDTO);
    
   
    /**
     * update a cscpUser.
     *
     * @param cscpUserDTO the entity to update
     * @return the persisted entity
     */
    CscpUserDTO update(CscpUserDTO cscpUserDTO); 
    
    /**
     * 根据用户ID修改用户密码
     *
     * @param cscpUserPasswordUpdate the entity to update
     * @param checkOld
     * @return the persisted entity
     */
    CscpUserServiceImpl.UpdatePasswordResult updatePassword(
            CscpUserPasswordUpdate cscpUserPasswordUpdate,boolean checkOld) throws Exception;

    /**
     * Get all the cscpUsers.
     *
     * @return the list of entities
     */
    PageResult<CscpUserDTO> findAll();

    /**
     * Get the  cscpUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpUserDTO findOne(Long id);

    /**
     * Delete the  cscpUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpUsers.
     *
     * @return the list of entities
     */
    PageResult<CscpUserDTO> findByCscpUserDTO(CscpUserDTO cscpUserDTO, Pageable page);

 	/**
     * Get the cscpUsers.
     *
     * @param cscpUserCriteria
     * @param page
     * @return
     */
	PageResult<CscpUserDTO> findByCscpUserCriteria(CscpUserCriteria cscpUserCriteria, Pageable page);
	/**
	 * 验证用户名是否存在
	 * @param username
	 * @return
	 */
	boolean existByUsername(String username);

    /**
     * 验证口令是否过期 0：未过期 1：初始口令需要修改 2：口令超3个月过期
     * @param userId
     * @param pwd
     * @return
     */
    int passwordNeedChange(Long userId,String pwd);


    /**
     * Get the cscpUsers.
     *
     * @param username
     * @return
     */
    CscpUser findByUsername(String username);

    public String decryptPassword(String ciphertext);

    boolean deblockingAccount(String username);
}
