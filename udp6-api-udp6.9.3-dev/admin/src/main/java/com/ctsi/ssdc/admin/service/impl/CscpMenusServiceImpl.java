package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.constants.AdminFlagConstant;
import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.CscpMenusExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.repository.CscpMenusRepository;
import com.ctsi.ssdc.admin.repository.CscpRoleMenuRepository;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRoleRepository;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.admin.service.mapper.CscpMenusMapper;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.enums.LowCodeApiPermisionEnum;
import com.ctsi.ssdc.model.MenuItemBean;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.TreeMenuSelect;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpMenus.
 *
 * @author ctsi biyi generator
 */
//用于管理CscpMenus的服务实现。
@Service
public class CscpMenusServiceImpl implements CscpMenusService {
    private final Logger log = LoggerFactory.getLogger(CscpMenusServiceImpl.class);

    @Autowired
    private CscpMenusRepository cscpMenusRepository;

    private final CscpMenusMapper cscpMenusMapper;

    @Autowired
    private CscpRoleMenuRepository cscpRoleMenuRepository;

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    private CscpUserRoleRepository cscpUserRoleRepository;

    @Autowired
    private CscpRolesService cscpRolesService;

    public CscpMenusServiceImpl(CscpMenusMapper cscpMenusMapper) {
        this.cscpMenusMapper = cscpMenusMapper;
    }

    /**
     * insert a cscpMenus.
     *
     * @param cscpMenusDTO the entity to insert
     * @return the persisted entity
     */

   /* public CscpMenusDTO insert(CscpMenusDTO cscpMenusDTO) {
        log.debug("Request to insert CscpMenus : {}", cscpMenusDTO);

        CscpMenus cscpMenus = cscpMenusMapper.toEntity(cscpMenusDTO);
        cscpMenusRepository.insert(cscpMenus);
        int id = cscpMenus.getId();
        CscpMenus  cscpMenusOrder = new CscpMenus();
        cscpMenusOrder.setOrderby(id);
        CscpMenusExample menusExample =new CscpMenusExample();
        Criteria criteria = menusExample.createCriteria();
        criteria.andIdEqualTo(id);
        cscpMenusRepository.updateByExampleSelective(cscpMenusOrder,menusExample);
        return cscpMenusMapper.toDto(cscpMenus);
    }*/
    @Override
    //重写insert方法
    public CscpMenusDTO insert(CscpMenusDTO cscpMenusDTO) {
        log.debug("Request to insert CscpMenus : {}", cscpMenusDTO);
        List<CscpMenusDTO> childList = cscpMenusDTO.getChildMenusList();
        CscpMenus cscpMenus = cscpMenusMapper.toEntity(cscpMenusDTO);
        //获取orderby顺序
        cscpMenus.setOrderby(getOrderBy(cscpMenusDTO));
        cscpMenusRepository.insert(cscpMenus);
        CscpMenusDTO cscpMenusDto = cscpMenusMapper.toDto(cscpMenus);
        //针对低代码模块设计同步按钮菜单

        if(CollectionUtils.isEmpty(childList)){
            return cscpMenusDto;
        }
        cscpMenusDto.setChildMenusList(childList);
        this.synChildMenusBatchForLowCode(cscpMenusDto);
        return cscpMenusDto;
    }

    //获取orderby顺序
    @Override
    public Integer getOrderBy(CscpMenusDTO cscpMenusDTO) {
        CscpMenus cscpMenus = cscpMenusMapper.toEntity(cscpMenusDTO);
        Integer orderById;
        Long parentId = cscpMenus.getParentId();
        //获取相同parentId的孩子数量
        List<CscpMenus> childs = cscpMenusRepository.selectByParentId(parentId);
        if (childs.isEmpty()) {
            orderById = 1;
        } else {
            //最后一个孩子的顺序值
            orderById = childs.get(childs.size() - 1).getOrderby() + 1;
        }

        return orderById;
    }

