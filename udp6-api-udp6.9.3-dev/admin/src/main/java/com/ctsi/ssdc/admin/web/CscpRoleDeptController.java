package com.ctsi.ssdc.admin.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.lang.Long;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctsi.ssdc.admin.service.CscpRoleDeptService;
import com.ctsi.ssdc.admin.domain.CscpRoleDept;
import com.ctsi.ssdc.admin.domain.CscpRoleDeptExample;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;


/**
 * REST controller for managing CscpRoleDept.
 *
 * @author ctsi-biyi-generator
 *
 */
@RestController
@RequestMapping("/api")
public class CscpRoleDeptController {

    private final Logger log = LoggerFactory.getLogger(CscpRoleDeptController.class);

    private static final String ENTITY_NAME = "cscpRoleDept";

    private final CscpRoleDeptService cscpRoleDeptService;

    public CscpRoleDeptController(CscpRoleDeptService cscpRoleDeptService) {
        this.cscpRoleDeptService = cscpRoleDeptService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpRoleDepts : Create a new cscpRoleDept.
     *
     * @param cscpRoleDept the cscpRoleDept to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpRoleDept, or with status 400 (Bad Request) if the cscpRoleDept has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpRoleDepts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpRoleDept")
    public ResponseEntity<CscpRoleDept> createCscpRoleDept(@RequestBody CscpRoleDept cscpRoleDept) throws URISyntaxException {
        log.debug("REST request to save CscpRoleDept : {}", cscpRoleDept);
        CscpRoleDept result = cscpRoleDeptService.insert(cscpRoleDept);
        return ResponseEntity.created(new URI("/api/cscpRoleDepts" + "/" +result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
	
    /**
     * PUT  /cscpRoleDepts : Updates an existing cscpRoleDept.
     *
     * @param cscpRoleDept the cscpRoleDept to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpRoleDept,
     * or with status 400 (Bad Request) if the cscpRoleDept is not valid,
     * or with status 500 (Internal Server Error) if the cscpRoleDept couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpRoleDepts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpRoleDept")
    public ResponseEntity<CscpRoleDept> updateCscpRoleDept(@RequestBody CscpRoleDept cscpRoleDept)  {
        log.debug("REST request to update CscpRoleDept : {}", cscpRoleDept);
        CscpRoleDept result = cscpRoleDeptService.update(cscpRoleDept);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /cscpRoleDepts : get the cscpRoleDepts with page.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpRoleDepts in body
     */
    @GetMapping("/cscpRoleDeptsByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRoleDeptsByCriteria")
    public PageResult<CscpRoleDept> getCscpRoleDeptsByCriteria(CscpRoleDeptExample cscpRoleDeptExample, Pageable page) {
        log.debug("REST request to get CscpRoleDeptsByCriteria");
        return cscpRoleDeptService.findByExample(cscpRoleDeptExample, page);
    }
    
    /**
     * GET  /cscpRoleDepts : get the cscpRoleDepts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpRoleDepts in body
     */
    @GetMapping("/cscpRoleDeptsList")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRoleDeptsList")
    public PageResult<CscpRoleDept> getCscpRoleDeptsList(CscpRoleDeptExample cscpRoleDeptExample) {
        log.debug("REST request to get CscpRoleDeptsList");
        return cscpRoleDeptService.findByExample(cscpRoleDeptExample);
    }

    /**
     * GET  /cscpRoleDepts/:id : get the "id" cscpRoleDept.
     *
     * @param id the id of the cscpRoleDept to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpRoleDept, or with status 404 (Not Found)
     */
    @GetMapping("/cscpRoleDepts/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpRoleDept")
    public ResponseEntity<CscpRoleDept> getCscpRoleDept(@PathVariable Long id) {
        log.debug("REST request to get CscpRoleDept : {}", id);
        CscpRoleDept cscpRoleDept = cscpRoleDeptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpRoleDept));
    }
	
    /**
     * DELETE  /cscpRoleDepts/:id : delete the "id" cscpRoleDept.
     *
     * @param id the id of the cscpRoleDept to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpRoleDepts/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpRoleDept")
    public ResponseEntity<Void> deleteCscpRoleDept(@PathVariable Long id) {
        log.debug("REST request to delete CscpRoleDept : {}", id);
        cscpRoleDeptService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
}
