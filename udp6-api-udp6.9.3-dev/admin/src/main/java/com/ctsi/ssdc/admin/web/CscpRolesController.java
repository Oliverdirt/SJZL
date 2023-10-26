package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.constants.AdminFlagConstant;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * REST controller for managing CscpRoles.
 *
 * @author ctsi biyi generator
 *
 */
@RestController
@RequestMapping("/api/system")
public class CscpRolesController {

    private final Logger log = LoggerFactory.getLogger(CscpRolesController.class);

    private static final String ENTITY_NAME = "cscpRoles";

    private final CscpRolesService cscpRolesService;

    @Autowired
    private  CscpUserDetailService cscpUserDetailService;



    public CscpRolesController(CscpRolesService cscpRolesService) {
        this.cscpRolesService = cscpRolesService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpRoless : Create a new cscpRoles.
     *
     * @param cscpRolesDTO the cscpRolesDTO to create
     * @return the ResponseEntity with status 201 (Created) and
     * with body the new cscpRolesDTO, or with status 400 (Bad Request) if the cscpRoles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpRoless")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpRoles")
    public ResponseEntity<CscpRolesDTO> createCscpRoles(
            @RequestBody CscpRolesDTO cscpRolesDTO) throws URISyntaxException {

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        cscpRolesDTO.setTenantId(currentUser.getTenantId());


        log.debug("REST request to save CscpRoles : {}", cscpRolesDTO);
        if (cscpRolesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpRoles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CscpRolesDTO result = cscpRolesService.insert(cscpRolesDTO);
        return ResponseEntity.created(new URI("/api/cscpRoless/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpRoless : Updates an existing cscpRoles.
     *
     * @param cscpRolesDTO the cscpRolesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpRolesDTO,
     * or with status 400 (Bad Request) if the cscpRolesDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpRolesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpRoless")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpRoles")
    public ResponseEntity<CscpRolesDTO> updateCscpRoles(
            @RequestBody CscpRolesDTO cscpRolesDTO) throws URISyntaxException {
        log.debug("REST request to update CscpRoles : {}", cscpRolesDTO);

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        // 判断租户id
        if(!cscpRolesDTO.getTenantId().equals(currentUser.getTenantId())){
            log.error("update error,not update tenantId");
            throw new BadRequestAlertException("cscpRoles cannot update the tenantID", cscpRolesDTO.getTenantId().toString(), "tenantIdError");
        }

        if (cscpRolesDTO.getId() == null) {
            return createCscpRoles(cscpRolesDTO);
        }
        CscpRolesDTO result = cscpRolesService.update(cscpRolesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpRolesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpRoless : get the cscpRoless.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpRoless in body
     */
    @GetMapping("/cscpRoless")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRoless")
    public PageResult<CscpRolesDTO> getCscpRoless(CscpRolesDTO cscpRolesDTO, Pageable page) {
        log.debug("REST request to get CscpRoless");

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        cscpRolesDTO.setTenantId(currentUser.getTenantId());

        return cscpRolesService.findByCscpRolesDTO(cscpRolesDTO, page);
    }
    
    /**
     * GET  /cscpRoless : get the cscpRoless.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpRoless in body
     */
    @GetMapping("/cscpRolessByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRolessByCriteria")
    public PageResult<CscpRolesDTO> getCscpRolessByCriteria(CscpRolesCriteria cscpRolesCriteria, Pageable page) {
        log.debug("REST request to get CscpRolessByCriteria");

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        LongCriteria tenantIdCriteria = new LongCriteria();
        tenantIdCriteria.setEquals(currentUser.getTenantId());
        cscpRolesCriteria.setTenantId(tenantIdCriteria);

        return cscpRolesService.findByCscpRolesCriteria(cscpRolesCriteria, page);
    }

    /**
     * GET  /cscpRoless/:id : get the "id" cscpRoles.
     *
     * @param id the id of the cscpRolesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpRolesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpRoless/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRoles")
    public ResponseEntity<CscpRolesDTO> getCscpRoles(@PathVariable Long id) {
        log.debug("REST request to get CscpRoles : {}", id);
        CscpRolesDTO cscpRolesDTO = cscpRolesService.findOne(id);

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);

        if(!currentUser.getTenantId().equals(cscpRolesDTO.getTenantId())){
             log.error("current role is null");
             throw new BadRequestAlertException("current role is null", id.toString(), "roleIdError");
         }


        return ResponseEntity.ok()
                .body(cscpRolesDTO);
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpRolesDTO));
    }

    /**
     * DELETE  /cscpRoless/:id : delete the "id" cscpRoles.
     *
     * @param id the id of the cscpRolesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpRoless/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpRoles")
    public ResponseEntity<Void> deleteCscpRoles(@PathVariable  Long id) {
        log.debug("REST request to delete CscpRoles : {}", id);

        // 查询角色
        CscpRolesDTO cscpRolesDTO = cscpRolesService.findOne(id);

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);


        // 判断是否为租户管理员，只有该角色所属租户的租户管理员才能进行删除操作
        if(!currentUser.getAdminFlag().equals(AdminFlagConstant.COMMON_ADMIN_USER) && !currentUser.getTenantId().equals(cscpRolesDTO.getTenantId())){
            throw new BadRequestAlertException("删除失败：仅管理员可删除！",ENTITY_NAME,"error");
        }
        // 判断是否为同一租户
        if(!currentUser.getTenantId().equals(cscpRolesDTO.getTenantId())){
            throw new BadRequestAlertException("删除失败： 禁止删除非本租户下的角色信息",ENTITY_NAME,"error");
        }

        cscpRolesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 批量添加用户
     * @param roleId
     * @param userIds
     * @return
     */
    @PostMapping("/role/saveUsers/{roleId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "saveUsers")
    public AjaxBackData saveUsers(@PathVariable Long roleId, @RequestBody Long[] userIds){
        log.debug("批量添加用户",roleId,userIds);

        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);

        // 判断 tenant
        CscpRolesDTO rolesDTO = cscpRolesService.findOne(roleId);

        if(!rolesDTO.getTenantId().equals(currentUser.getTenantId())){
            log.error("current role is null");
            throw new BadRequestAlertException("current role is null", roleId.toString(), "roleIdError");
        }

        AjaxBackData ajaxBackData = null;
        String returnFlag = cscpRolesService.saveUsers(roleId, userIds);
        if("true".equals(returnFlag)){
            ajaxBackData = new AjaxBackData(true,"批量添加用户成功");
        }else{
            ajaxBackData = new AjaxBackData(false,"批量添加用户失败");
        }
        ajaxBackData.setId(String.valueOf(roleId));
        return ajaxBackData;
    }

    /**
     * 根据角色id查询所关联的用户
     * @param roleId
     * @return
     */
    @GetMapping("/role/findUsers/{roleId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getUsersByRoleId")
    public ResponseEntity<List<CscpUser>>  getUsersByRoleId(@PathVariable Long roleId){
        log.debug("根据角色id查询所关联的用户",roleId);

        // 判断 tenant
        CscpRolesDTO rolesDTO = cscpRolesService.findOne(roleId);
        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);

        if(!rolesDTO.getTenantId().equals(currentUser.getTenantId())){
            log.error("current role is null");
            throw new BadRequestAlertException("current role is null", roleId.toString(), "roleIdError");
        }

        List<CscpUser> usersByRoleId = cscpRolesService.getUsersByRoleId(roleId);


        return ResponseEntity.ok()
                .body(usersByRoleId);
    }

//    @GetMapping("/cscpUserDetailsOrTwo")
//
//    public PageResult<CscpUserDetailDTO> cscpUserDetailsOrTwo(CscpUserDetailLike cscpUserDetailLike, Pageable page) {
//        log.debug("REST request to get CscpUserDetails");
//
//        // 查询租户id
//        Long currentUserId = SecurityHxUtils.getCurrentUserId();
//        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
//
//
//        PageResult<CscpUserDetailDTO> cscpUserDetailDTOPageResult = cscpUserDetailService.selectByTenantId();
//        return cscpUserDetailService.findByCscpUserDetailDTOOr(cscpUserDetailLike, page);
//    }

    /**
     * 修改保存数据权限
     */

    @PutMapping("/dataScope")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "dataScope")
    @ApiOperation("修改保存数据权限")
    public AjaxResult dataScope(@RequestBody CscpRoles role)
    {
        cscpRolesService.checkRoleAllowed(role);
        return toAjax(cscpRolesService.authDataScope(role));
    }
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
