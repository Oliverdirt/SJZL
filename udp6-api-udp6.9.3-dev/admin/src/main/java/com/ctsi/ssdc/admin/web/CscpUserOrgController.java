package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserOrgDTO;
import com.ctsi.ssdc.admin.service.CscpUserOrgService;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxBackData;
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
 * REST controller for managing CscpUserOrg.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpUserOrgController {

    private final Logger log = LoggerFactory.getLogger(CscpUserOrgController.class);

    private static final String ENTITY_NAME = "cscpUserOrg";

    private final CscpUserOrgService cscpUserOrgService;

    public CscpUserOrgController(CscpUserOrgService cscpUserOrgService) {
        this.cscpUserOrgService = cscpUserOrgService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpUserOrgs : Create a new cscpUserOrg.
     *
     * @param cscpUserOrgDTO the cscpUserOrgDTO to create
     * @return the ResponseEntity with status 201
     * (Created) and with body the new cscpUserOrgDTO,
     * or with status 400 (Bad Request) if the cscpUserOrg has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpUserOrgs")

    public ResponseEntity<CscpUserOrgDTO> createCscpUserOrg(
            @RequestBody CscpUserOrgDTO cscpUserOrgDTO) throws URISyntaxException {
        log.debug("REST request to save CscpUserOrg : {}", cscpUserOrgDTO);
        if (cscpUserOrgDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpUserOrg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CscpUserOrgDTO result = cscpUserOrgService.insert(cscpUserOrgDTO);
        return ResponseEntity.created(new URI("/api/cscpUserOrgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpUserOrgs : Updates an existing cscpUserOrg.
     *
     * @param cscpUserOrgDTO the cscpUserOrgDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserOrgDTO,
     * or with status 400 (Bad Request) if the cscpUserOrgDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserOrgDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUserOrgs")

    public ResponseEntity<CscpUserOrgDTO> updateCscpUserOrg(
            @RequestBody CscpUserOrgDTO cscpUserOrgDTO) throws URISyntaxException {
        log.debug("REST request to update CscpUserOrg : {}", cscpUserOrgDTO);
        if (cscpUserOrgDTO.getId() == null) {
            return createCscpUserOrg(cscpUserOrgDTO);
        }
        CscpUserOrgDTO result = cscpUserOrgService.update(cscpUserOrgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpUserOrgDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpUserOrgs : get the cscpUserOrgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserOrgs in body
     */
    @GetMapping("/cscpUserOrgs")

    public PageResult<CscpUserOrgDTO> getCscpUserOrgs(CscpUserOrgDTO cscpUserOrgDTO, Pageable page) {
        log.debug("REST request to get CscpUserOrgs");
        return cscpUserOrgService.findByCscpUserOrgDTO(cscpUserOrgDTO, page);
    }
    
    /**
     * GET  /cscpUserOrgs : get the cscpUserOrgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserOrgs in body
     */
    @GetMapping("/cscpUserOrgsByCriteria")

    public PageResult<CscpUserOrgDTO> getCscpUserOrgsByCriteria(
            CscpUserOrgCriteria cscpUserOrgCriteria, Pageable page) {
        log.debug("REST request to get CscpUserOrgsByCriteria");
        return cscpUserOrgService.findByCscpUserOrgCriteria(cscpUserOrgCriteria, page);
    }

    /**
     * GET  /cscpUserOrgs/:id : get the "id" cscpUserOrg.
     *
     * @param id the id of the cscpUserOrgDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpUserOrgDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpUserOrgs/{id}")

    public ResponseEntity<CscpUserOrgDTO> getCscpUserOrg(@PathVariable Long id) {
        log.debug("REST request to get CscpUserOrg : {}", id);
        CscpUserOrgDTO cscpUserOrgDTO = cscpUserOrgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserOrgDTO));
    }

    /**
     * DELETE  /cscpUserOrgs/:id : delete the "id" cscpUserOrg.
     *
     * @param id the id of the cscpUserOrgDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpUserOrgs/{id}")

    public ResponseEntity<Void> deleteCscpUserOrg(@PathVariable  Long id) {
        log.debug("REST request to delete CscpUserOrg : {}", id);
        cscpUserOrgService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 机构人员唯一校验
     * @param users
     * @return
     */
    @PostMapping("/org/userOnlyOne/{id}")
    public AjaxBackData userOnlyOne(@PathVariable Long id,@RequestBody  String[] users){
        int i = cscpUserOrgService.userOnlyOne(id,users);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(i>0){
            ajaxBackData.setMsg("校验成功");
            ajaxBackData.setCode(200);
            ajaxBackData.setSuccess(true);
        }else {
            ajaxBackData.setMsg("校验失败");
            ajaxBackData.setCode(500);
            ajaxBackData.setSuccess(false);
        }
        return ajaxBackData;
    }
}
