package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing CscpRoles.
 *
 * @author ctsi biyi generator
 */
public interface CscpRolesService {

    /**
     * insert a cscpRoles.
     *
     * @param cscpRolesDTO the entity to insert
     * @return the persisted entity
     */
    CscpRolesDTO insert(CscpRolesDTO cscpRolesDTO);


    /**
     * update a cscpRoles.
     *
     * @param cscpRolesDTO the entity to update
     * @return the persisted entity
     */
    CscpRolesDTO update(CscpRolesDTO cscpRolesDTO);

    /**
     * Get all the cscpRoless.
     *
     * @return the list of entities
     */
    PageResult<CscpRolesDTO> findAll();

    /**
     * Get the  cscpRoles.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpRolesDTO findOne(Long id);

    /**
     * Delete the  cscpRoles.
     *
     * @param id the id of the entity
     */
    void delete(Long id);


    /**
     * Get the cscpRoless.
     *
     * @return the list of entities
     */
    PageResult<CscpRolesDTO> findByCscpRolesDTO(CscpRolesDTO cscpRolesDTO, Pageable page);

    /**
     * Get the cscpRoless.
     *
     * @param cscpRolesCriteria
     * @param page
     * @return
     */
    PageResult<CscpRolesDTO> findByCscpRolesCriteria(CscpRolesCriteria cscpRolesCriteria, Pageable page);

    /**
     * 批量添加用户
     *
     * @param roleId
     * @param userIds
     * @return
     */
    String saveUsers(Long roleId, Long[] userIds);


    List<CscpUser> getUsersByRoleId(Long roleId);

    List<CscpUser> getUsersByRoleIds(Set<Long> roleId);

    CscpRoles getRoleByTenant(CscpRoles roles);

    /**
     * 校验角色是否允许操作
     *
     * @param roles 角色信息
     */
    void checkRoleAllowed(CscpRoles roles);

    /**
     * 修改数据权限信息
     *
     * @param roles 角色信息
     * @return 结果
     */
    int authDataScope(CscpRoles roles);

    List<CscpRoles> getNameById(List<Long> ids);

}
