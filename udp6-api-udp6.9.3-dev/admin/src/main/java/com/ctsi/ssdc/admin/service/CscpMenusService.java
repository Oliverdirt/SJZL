package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.domain.CscpRoleMenu;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.model.MenuItemBean;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.TreeMenuSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CscpMenus.
 *
 * @author ctsi biyi generator
 *
 */
public interface CscpMenusService {

    /**
     * insert a cscpMenus.
     *
     * @param cscpMenusDTO the entity to insert
     * @return the persisted entity
     */
    CscpMenusDTO insert(CscpMenusDTO cscpMenusDTO);
    
   
    /**
     * update a cscpMenus.
     *
     * @param cscpMenusDTO the entity to update
     * @return the persisted entity
     */
    CscpMenusDTO update(CscpMenusDTO cscpMenusDTO); 

    /**
     * Get all the cscpMenuss.
     *
     * @return the list of entities
     */
    PageResult<CscpMenusDTO> findAll();

    //查找List格式
    List<CscpMenus> findAllList();

    /**
     * Get the  cscpMenus by userId.
     *
     * @param id the id of the entity
     * @return the entity
     */
    List<CscpMenusDTO> findByUserId(Long id);

    
    /**
     * Get the  cscpMenus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CscpMenusDTO findOne(Long id);
    
    /**
     * Delete the  cscpMenus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    
     /**
     * Get the cscpMenuss.
     *
     * @return the list of entities
     */
    PageResult<CscpMenusDTO> findByCscpMenusDTO(CscpMenusDTO cscpMenusDTO, Pageable page);



 	/**
     * Get the cscpMenuss.
     *
     * @param cscpMenusCriteria
     * @param page
     * @return
     */
	PageResult<CscpMenusDTO> findByCscpMenusCriteria(CscpMenusCriteria cscpMenusCriteria, Pageable page);

	MenuItemBean roleMenuList(Long roleId);
	
	void saveRoleMenus(Long roleId, String menus, String permissions);
	void addRoleMenu(Long roleId, Long menuId);
	//获取orderbyId
    Integer getOrderBy(CscpMenusDTO cscpMenusDTO);

	//按照父id查找menus
    List<CscpMenus>  findByParentId(Long parentId);

    List<CscpMenusDTO> findAllMenusByCondition(CscpMenusDTO cscpMenusDTO);

    CscpRoleMenu selectByMenuIdAndRoleId(Long menuId, Long roleId);
    int insert(CscpRoleMenu record);
    CscpMenus selectOneById(Long parentId);
    void addRoleMenuParentFor(Long id,List<Long> roleId);
    void addRoleMenuFor(CscpMenusDTO cscpMenusDto,CscpMenus parentMenu);
    void addRoleMenuByParentId(CscpMenusDTO cscpMenusDto,CscpMenus cscpMenus);
    void addRoleMenu(CscpMenusDTO cscpMenusDto,CscpMenusDTO old);
    List<CscpRoleMenu> selectByMenuId(Long menuId);
    void deleteMenusByMenuId(Long menuId);
    void addRoleMenuList(List<CscpRoleMenu> menu);
    List<CscpMenus> selectIdByParentId(Long parentId);
    List<Long> selectListByMenuId(Long menuId);
    void dragAddRole(Long parentId,List<Long> allRoleId);
    List<CscpMenus> selectById(Long id);
    void deleteMenusByMenuIdNo(Long menuId);

    /**
     * 根据角色id，删除旧菜单
     * @param roleId
     */
    void deleteRoleMenus(Long roleId);
    List<TreeMenuSelect> buildMenuTreeSelect(Long uid);

    CscpMenus getMenuByUrl(String url);
    /**
     * 根据urlList批量查询菜单列表
     * @param urlList
     * @return
     */
    List<CscpMenus> getMenuByUrlBatch(List<String> urlList);

    /**
     * 根据id 批量删除菜单
     * @param idList 菜单id列表
     */
    void deleteByIdBatch(List<Long> idList);

    MenuItemBean getMenuItemBean(Long roleId);

    List<CscpMenusDTO> findByRoleId(Long roleId);

    /**
     * 低代码同步子列表 针对按钮
     * @param cspMenusDTO 父及菜单 及设置默认admin权限
     */
    void synChildMenusBatchForLowCode(CscpMenusDTO cspMenusDTO);

    // 根据菜单名称查询菜单
    List<CscpMenus> qryMenuByModuleId(String moduleId);

    void updateChildMenusBatchForLowCode(CscpMenusDTO cspMenusDTO);
}
