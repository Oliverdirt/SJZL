package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.CscpUserDetailLike;
import com.ctsi.ssdc.admin.domain.FlowUserTaskInfo;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserUpdateDTO;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing CscpUserDetail.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpUserDetailService {

    /**
     * insert a cscpUserDetail.
     *
     * @param cscpUserDetailDTO the entity to insert
     * @return the persisted entity
     */
    CscpUserDetailDTO insert(CscpUserDetailDTO cscpUserDetailDTO);
    
   
    /**
     * update a cscpUserDetail.
     *
     * @param cscpUserDetailDTO the entity to update
     * @return the persisted entity
     */
    CscpUserDetailDTO update(CscpUserDetailDTO cscpUserDetailDTO); 


    //当前管理员修改用户密码
    CscpUserDetailDTO userPwdUpdate(CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception;
    /**
     * Get all the cscpUserDetails.
     *
     * @return the list of entities
     */
    PageResult<CscpUserDetailDTO> findAll();

    /**
     * Get the  cscpUserDetail.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpUserDetailDTO findOne(Long id);
    
    /**
     * get list by user work group id
     * @param id
     * @return
     */
    List<CscpUserDetailDTO> selectByWorkGroupId(Long id);
    
    /**
     * get list by user work group id
     * @param userId
     * @return
     */
    CscpUserDetailDTO selectByUserId(Long userId);

    /**
     * Delete the  cscpUserDetail.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpUserDetails.
     *
     * @return the list of entities
     */
    PageResult<CscpUserDetailDTO> findByCscpUserDetailDTO(CscpUserDetailDTO cscpUserDetailDTO, Pageable page);

 	/**
     * Get the cscpUserDetails.
     *
     * @param cscpUserDetailCriteria
     * @param page
     * @return
     */
	PageResult<CscpUserDetailDTO> findByCscpUserDetailCriteria(
	        CscpUserDetailCriteria cscpUserDetailCriteria, Pageable page);

	PageResult<CscpUserDetailDTO> findByCscpUserDetailDtoOr(CscpUserDetailLike cscpUserDetailLike, Pageable page);

	PageResult<CscpUserDetailDTO> findByUserId(String userId, Pageable page);
    /**
     * 根据userId获取用户详情信息
     *
     * @param userId
     * @return
     */
	CscpUserDetailDTO findByUserId(Long userId);

    List<CscpUserDetailDTO> selectByTenantId(Long tenantId);

    /**
     * 修改用户信息
     * @param cscpUserUpdateDTO
     * @return
     */
    CscpUserDetailDTO updateUserDTO(CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception;


    FlowUserTaskInfo findFlowUserTaskInfo();


    /**
     * @Description: 根据部门id查询用户信息
     * @Author: sunsheng
     **/
    List<CscpUser> getUsersByDeptIds(Set<Long> idSet);

}
