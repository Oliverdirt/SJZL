package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleDTO;
import com.ctsi.ssdc.admin.service.CscpUserRoleService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;


/**
 * REST controller for managing CscpUserRole.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpUserRoleController {

    private final Logger log = LoggerFactory.getLogger(CscpUserRoleController.class);

    private static final String ENTITY_NAME = "cscpUserRole";

    private final CscpUserRoleService cscpUserRoleService;

    public CscpUserRoleController(CscpUserRoleService cscpUserRoleService) {
        this.cscpUserRoleService = cscpUserRoleService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpUserRoles : Create a new cscpUserRole.
     *
     * @param cscpUserRoleDTO the cscpUserRoleDTO to create
     * @return the ResponseEntity with status 201 (Created)
     * and with body the new cscpUserRoleDTO, or with status 400 (Bad Request)
     * if the cscpUserRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpUserRoles")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpUserRole")
    public ResponseEntity<CscpUserRoleDTO> createCscpUserRole(
            @RequestBody CscpUserRoleDTO cscpUserRoleDTO) throws URISyntaxException {
        log.debug("REST request to save CscpUserRole : {}", cscpUserRoleDTO);
        if (cscpUserRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpUserRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CscpUserRoleDTO result = cscpUserRoleService.insert(cscpUserRoleDTO);
        return ResponseEntity.created(new URI("/api/cscpUserRoles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpUserRoles : Updates an existing cscpUserRole.
     *
     * @param cscpUserRoleDTO the cscpUserRoleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserRoleDTO,
     * or with status 400 (Bad Request) if the cscpUserRoleDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserRoleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUserRoles")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpUserRole")
    public ResponseEntity<CscpUserRoleDTO> updateCscpUserRole(
            @RequestBody CscpUserRoleDTO cscpUserRoleDTO) throws URISyntaxException {
        log.debug("REST request to update CscpUserRole : {}", cscpUserRoleDTO);
        if (cscpUserRoleDTO.getId() == null) {
            return createCscpUserRole(cscpUserRoleDTO);
        }
        CscpUserRoleDTO result = cscpUserRoleService.update(cscpUserRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpUserRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpUserRoles : get the cscpUserRoles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserRoles in body
     */
    @GetMapping("/cscpUserRoles")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserRoles")
    public PageResult<CscpUserRoleDTO> getCscpUserRoles(CscpUserRoleDTO cscpUserRoleDTO, Pageable page) {
        log.debug("REST request to get CscpUserRoles");
        return cscpUserRoleService.findByCscpUserRoleDTO(cscpUserRoleDTO, page);
    }
    
    /**
     * GET  /cscpUserRoles : get the cscpUserRoles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserRoles in body
     */
    @GetMapping("/cscpUserRolesByCriteria")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserRolesByCriteria")
    public PageResult<CscpUserRoleDTO> getCscpUserRolesByCriteria(
            CscpUserRoleCriteria cscpUserRoleCriteria, Pageable page) {
        log.debug("REST request to get CscpUserRolesByCriteria");
        return cscpUserRoleService.findByCscpUserRoleCriteria(cscpUserRoleCriteria, page);
    }

    /**
     * GET  /cscpUserRoles/:id : get the "id" cscpUserRole.
     *
     * @param id the id of the cscpUserRoleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpUserRoleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpUserRoles/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserRole")
    public ResponseEntity<CscpUserRoleDTO> getCscpUserRole(@PathVariable Long id) {
        log.debug("REST request to get CscpUserRole : {}", id);
        CscpUserRoleDTO cscpUserRoleDTO = cscpUserRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserRoleDTO));
    }

    /**
     * DELETE  /cscpUserRoles/:id : delete the "id" cscpUserRole.
     *
     * @param id the id of the cscpUserRoleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpUserRoles/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpUserRole")
    public ResponseEntity<Void> deleteCscpUserRole(@PathVariable  Long id) {
        log.debug("REST request to delete CscpUserRole : {}", id);
        cscpUserRoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/cscpRoleUsers/{roleId}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "cscpRoleUsers")
    public Long cscpRoleUsers(@PathVariable Long roleId){
        log.debug("get user by roleId : {}", roleId);
        if(roleId == 1){
            throw new BadRequestAlertException("删除失败,管理员角色不能删除",ENTITY_NAME,"error");
        }
        return cscpUserRoleService.getUsersByRoleId(roleId);
    }
}