    /**
     * update a cscpMenus.
     *
     * @param cscpMenusDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpMenusDTO update(CscpMenusDTO cscpMenusDTO) {
        log.debug("Request to update CscpMenus : {}", cscpMenusDTO);
        CscpMenus cscpMenus = cscpMenusMapper.toEntity(cscpMenusDTO);
        cscpMenusRepository.updateByPrimaryKeySelective(cscpMenus);
        if(CollectionUtils.isEmpty( cscpMenusDTO.getChildMenusList())){
            return cscpMenusMapper.toDto(cscpMenus);
        }
        this.updateChildMenusBatchForLowCode(cscpMenusDTO);
        return cscpMenusMapper.toDto(cscpMenus);
    }


    /**
     * Get all the cscpMenuss.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpMenusDTO> findAll() {
        log.debug("Request to get all CscpMenuss");

        List<CscpMenusDTO> data = cscpMenusRepository.selectByExample(null).stream()
                .map(cscpMenusMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpMenusRepository.countByExample(null);
        }

        return new PageResult<CscpMenusDTO>(data, count, count);

    }

    @Override
    public List<CscpMenus> findAllList() {
        log.debug("Request to get all CscpMenuss");
        return cscpMenusRepository.selectByExample(null);
    }

    /**
     * Get one cscpMenus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpMenusDTO findOne(Long id) {
        log.debug("Request to get CscpMenus : {} ", id);

        CscpMenus cscpMenus = cscpMenusRepository.selectByPrimaryKey(id);
        return cscpMenusMapper.toDto(cscpMenus);
    }

    /**
     * Delete the cscpMenus .
     * 递归删除节点下面所有子节点
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CscpMenus : {} ", id);
        List<CscpMenus> cscpMenus = cscpMenusRepository.selectByParentId(id);
        if (!cscpMenus.isEmpty()) {
            for (CscpMenus cscpMenu : cscpMenus) {
                delete(cscpMenu.getId());
            }
        }
        cscpMenusRepository.deleteByPrimaryKey(id);

        //根据菜单id删除菜单角色关联
        cscpRoleMenuRepository.deleteMenusByMenuId(id);
    }

    private String getPageOrderBy(Pageable page) {

        if (page != null && page.getSort() != null) {

            StringBuilder sb = new StringBuilder();

            page.getSort().forEach(sort -> sb.append(sort.getProperty())
                    .append(" ").append(sort.getDirection()).append(","));

            if (sb.length() > 1) {
                return (sb.substring(0, sb.length() - 1));
            }
        }

        return null;
    }

    /**
     * Get the cscpMenuss.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpMenusDTO> findByCscpMenusDTO(CscpMenusDTO cscpMenusDTO, Pageable page) {

        log.debug("Request to find CscpMenuss");

        CscpMenusExample example = new CscpMenusExample();

        example.setPage(page);

        Criteria critieria = example.createCriteria();

        if (cscpMenusDTO.getId() != null) {
            critieria.andIdEqualTo(cscpMenusDTO.getId());
        }
        if (cscpMenusDTO.getParentId() != null) {
            critieria.andParentIdEqualTo(cscpMenusDTO.getParentId());
        }
        if (cscpMenusDTO.getOrderby() != null) {
            critieria.andOrderbyEqualTo(cscpMenusDTO.getOrderby());
        }

        if (StringUtils.isNotBlank(cscpMenusDTO.getName())) {
            critieria.andNameLike(String.format("%%%s%%", cscpMenusDTO.getName()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getIcon())) {
            critieria.andIconLike(String.format("%%%s%%", cscpMenusDTO.getIcon()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getTitle())) {
            critieria.andTitleLike(String.format("%%%s%%", cscpMenusDTO.getTitle()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getUrl())) {
            critieria.andUrlLike(String.format("%%%s%%", cscpMenusDTO.getUrl()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getComponent())) {
            critieria.andComponentLike(String.format("%%%s%%", cscpMenusDTO.getComponent()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getType())) {
            critieria.andTypeLike(String.format("%%%s%%", cscpMenusDTO.getType()));
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpMenusDTO> data = cscpMenusMapper.toDto(cscpMenusRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpMenusRepository.countByExample(example);
        }

        return new PageResult<CscpMenusDTO>(data, count, count);

    }

    /**
     * Get the cscpMenuss.
     *
     * @param cscpMenusCriteria
     * @param page
     * @return
     */
    @Override
    public PageResult<CscpMenusDTO> findByCscpMenusCriteria(CscpMenusCriteria cscpMenusCriteria, Pageable page) {

        CscpMenusExample example = new CscpMenusExample();

        example.setPage(page);

        Criteria criteria = example.createCriteria();

        if (cscpMenusCriteria != null) {
            cscpMenusCriteria.buildCriteria(criteria);
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpMenusDTO> data = cscpMenusMapper.toDto(cscpMenusRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpMenusRepository.countByExample(example);
        }

        return new PageResult<CscpMenusDTO>(data, count, count);

    }

    @Override
    public List<CscpMenusDTO> findByUserId(Long id) {
        List<CscpMenusDTO> cscpMenusDtos = cscpMenusMapper.toDto(cscpMenusRepository.selectByUserId(id));
//        CscpMenusDTO cscpMenusDTO = new CscpMenusDTO();
//        cscpMenusDTO.setName("self-edit");
//        cscpMenusDTO.setIcon("md-key");
//        cscpMenusDTO.setTitle("个人中心");
//        cscpMenusDTO.setType("non-menu");
//        cscpMenusDTO.setUrl("/self-edit");
//        cscpMenusDTO.setHttpMethod("*");
//        cscpMenusDTO.setComponent("views/admin/system-page/own-space/self-edit.vue");
//        cscpMenusDTO.setParentId(1L);
//        cscpMenusDtos.add(cscpMenusDTO);
        return cscpMenusDtos;
    }

    @Override
    public MenuItemBean getMenuItemBean(Long roleId) {

        List<CscpMenusDTO> currentRoleAllMenus;
        //查询当前用户菜单列表
        Long uid = SecurityHxUtils.getCurrentUserId();
        log.debug("REST request to get CscpMenus by userId: {}", uid);
        List<CscpMenusDTO> currentUserAllMenus = cscpMenusMapper.toDto(cscpMenusRepository.selectByUserId(uid));
        Map<Long, CscpMenusDTO> result1;
        if(null != roleId){
            // 根据角色id查询菜单信息
            currentRoleAllMenus =cscpMenusRepository.menusByRoleIdAndTenantId(roleId);
             result1 = currentRoleAllMenus.stream().collect(
                    Collectors.toMap(x -> x.getId(), x -> x));
        }else{
            result1 = currentUserAllMenus.stream().collect(
                    Collectors.toMap(x -> x.getId(), x -> x));
        }
        List<MenuItemBean> list = getMenuItemBeans(result1, currentUserAllMenus);
        MenuItemBean example = new MenuItemBean();
        example.setItems(list);
        return example;
    }

    @Override
    public List<CscpMenusDTO> findByRoleId(Long roleId) {

        return cscpMenusRepository.menusByRoleIdAndTenantId(roleId);
    }

    @Override
    public MenuItemBean roleMenuList(Long roleId) {

        try {
            // 获取当前租户下管理员的菜单页
            Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
            Map<Long, CscpMenusDTO> hasRight = cscpMenusRepository.menusByRole(roleId,currentTenantId);

            // 查询当前租户下管理员的信息，默认一个租户下仅一个管理员
            CscpUserDetailDTO detailDTO = cscpUserDetailRepository.selectAdminByTenantId(currentTenantId,AdminFlagConstant.COMMON_ADMIN_USER);
            Long userId = detailDTO.getUserId();
            CscpUserRole userRole = cscpUserRoleRepository.selectRoleIdByUserId(userId);
            Long adminRoleId = userRole.getRoleId();
            List<CscpMenusDTO> allMenus = cscpMenusRepository.selectAllMenusByAdminId(adminRoleId).stream()
                    .map(cscpMenusMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
/*            List<CscpMenusDTO> allMenus = cscpMenusRepository.selectByExample(null).stream()
                    .map(cscpMenusMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));*/
//            HashMap<String, MenuItemBean> itemMap = new HashMap<String, MenuItemBean>();
            List<MenuItemBean> list = getMenuItemBeans(hasRight, allMenus);

            MenuItemBean example = new MenuItemBean();
            example.setItems(list);
            return example;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private List<MenuItemBean> getMenuItemBeans(Map<Long, CscpMenusDTO> hasRight, List<CscpMenusDTO> allMenus) {
        List<MenuItemBean> list = new ArrayList<MenuItemBean>();
//            List<CscpMenusDTO> childMenus = new ArrayList<>();
        for (CscpMenusDTO menus:allMenus) {
            MenuItemBean mib = new MenuItemBean();
            CscpMenusDTO item = menus;
            if ("parent".equals(item.getType()) || item.getUrl() == null) {//URL为空
                mib.setHref("#");
            } else {
                mib.setSref(item.getUrl());
            }
            if(item.getParentId() == 0){
                mib.setTitle(item.getTitle());//TITILE
                mib.setIcon(item.getIcon());//ICON
                mib.setId(item.getId().toString());//ID
                if (hasRight.get(item.getId()) == null) {
                    mib.setChecked(false);
                } else {
                    mib.setChecked(true);
                }
                mib.setHasPermission(mib.getPermissions() != null);
                list.add(mib);
            }

        }

        // 遍历一级菜单
        for (MenuItemBean menuItemBean: list) {
            // 将子元素 set进一级菜单里面
            menuItemBean.setItems(getChild(menuItemBean.getId(),allMenus,hasRight));
        }
        return list;
    }

    /**
     * 获取子节点
     * @param pid
     * @param allMenus
     * @return
     */
    private   List<MenuItemBean>  getChild(String pid,List<CscpMenusDTO> allMenus,  Map<Long, CscpMenusDTO> hasRight){
        List<MenuItemBean> childMenus = new ArrayList<MenuItemBean>();
        for (CscpMenusDTO childMenu: allMenus) {
            MenuItemBean mib = new MenuItemBean();
            if(!"0".equals(pid)){
                if(childMenu.getParentId().toString().equals(pid)){
                    mib.setTitle(childMenu.getTitle());//TITILE
                    mib.setIcon(childMenu.getIcon());//ICON
                    mib.setId(childMenu.getId().toString());//ID
                    if (hasRight.get(childMenu.getId()) == null) {
                        mib.setChecked(false);
                    } else {
                        mib.setChecked(true);
                    }
                    mib.setHasPermission(mib.getPermissions() != null);
                    childMenus.add(mib);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (MenuItemBean menuItemBean: childMenus) {
            // 继续添加子元素
            menuItemBean.setItems(getChild(menuItemBean.getId(),allMenus,hasRight));
        }

        //停下来的条件，如果 没有子元素了，则停下来
        if( childMenus.size()==0 ){
            return null;
        }
        return childMenus;
    }

//    @Override
    public MenuItemBean roleMenuList1(Long roleId) {

        try {
            // 获取当前租户下管理员的菜单页
            Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
            Map<Long, CscpMenusDTO> hasRight = cscpMenusRepository.menusByRole(roleId,currentTenantId);

            // 查询当前租户下管理员的信息，默认一个租户下仅一个管理员
            CscpUserDetailDTO detailDTO = cscpUserDetailRepository.selectAdminByTenantId(currentTenantId,AdminFlagConstant.COMMON_ADMIN_USER);
            Long userId = detailDTO.getUserId();
            CscpUserRole userRole = cscpUserRoleRepository.selectRoleIdByUserId(userId);
            Long adminRoleId = userRole.getRoleId();
            List<CscpMenusDTO> allMenus = cscpMenusRepository.selectAllMenusByAdminId(adminRoleId).stream()
                    .map(cscpMenusMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));
/*            List<CscpMenusDTO> allMenus = cscpMenusRepository.selectByExample(null).stream()
                    .map(cscpMenusMapper::toDto)
                    .collect(Collectors.toCollection(LinkedList::new));*/
            HashMap<String, MenuItemBean> itemMap = new HashMap<String, MenuItemBean>();
            List<MenuItemBean> list = new ArrayList<MenuItemBean>();
            for (int i = 0; i < allMenus.size(); i++) {
                MenuItemBean mib = new MenuItemBean();
                CscpMenusDTO item = allMenus.get(i);
                if ("parent".equals(item.getType()) || item.getUrl() == null) {//URL为空
                    mib.setHref("#");
                } else {
                    mib.setSref(item.getUrl());
                }
                mib.setTitle(item.getTitle());//TITILE
                mib.setIcon(item.getIcon());//ICON
                mib.setId(item.getId().toString());//ID
                if (hasRight.get(item.getId()) == null) {
                    mib.setChecked(false);
                } else {
                    mib.setChecked(true);
                }

                //mib.setPermissions(mapPermission.get(mib.getId()));
                mib.setHasPermission(mib.getPermissions() != null);

                if (itemMap.get(item.getParentId().toString()) != null) {
                    MenuItemBean pmib = (MenuItemBean) itemMap.get(item.getParentId().toString());
                    List<MenuItemBean> subitem = (pmib).getItems();
                    if (subitem == null) {
                        subitem = new ArrayList<MenuItemBean>();
                        pmib.setItems(subitem);
                        pmib.setChecked(false);
                    }
                    subitem.add(mib);

                } else {
                    list.add(mib);
                }
                itemMap.put(item.getId().toString(), mib);
            }

            MenuItemBean example = new MenuItemBean();
            example.setItems(list);
            return example;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void saveRoleMenus(Long roleId, String menus, String permissions) {

        String[] ms = menus.split(",");

        // 菜单为空时
        if ("".equals(ms[0])){
            return;
        }

        try {
            CscpRoleMenuExample example = new CscpRoleMenuExample();
            LongCriteria roleIdCriteria = new LongCriteria();
            roleIdCriteria.setEquals(roleId);
            example.setRoleId(roleIdCriteria);
            example.buildCriteria();
            cscpRoleMenuRepository.deleteByExample(example);

            for (int i = 0; i < ms.length; i++) {
                CscpRoleMenu record = new CscpRoleMenu();
                record.setRoleId(roleId);
                record.setMenuId(Long.valueOf(ms[i]));
                cscpRoleMenuRepository.insert(record);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void addRoleMenu(Long roleId, Long menuId) {
        CscpRoleMenu menu = new CscpRoleMenu();
        menu.setRoleId(roleId);
        menu.setMenuId(menuId);
        cscpRoleMenuRepository.insert(menu);
    }


    //按照父id查找menus
    @Override
    public List<CscpMenus> findByParentId(Long parentId) {
        return cscpMenusRepository.selectByParentId(parentId);
    }

    @Override
    public List<CscpMenusDTO> findAllMenusByCondition(CscpMenusDTO cscpMenusDTO) {
        log.debug("Request to find CscpMenuss");

        CscpMenusExample example = new CscpMenusExample();

        Criteria critieria = example.createCriteria();

        if (cscpMenusDTO.getId() != null) {
            critieria.andIdEqualTo(cscpMenusDTO.getId());
        }
        if (cscpMenusDTO.getParentId() != null) {
            critieria.andParentIdEqualTo(cscpMenusDTO.getParentId());
        }
        if (cscpMenusDTO.getOrderby() != null) {
            critieria.andOrderbyEqualTo(cscpMenusDTO.getOrderby());
        }

        if (StringUtils.isNotBlank(cscpMenusDTO.getName())) {
            critieria.andNameLike(String.format("%%%s%%", cscpMenusDTO.getName()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getIcon())) {
            critieria.andIconLike(String.format("%%%s%%", cscpMenusDTO.getIcon()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getTitle())) {
            critieria.andTitleLike(String.format("%%%s%%", cscpMenusDTO.getTitle()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getUrl())) {
            critieria.andUrlLike(String.format("%%%s%%", cscpMenusDTO.getUrl()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getComponent())) {
            critieria.andComponentLike(String.format("%%%s%%", cscpMenusDTO.getComponent()));
        }
        if (StringUtils.isNotBlank(cscpMenusDTO.getType())) {
            critieria.andTypeLike(String.format("%%%s%%", cscpMenusDTO.getType()));
        }
        example.setOrderByClause("orderby");

        List<CscpMenusDTO> data = cscpMenusMapper.toDto(cscpMenusRepository.selectByExamplewithPage(example));

        return data;
    }

    /**
     * 根据角色id，删除旧菜单
     * @param roleId
     */
    @Override
    public void deleteRoleMenus(Long roleId) {
        CscpRoleMenuExample cscpRoleMenuExample = new CscpRoleMenuExample();
        CscpRoleMenuExample.Criteria criteria = cscpRoleMenuExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        cscpRoleMenuRepository.deleteByExample(cscpRoleMenuExample);

    }

    @Override
    public List<TreeMenuSelect> buildMenuTreeSelect(Long uid) {
        List<CscpMenus> cscpMenuss = cscpMenusRepository.selectByUserId(uid);
        List<CscpMenus> menuTree = buildMenuTree(cscpMenuss);
        return menuTree.stream().map(TreeMenuSelect::new).collect(Collectors.toList());
    }

    @Override
    public CscpMenus getMenuByUrl(String url) {
        return cscpMenusRepository.getMenuByUrl(url);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menuss 菜单列表
     * @return 树结构列表
     */
    public List<CscpMenus> buildMenuTree(List<CscpMenus> menuss)
    {
        List<CscpMenus> returnList = new ArrayList<CscpMenus>();
        List<Long> tempList = new ArrayList<Long>();
        for (CscpMenus cscpMenus : menuss)
        {
            tempList.add(cscpMenus.getId());
        }
        for (Iterator<CscpMenus> iterator = menuss.iterator(); iterator.hasNext();)
        {
            CscpMenus menus = (CscpMenus) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menus.getParentId()))
            {
                recursionFn(menuss, menus);
                returnList.add(menus);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menuss;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<CscpMenus> list, CscpMenus t)
    {
        // 得到子节点列表
        List<CscpMenus> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CscpMenus tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CscpMenus> list, CscpMenus t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<CscpMenus> getChildList(List<CscpMenus> list, CscpMenus t)
    {
        List<CscpMenus> tlist = new ArrayList<CscpMenus>();
        Iterator<CscpMenus> it = list.iterator();
        while (it.hasNext())
        {
            CscpMenus n = (CscpMenus) it.next();
            if ((n.getParentId()!=null) && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    @Override
    public List<CscpMenus> getMenuByUrlBatch(List<String> urlList) {
        return cscpMenusRepository.getMenuByUrlBatch(urlList);
    }

    @Override
    @Transactional
    public void deleteByIdBatch(List<Long> idList) {
        log.info("Request to delete CscpMenusBatch : {} ", idList);
        if(CollectionUtils.isEmpty(idList)){
            return;
        }
        List<Long> allIdList = new ArrayList<>(idList);
        //获取子级菜单id
        while(CollectionUtils.isNotEmpty(idList)){
            idList = cscpMenusRepository.getMenuChildIdParentBatch(idList);
            allIdList.addAll(idList);
        }
        //根据菜单id 批量删除菜单
        cscpMenusRepository.deleteByPrimaryKeyBatch(allIdList);
        //根据菜单id批量删除菜单角色关联
        cscpRoleMenuRepository.deleteMenusByMenuIdBatch(allIdList);
    }



    @Override
    public void deleteMenusByMenuId(Long menuId) {
        cscpRoleMenuRepository.deleteMenusByMenuId(menuId);
    }

    @Override
    @Transactional
    public void addRoleMenuList(List<CscpRoleMenu> menu) {
        cscpRoleMenuRepository.insertList(menu);
    }

    @Override
    public List<CscpRoleMenu> selectByMenuId(Long menuId) {
        List<CscpRoleMenu> cscpRoleMenus = cscpRoleMenuRepository.selectByMenuId(menuId);
        return cscpRoleMenus;
    }

    @Override
    public List<CscpMenus> selectIdByParentId(Long parentId) {
        List<CscpMenus> cscpMenus = cscpMenusRepository.selectIdByParentId(parentId);
        return cscpMenus;
    }

    @Override
    public List<Long> selectListByMenuId(Long menuId) {
        List<Long> cscpRoleMenus = cscpRoleMenuRepository.selectListByMenuId(menuId);
        return cscpRoleMenus;
    }

    @Override
    public void dragAddRole(Long parentId, List<Long> allRoleId) {
        CscpMenus cscpMenus = cscpMenusRepository.selectOneById(parentId);
        if(cscpMenus != null){
            Long parentId1 = cscpMenus.getParentId();
            List<Long> oldRoleId2 = cscpRoleMenuRepository.selectListByMenuId(parentId1);
            for (Long one : allRoleId) {
                if (!oldRoleId2.contains(one)) {
                    CscpRoleMenu roleMenu = new CscpRoleMenu();
                    roleMenu.setMenuId(parentId1);
                    roleMenu.setRoleId(one);
                    cscpRoleMenuRepository.insert(roleMenu);
                }
            }
            dragAddRole(parentId1,allRoleId);
        }
    }

    @Override
    public List<CscpMenus> selectById(Long id) {
        List<CscpMenus> cscpMenus = cscpMenusRepository.selectById(id);
        return cscpMenus;
    }

    @Override
    public void deleteMenusByMenuIdNo(Long menuId) {
        cscpRoleMenuRepository.deleteMenusByMenuIdNo(menuId);
    }

    @Override
    public CscpRoleMenu selectByMenuIdAndRoleId(Long menuId, Long roleId) {
        CscpRoleMenu cscpRoleMenu = cscpRoleMenuRepository.selectByMenuIdAndRoleId(menuId, roleId);
        return cscpRoleMenu;
    }

    @Override
    public int insert(CscpRoleMenu record) {
        int insert = cscpRoleMenuRepository.insert(record);
        return insert;
    }

    @Override
    public CscpMenus selectOneById(Long parentId) {
        CscpMenus cscpMenus = cscpMenusRepository.selectOneById(parentId);
        return cscpMenus;
    }

    @Override
    public void addRoleMenuParentFor(Long id, List<Long> roleId) {
        CscpMenus cscpMenus = cscpMenusRepository.selectOneById(id);
        if (cscpMenus != null) {
            Long parentId = cscpMenus.getParentId();
            if(parentId != 0){
                for (Long role:roleId) {
                    CscpRoleMenu cscpRoleMenu = cscpRoleMenuRepository.selectByMenuIdAndRoleId(parentId, role);
                    if(cscpRoleMenu == null){
                        CscpRoleMenu roleMenu = new CscpRoleMenu();
                        roleMenu.setMenuId(parentId);
                        roleMenu.setRoleId(role);
                        cscpRoleMenuRepository.insert(roleMenu);
                    }
                }
            }
            addRoleMenuParentFor(cscpMenus.getParentId(),roleId);
        }
    }

    @Override
    public void addRoleMenuFor(CscpMenusDTO cscpMenusDto, CscpMenus parentMenu) {
        addRoleMenuByParentId(cscpMenusDto,parentMenu);
        //查出上级菜单
        CscpMenus cscpMenus = cscpMenusRepository.selectOneById(parentMenu.getParentId());
        if (cscpMenus != null) {
            addRoleMenuFor(cscpMenusDto,cscpMenus);
        }
    }

    @Override
    public void addRoleMenuByParentId(CscpMenusDTO cscpMenusDto, CscpMenus cscpMenus) {
        List<Long> roleId = cscpMenusDto.getRoleId();
        if(roleId != null && roleId.size() > 0){
            List<CscpRoleMenu> roleMenus = new ArrayList<>();
            for (Long role:roleId) {
                CscpRoleMenu roleMenuOld = cscpRoleMenuRepository.selectByMenuIdAndRoleId(cscpMenus.getId(), role);
                if(roleMenuOld == null){
                    CscpRoleMenu cscpRoleMenu = new CscpRoleMenu();
                    cscpRoleMenu.setRoleId(role);
                    cscpRoleMenu.setMenuId(cscpMenus.getId());
                    roleMenus.add(cscpRoleMenu);
                }
            }
            if(roleMenus != null && roleMenus.size() > 0){
                cscpRoleMenuRepository.insertList(roleMenus);
            }
        }
    }

    @Override
    public void addRoleMenu(CscpMenusDTO cscpMenusDto, CscpMenusDTO old) {
        List<Long> roleId = cscpMenusDto.getRoleId();
        if(roleId != null && roleId.size() > 0){
            List<CscpRoleMenu> roleMenus = new ArrayList<>();
            for (Long role:roleId) {
                CscpRoleMenu cscpRoleMenu = new CscpRoleMenu();
                cscpRoleMenu.setRoleId(role);
                cscpRoleMenu.setMenuId(old.getId());
                roleMenus.add(cscpRoleMenu);
            }
            if(roleMenus != null && roleMenus.size() > 0){
                cscpRoleMenuRepository.insertList(roleMenus);
            }
        }
    }

    /**
     * 低代码同步父及菜单
     * @author wbb
     * @param cspMenusDTO 父及菜单及设置默认admin权限
     */
    @Override
    @Transactional
    public void synChildMenusBatchForLowCode(CscpMenusDTO cspMenusDTO) {
        if(cspMenusDTO == null || CollectionUtils.isEmpty(cspMenusDTO.getChildMenusList())){
            return;
        }
        //加工菜单子菜单实体
        List<CscpMenus> addList = new ArrayList<>();
        List<CscpMenusDTO> childList = cspMenusDTO.getChildMenusList();
        for(CscpMenusDTO dto : childList){
            dto.setParentId(cspMenusDTO.getId());
            CscpMenus cscpMenus = cscpMenusMapper.toEntity(dto);
            addList.add(cscpMenus);
        }
        //批量添加
        cscpMenusRepository.insertBatch(addList);
        //默认权限赋值给管理员
        List<CscpRoleMenu> rmList = new ArrayList<>();
        //多租时在租户账号下给admin赋权
        Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
        CscpRoles cscpRoles = new CscpRoles();
        cscpRoles.setName("admin");
        cscpRoles.setTenantId(currentTenantId);
        CscpRoles roleByTenant = cscpRolesService.getRoleByTenant(cscpRoles);
        for(CscpMenus cms : addList){
            CscpRoleMenu rm = new CscpRoleMenu();
            rm.setRoleId(1L);
            rm.setMenuId(cms.getId());
            rmList.add(rm);
            //多租户时给租户admin账号设置菜单权限
            if(roleByTenant.getId() != 1L){
                CscpRoleMenu rmT = new CscpRoleMenu();
                rmT.setRoleId(roleByTenant.getId());
                rmT.setMenuId(cms.getId());
                rmList.add(rmT);
            }
        }
        cscpRoleMenuRepository.insertList(rmList);
    }

    /**
     * 低代码更新子级菜单
     * @author wbb
     * @param cspMenusDTO 父及菜单及设置默认admin权限
     */
    @Override
    @Transactional
    public void updateChildMenusBatchForLowCode(CscpMenusDTO cspMenusDTO) {
        if(cspMenusDTO == null || CollectionUtils.isEmpty(cspMenusDTO.getChildMenusList())){
            return;
        }
        //获取permisonCodeList
        List<String> permisionCodeList = cspMenusDTO.getChildMenusList().stream().map(CscpMenusDTO::getPermissionCode).collect(Collectors.toList());
        //根据permisionCode 删除菜单权限及菜单
        cscpMenusRepository.deleteMenuAndMenuRole(permisionCodeList);
        //新增菜单
        this.synChildMenusBatchForLowCode(cspMenusDTO);
    }

    @Override
    public List<CscpMenus> qryMenuByModuleId(String moduleId) {
        return cscpMenusRepository.qryMenuByModuleId(moduleId);
    }
}
