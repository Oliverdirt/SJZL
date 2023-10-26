package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.domain.CscpRoleMenu;
import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Validate;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.*;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * REST controller for managing CscpMenus.
 *
 * @author ctsi biyi generator
 *
 */
@RestController
@RequestMapping("/api/system")
@Component
public class CscpMenusController {

    private final Logger log = LoggerFactory.getLogger(CscpMenusController.class);

    private static final String ENTITY_NAME = "cscpMenus";

    private final CscpMenusService cscpMenusService;
    @Autowired
    private CscpRolesService cscpRolesService;
    public CscpMenusController(CscpMenusService cscpMenusService) {
        this.cscpMenusService = cscpMenusService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpMenuss : Create a new cscpMenus.
     * @param cscpMenusDTO the cscpMenusDTO to create
     * @return the ResponseEntity with status 201
     * (Created) and with body the new cscpMenusDTO, or
     * with status 400 (Bad Request) if the cscpMenus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpMenuss")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpMenus")
    public ResponseEntity<CscpMenusDTO> createCscpMenus(
            @RequestBody CscpMenusDTO cscpMenusDTO) throws URISyntaxException {
        log.debug("REST request to save CscpMenus : {}", cscpMenusDTO);
        if (cscpMenusDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpMenus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CscpMenusDTO result = cscpMenusService.insert(cscpMenusDTO);
        return ResponseEntity.created(new URI("/api/cscpMenuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpMenuss : Updates an existing cscpMenus.
     *
     * @param cscpMenusDTO the cscpMenusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpMenusDTO,
     * or with status 400 (Bad Request) if the cscpMenusDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpMenusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpMenuss")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpMenus")
    public ResponseEntity<CscpMenusDTO> updateCscpMenus(
            @RequestBody CscpMenusDTO cscpMenusDTO) throws URISyntaxException {
        log.debug("REST request to update CscpMenus : {}", cscpMenusDTO);
        if (cscpMenusDTO.getId() == null) {
            return createCscpMenus(cscpMenusDTO);
        }
        CscpMenusDTO result = cscpMenusService.update(cscpMenusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpMenusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpMenuss : get the cscpMenuss.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpMenuss in body
     */
    @GetMapping("/cscpAllMenus")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpMenuss")
    public List<CscpMenusDTO> getCscpMenuss(CscpMenusDTO cscpMenusDTO) {
        log.debug("REST request to get CscpAllMenus");

        return cscpMenusService.findAllMenusByCondition(cscpMenusDTO);
    }


    @GetMapping("/tenant/cscpAllMenus")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getTenantCscpMenuss")
    public List<CscpMenusDTO> getTenantCscpMenuss(CscpMenusDTO cscpMenusDTO) {
        log.debug("REST request to get CscpAllMenus");
        List<CscpMenusDTO> allMenusByCondition = cscpMenusService.findAllMenusByCondition(cscpMenusDTO);
        for (int i = 0; i < allMenusByCondition.size(); i++) {
            if("1479655112178688002".equals(allMenusByCondition.get(i).getId().toString())
                    ||"1479632694877904898".equals(allMenusByCondition.get(i).getId().toString())
                    || "1482275013187092481".equals(allMenusByCondition.get(i).getId().toString())
                    || "1482275090282594305".equals(allMenusByCondition.get(i).getId().toString())
                    || "1482275206167019521".equals(allMenusByCondition.get(i).getId().toString())
            ){
                allMenusByCondition.remove(i);
            }
        }

        return allMenusByCondition;
    }
    /**
     * GET  /cscpMenuss : get the cscpMenuss.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpMenuss in body
     */
    @GetMapping("/cscpMenussByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpMenussByCriteria")
    public PageResult<CscpMenusDTO> getCscpMenussByCriteria(CscpMenusCriteria cscpMenusCriteria, Pageable page) {
        log.debug("REST request to get CscpMenussByCriteria");
        return cscpMenusService.findByCscpMenusCriteria(cscpMenusCriteria, page);
    }


    /**
     * GET  /cscpMenuss/
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpMenusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpMenus")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpMenus")
    public List<CscpMenusDTO> getCscpMenus() {

        //TODO: int-->Long
        Long uid = SecurityHxUtils.getCurrentUserId();
        log.debug("REST request to get CscpMenus by userId: {}", uid);
        List<CscpMenusDTO> list = cscpMenusService.findByUserId(uid);
        return list;
    }

    /**
     * DELETE  /cscpMenuss/:id : delete the "id" cscpMenus.
     *
     * @param id the id of the cscpMenusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpMenuss/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpMenus")
    public ResponseEntity<Void> deleteCscpMenus(@PathVariable  Long id) {
        log.debug("REST request to delete CscpMenus : {}", id);
        cscpMenusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping(value="/menu/queryByRoleId")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "roleMenuList")
	public ResponseEntity<MenuItemBean> roleMenuList(@RequestParam Long roles){
		MenuItemBean ls = cscpMenusService.roleMenuList(roles);
		return new ResponseEntity(ls, HttpStatus.OK);
	}

    @PostMapping(value="/menu/queryByRoleId/{roleId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "roleMenuList")
    public ResponseEntity<MenuItemBean> queryByRoleId(@PathVariable("roleId") Long roleId){
        MenuItemBean ls = cscpMenusService.roleMenuList(roleId);
        return new ResponseEntity(ls, HttpStatus.OK);
    }

    /**
     * 获取当前用户的全部菜单并构建树表
     */
    @PostMapping("/getMenuItemBean")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getMenuItemBean")
    public ResponseEntity<MenuItemBean> getMenuItemBean(@RequestParam Long roleId) {
//        List<CscpMenusDTO> allMenus = new ArrayList<>();
//        //查询当前用户菜单列表
//        if(null == roleId){
//            Long uid = SecurityHxUtils.getCurrentUserId();
//            log.debug("REST request to get CscpMenus by userId: {}", uid);
//            allMenus = cscpMenusService.findByUserId(uid);
//        }else{
//            // 根据角色id查询菜单信息
//            allMenus =cscpMenusService.findByRoleId(roleId);
//        }
        // 查询指定角色的菜单列表
        MenuItemBean ls = cscpMenusService.getMenuItemBean(roleId);
        return new ResponseEntity(ls, HttpStatus.OK);
    }




    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/menu/treeselect")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "treeselect")
    public ResponseEntity<List<TreeMenuSelect>> treeselect()
    {
        Long uid = SecurityHxUtils.getCurrentUserId();
        List<TreeMenuSelect> treeSelects = cscpMenusService.buildMenuTreeSelect(uid);
        return ResponseEntity.ok().body(treeSelects);
    }
    @PostMapping(value="/menu/save")
	@Validate
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "saveRoleMenus")
	public ResponseEntity<AjaxForm> saveRoleMenus(@RequestParam Long roles,
                                                  @RequestParam String menus, @RequestParam String permissions){
    	cscpMenusService.saveRoleMenus(roles, menus, permissions);
		return new ResponseEntity(new AjaxForm(true, ""), HttpStatus.OK);
	}

    /**
     * GET  /cscpMenuss/:id : get the "id" cscpMenus.
     *
     * @param id the id of the cscpMenusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpMenusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpMenuss/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpMenus")
    public ResponseEntity<CscpMenusDTO> getCscpMenus(@PathVariable Long id) {
        log.debug("REST request to get CscpMenus : {}", id);
        CscpMenusDTO cscpMenusDTO = cscpMenusService.findOne(id);
        List<CscpMenus> byParentId = cscpMenusService.findByParentId(id);
        if(byParentId != null && byParentId.size() > 0){
            //不展示角色
            cscpMenusDTO.setFlag(0);
        }else{
            //展示角色
            cscpMenusDTO.setFlag(1);
        }
        List<CscpRoleMenu> cscpRoleMenus = cscpMenusService.selectByMenuId(id);
        List<String> list2 = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        if(cscpRoleMenus != null && cscpRoleMenus.size() > 0){
            for (CscpRoleMenu one: cscpRoleMenus) {
                if(one.getRoleId() != 1L){
                    list.add(one.getRoleId());
                }
            }
            if(list != null && list.size() > 0){
                List<CscpRoles> nameById = cscpRolesService.getNameById(list);
                for (CscpRoles name: nameById) {
                    list2.add(name.getName());
                }
                cscpMenusDTO.setRoleName(list2);
                cscpMenusDTO.setRoleId(list);
            }else{
                cscpMenusDTO.setRoleName(list2);
                cscpMenusDTO.setRoleId(list);
            }
        }else{
            cscpMenusDTO.setRoleName(list2);
            cscpMenusDTO.setRoleId(list);
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpMenusDTO));
    }
    //自定义菜单增加与更新
    @PutMapping("/menu/addMenu")
    @Transactional
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "addMenu")
    public AjaxBackData addMenu(@RequestBody CscpMenusDTO cscpMenusDto){
        AjaxBackData ajaxBackData = new AjaxBackData(true,"");
        if (cscpMenusDto != null){
            //判断权限是否存在 存在则更新，不存在则新增
            Long id = cscpMenusDto.getId();
            if(cscpMenusService.findOne(id) != null){
                cscpMenusService.update(cscpMenusDto);
                log.debug("REST request to update CscpMenus : {}", id);
                cscpMenusService.deleteMenusByMenuIdNo(id);
                List<Long> roleId = cscpMenusDto.getRoleId();
                if(CollectionUtils.isNotEmpty(roleId)){
                    List<CscpRoleMenu> cscpRoleMenus = new ArrayList<>();
                    for (Long role:roleId) {
                        CscpRoleMenu cscpRoleMenu = new CscpRoleMenu();
                        cscpRoleMenu.setRoleId(role);
                        cscpRoleMenu.setMenuId(id);
                        cscpRoleMenus.add(cscpRoleMenu);
                    }
                    cscpMenusService.addRoleMenuList(cscpRoleMenus);
                    cscpMenusService.addRoleMenuParentFor(id,roleId);
                }
            }else {
                CscpMenusDTO newCscpMenusDto = cscpMenusService.insert(cscpMenusDto);
                log.debug("REST request to insert CscpMenus");

                // 与管理员角色绑定,roleId为1l(默认新增角色admin)
                cscpMenusService.addRoleMenu(1L, newCscpMenusDto.getId());

                Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
                CscpRoles cscpRoles = new CscpRoles();
                cscpRoles.setName("admin");
                cscpRoles.setTenantId(currentTenantId);
                CscpRoles roleByTenant = cscpRolesService.getRoleByTenant(cscpRoles);

                if(roleByTenant.getId() != 1L){
                    //除了superAdmin的租户
                    cscpMenusService.addRoleMenu(roleByTenant.getId(),newCscpMenusDto.getId());
                }

                if (CollectionUtils.isNotEmpty(cscpMenusDto.getRoleId())) {
                    cscpMenusService.addRoleMenu(cscpMenusDto, newCscpMenusDto);
                    //parentId不为空，从已有菜单新增
                    if (cscpMenusDto.getParentId() != null) {
                        if (cscpMenusDto.getParentId() != 0) {
                            //parentId不为0，有父菜单,查出上级菜单
                            CscpMenus cscpMenus = cscpMenusService.selectOneById(cscpMenusDto.getParentId());
                            cscpMenusService.addRoleMenuFor(cscpMenusDto, cscpMenus);
                        }
                    }
                }
                //返回最新插入数据的id
                ajaxBackData.setId(newCscpMenusDto.getId().toString());
            }
        }
        return ajaxBackData;
    }

    //自定义菜单删除
    @DeleteMapping("/menu/deleteMenu/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteMenu")
    public AjaxBackData deleteMenu(@PathVariable Long id){
        AjaxBackData ajaxBackData = new AjaxBackData();
        log.debug("REST request to delete CscpMenus : {}", id);
        if(id != null){
            cscpMenusService.delete(id);
        }
        return ajaxBackData;
    }
    //重写目录树get方法
    @GetMapping("/menu/getMenu")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getMenu")
    public PageResult<CscpMenus> getMenu(@RequestParam Long parentId){
        log.debug("REST request to get all menus");
        //根据获取指定节点的孩子节点，若没有指定则找id=1的孩子节点
        List<CscpMenus> childs;
        if(parentId == null){
            childs = cscpMenusService.findByParentId(1L);
            for(CscpMenus one : childs){
                List<CscpMenus> cscpMenus = cscpMenusService.selectById(one.getId());
                if(cscpMenus != null && cscpMenus.size() > 0){
                    one.setFlag(0);
                }else{
                    one.setFlag(1);
                }
                List<Long> longs = cscpMenusService.selectListByMenuId(one.getId());
                one.setRoleId(longs);
            }
        }else{
            childs = cscpMenusService.findByParentId(parentId);
            for(CscpMenus one : childs){
                List<CscpMenus> cscpMenus = cscpMenusService.selectById(one.getId());
                if(cscpMenus != null && cscpMenus.size() > 0){
                    one.setFlag(0);
                }else{
                    one.setFlag(1);
                }
                List<Long> longs = cscpMenusService.selectListByMenuId(one.getId());
                one.setRoleId(longs);
            }
        }
        PageResult<CscpMenus> result = new PageResult<>();
        result.setData(childs);
        result.setRecordsTotal(childs.size());
        return result;
    }

      //异步路由树拖拽实现
    @PutMapping("/menu/updateMenu")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateOrderBy")
    public AjaxBackData updateOrderBy(@RequestBody CscpMenusDTO[] cscpMenusDtos){
        AjaxBackData ajaxBackData = new AjaxBackData(true,"");
        if (cscpMenusDtos.length <=0){
            return ajaxBackData;
        }else {
            List<Long> allRoleIdList = new ArrayList<>();

            for(CscpMenusDTO cscpMenusDto:cscpMenusDtos){
               if(cscpMenusDto.getOrderby()==0){
                   Integer orderBy =cscpMenusService.getOrderBy(cscpMenusDto);
                    cscpMenusDto.setOrderby(orderBy);
               }
               cscpMenusService.update(cscpMenusDto);

               allRoleIdList.addAll(cscpMenusDto.getRoleId());
           }
            List<Long> allRoleId = allRoleIdList.stream().distinct().collect(Collectors.toList());//(1 2 3 4 5 6 7)
            if(allRoleId != null && allRoleId.size() > 0){
                Long parentId = cscpMenusDtos[0].getParentId();
                if(parentId != 0){
                    List<Long> oldRoleId = cscpMenusService.selectListByMenuId(parentId);//(1 2 3 4 5)
                    for (Long one : allRoleId) {
                        if (!oldRoleId.contains(one)) {
                            CscpRoleMenu roleMenu = new CscpRoleMenu();
                            roleMenu.setMenuId(parentId);
                            roleMenu.setRoleId(one);
                            cscpMenusService.insert(roleMenu);
                        }
                    }
                    cscpMenusService.dragAddRole(parentId,allRoleId);
                }
            }

        }
        return ajaxBackData;
    }
    /**
     * 根据url获取菜单
     */
    //异步路由树拖拽实现
    @GetMapping ("/menu/getMenuByUrl")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateOrderBy")
    public AjaxResult getMenuByUrl(String url){
        CscpMenus cscpMenus = cscpMenusService.getMenuByUrl(url);
        return AjaxResult.success(cscpMenus);
    }

    /**
     * 获取buton 菜单列表
     */
    @GetMapping ("/menu/getButtonMenus")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateOrderBy")
    public List<String> getButtonMenus(){
        List<CscpMenusDTO> butonMenus = cscpMenusService.findByUserId(SecurityHxUtils.getCurrentUserId());
        List<String> persionList = new ArrayList<>();
        if(CollectionUtils.isEmpty(butonMenus)){
            return  persionList;
        }
        for(CscpMenusDTO b : butonMenus){
            if(b != null && StringUtils.equalsIgnoreCase("button",b.getType()) && StringUtils.isNotEmpty(b.getPermissionCode())){
                persionList.add(b.getPermissionCode());
            }
        }
        return persionList;
    }
}
