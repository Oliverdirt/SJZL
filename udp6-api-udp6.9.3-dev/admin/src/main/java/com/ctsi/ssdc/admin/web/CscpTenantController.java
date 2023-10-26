package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.CscpTenantExample;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.annotation.RepeatSubmit;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.AjaxForm;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing CscpTenant.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-tenant-controller"})
@RestController
@RequestMapping("/api")
public class CscpTenantController {

    private final Logger log = LoggerFactory.getLogger(CscpTenantController.class);

    private static final String ENTITY_NAME = "cscpTenant";

    private final CscpTenantService cscpTenantService;

    public CscpTenantController(CscpTenantService cscpTenantService) {
        this.cscpTenantService = cscpTenantService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpTenants : Create a new cscpTenant.
     *
     * @param cscpTenant the cscpTenant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpTenant, or with status 400 (Bad Request) if the cscpTenant has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpTenant",name = "cscpTenant",value = "the cscpTenant to create")
    })
    @ApiOperation(value = "POST  /cscpTenants : create a new cscpTenant.",notes = "POST  /cscpTenants : create a new cscpTenant.",httpMethod = "POST")
    @PostMapping("/cscpTenants")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpRoles")
    public ResponseEntity<CscpTenant> createCscpTenant(@RequestBody CscpTenant cscpTenant) throws Exception {
        log.debug("REST request to save CscpTenant : {}", cscpTenant);
        CscpTenant result = cscpTenantService.insert(cscpTenant);
        return ResponseEntity.created(new URI("/api/cscpTenants" + "/" +result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @RepeatSubmit
    @ApiOperation(value = "新增租户")
    @PostMapping("/insertTenant")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpRoles")
    public ResponseEntity<AjaxForm> insertTenant(@RequestBody CscpTenant tenant) throws Exception  {
        log.debug("REST request to save CscpTenant : {}", tenant);
        cscpTenantService.insertTenant(tenant);
        if(tenant.getId() != null){
            return new ResponseEntity(new AjaxForm(true, "新增租户成功"), HttpStatus.OK);
        }else{
            return new ResponseEntity(new AjaxForm(false, "新增租户失败"), HttpStatus.OK);
        }

    }

    @ApiOperation(value = "修改租户")
    @PostMapping("/updateTenant")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpRoles")
    public ResponseEntity<AjaxForm> updateTenant(@RequestBody CscpTenant tenant) throws URISyntaxException {
        log.debug("REST request to save CscpTenant : {}", tenant);
        cscpTenantService.updateTenant(tenant);
        return new ResponseEntity(new AjaxForm(true, ""), HttpStatus.OK);
    }
    /**
     * PUT  /cscpTenants : Updates an existing cscpTenant.
     *
     * @param cscpTenant the cscpTenant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpTenant,
     * or with status 400 (Bad Request) if the cscpTenant is not valid,
     * or with status 500 (Internal Server Error) if the cscpTenant couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpTenant",name = "cscpTenant",value = "the cscpTenant to update")
    })
    @ApiOperation(value = "PUT  /cscpTenants : updates an existing cscpTenant.",notes = "PUT  /cscpTenants : updates an existing cscpTenant.",httpMethod = "PUT")
    @PutMapping("/cscpTenants")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpTenant")
    public ResponseEntity<CscpTenant> updateCscpTenant(@RequestBody CscpTenant cscpTenant)  {
        log.debug("REST request to update CscpTenant : {}", cscpTenant);
        CscpTenant result = cscpTenantService.update(cscpTenant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpTenants/:id : get the "id" cscpTenant.
     *
     * @param id the id of the cscpTenant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpTenant, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpTenant to retrieve")
    })
    @ApiOperation(value = "GET  /cscpTenants/id : get the id cscpTenant.",notes = "GET  /cscpTenants/id : get the id cscpTenant.",httpMethod = "GET")
    @GetMapping("/cscpTenants/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpTenant")
    public ResponseEntity<CscpTenant> getCscpTenant(@PathVariable Long id) {
        log.debug("REST request to get CscpTenant : {}", id);
        CscpTenant cscpTenant = cscpTenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpTenant));
    }

    /**
    * GET  /cscpTenants : 获取列表分页
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpTenants in body
    */
    @ApiOperation(value = "获取列表分页")
    @GetMapping("/cscpTenants")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpTenantsList")
    public PageResult<CscpTenant> getCscpTenantsList(CscpTenantExample cscpTenantExample, Pageable pageable) {
        log.debug("REST request to get CscpTenantsList");
        return cscpTenantService.findByExample(cscpTenantExample,pageable);

    }
	
    /**
     * DELETE  /cscpTenants/:id : delete the "id" cscpTenant.
     *
     * @param id the id of the cscpTenant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpTenant to delete")
    })
    @ApiOperation(value = "DELETE  /cscpTenants/id : delete the id cscpTenant.",notes = "DELETE  /cscpTenants/id : delete the id cscpTenant.",httpMethod = "DELETE")
    @DeleteMapping("/cscpTenants/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpTenant")
    public ResponseEntity<Void> deleteCscpTenant(@PathVariable Long id) {
        log.debug("REST request to delete CscpTenant : {}", id);
        cscpTenantService.deleteTenantById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
    * GET  /cscpTenants/:id : get the "id" cscpTenant.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpTenant, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpTenant to retrieve")
    })
    @ApiOperation(value = "POST  /cscpTenants/export : export the cscpTenant to excel",notes = "POST  /cscpTenants/export : export the cscpTenant to excel",httpMethod = "POST")
    @PostMapping("/cscpTenants/export")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "exportTenants")
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpTenant");
        PageResult<CscpTenant> result = cscpTenantService.findAll();
        List<CscpTenant> list = result.getData();
        ExcelUtil<CscpTenant> util = new ExcelUtil<CscpTenant>(CscpTenant.class);
        return util.exportExcel(list, "cscpTenant");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "ids", value = "the id of the tenant to delete")
    })
    @ApiOperation(value = "DELETE  /jobs : delete the tenants.", notes = "DELETE  /ids : delete the ids .", httpMethod = "DELETE")
    @DeleteMapping("/cscpTenants/delAll")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteTenants")
    public ResponseEntity<Void> deleteTenants(Long[] ids) {
        log.debug("REST request to delete Job : {}", ids);
        cscpTenantService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(ids))).build();
    }

    /**
     * 根据租户id批量菜单
     * @param tenantId
     * @return
     */

    @ApiOperation(value = "获取菜单")
    @GetMapping("/cscpTenants/getMenu/{tenantId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpTenantMenu")
    public ResponseEntity<List<CscpMenusDTO>> getCscpTenantMenu(@PathVariable Long tenantId) {
        log.debug("REST request to get CscpTenant : {}", tenantId);
        List<CscpMenusDTO> cscpTenantMenuList = cscpTenantService.getCscpTenantMenu(tenantId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpTenantMenuList));
    }


    /**
     * 租户状态修改
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpTenant", name = "cscpTenant", value = "change cscpTenant status")
    })
    @ApiOperation(value = "PUT  /cscpTenants/changeStatus : updates an existing cscpTenant.", notes = "PUT  /cscpTenants : updates an existing cscpTenant.", httpMethod = "PUT")
    @PutMapping("/cscpTenants/changeStatus")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "changeStatus")
    public ResponseEntity<Long> changeStatus(@RequestBody CscpTenant cscpTenant){
        CscpTenant tenant = cscpTenantService.findOne(cscpTenant.getId());
        tenant.setActiveFlag(cscpTenant.getActiveFlag());
        CscpTenant result = cscpTenantService.changeStatus(tenant);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result.getId());
    }

    @ApiOperation("唯一性校验")
    @GetMapping("/cscpTenant/checkTenantAccountExist")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkTenantAccountExist")
    public ResponseEntity<AjaxBackData> checkTenantAccountExist(String tenantAccount){
        CscpTenant cscpTenant = new CscpTenant();
        cscpTenant.setTenantAccount(tenantAccount);
        boolean flag = cscpTenantService.checkTenantAccountExist(cscpTenant);
        AjaxBackData ajaxBackData = new AjaxBackData(flag,"租户账号已存在");
        return ResponseEntity.ok()
                .body(ajaxBackData);
    }

    @ApiOperation("获取所有租户")
    @GetMapping("/cscpTenant/getAll")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getAll")
    public ResponseEntity<List<CscpTenant>> getAll(String name){
        List<CscpTenant> all = cscpTenantService.getAll(name);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(all));
    }

    @ApiOperation("根据username获取租户")
    @GetMapping("/cscpTenant/selectTenant")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "selectTenant")
    public ResponseEntity<List<CscpTenant>> selectTenant(String username) {
        List<CscpTenant> all = cscpTenantService.selectByUsername(username);
        Long currentTenantId = SecurityHxUtils.getCurrentTenantId();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(all));
    }
}
