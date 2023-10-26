package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupOrgDTO;
import com.ctsi.ssdc.admin.service.CscpWorkGroupOrgService;
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
 * REST controller for managing CscpWorkGroupOrg.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpWorkGroupOrgController {

    private final Logger log = LoggerFactory.getLogger(CscpWorkGroupOrgController.class);

    private static final String ENTITY_NAME = "cscpWorkGroupOrg";

    private final CscpWorkGroupOrgService cscpWorkGroupOrgService;

    public CscpWorkGroupOrgController(CscpWorkGroupOrgService cscpWorkGroupOrgService) {
        this.cscpWorkGroupOrgService = cscpWorkGroupOrgService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpWorkGroupOrgs : Create a new cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the cscpWorkGroupOrgDTO to create
     * @return the ResponseEntity with status 201 (Created) and
     * with body the new cscpWorkGroupOrgDTO, or with status 400
     * (Bad Request) if the cscpWorkGroupOrg has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpWorkGroupOrgs")

    public ResponseEntity<CscpWorkGroupOrgDTO> createCscpWorkGroupOrg(
            @RequestBody CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO) throws URISyntaxException {
        log.debug("REST request to save CscpWorkGroupOrg : {}", cscpWorkGroupOrgDTO);
        if (cscpWorkGroupOrgDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpWorkGroupOrg cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        CscpWorkGroupOrgDTO result = cscpWorkGroupOrgService.insert(cscpWorkGroupOrgDTO);
        return ResponseEntity.created(new URI("/api/cscpWorkGroupOrgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpWorkGroupOrgs : Updates an existing cscpWorkGroupOrg.
     *
     * @param cscpWorkGroupOrgDTO the cscpWorkGroupOrgDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpWorkGroupOrgDTO,
     * or with status 400 (Bad Request) if the cscpWorkGroupOrgDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpWorkGroupOrgDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpWorkGroupOrgs")

    public ResponseEntity<CscpWorkGroupOrgDTO> updateCscpWorkGroupOrg(
            @RequestBody CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO) throws URISyntaxException {
        log.debug("REST request to update CscpWorkGroupOrg : {}", cscpWorkGroupOrgDTO);
        if (cscpWorkGroupOrgDTO.getId() == null) {
            return createCscpWorkGroupOrg(cscpWorkGroupOrgDTO);
        }
        CscpWorkGroupOrgDTO result = cscpWorkGroupOrgService.update(cscpWorkGroupOrgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpWorkGroupOrgDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpWorkGroupOrgs : get the cscpWorkGroupOrgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpWorkGroupOrgs in body
     */
    @GetMapping("/cscpWorkGroupOrgs")

    public PageResult<CscpWorkGroupOrgDTO> getCscpWorkGroupOrgs(
            CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO, Pageable page) {
        log.debug("REST request to get CscpWorkGroupOrgs");
        return cscpWorkGroupOrgService.findByCscpWorkGroupOrgDTO(cscpWorkGroupOrgDTO, page);
    }
    
    /**
     * GET  /cscpWorkGroupOrgs : get the cscpWorkGroupOrgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpWorkGroupOrgs in body
     */
    @GetMapping("/cscpWorkGroupOrgsByCriteria")

    public PageResult<CscpWorkGroupOrgDTO> getCscpWorkGroupOrgsByCriteria(
            CscpWorkGroupOrgCriteria cscpWorkGroupOrgCriteria, Pageable page) {
        log.debug("REST request to get CscpWorkGroupOrgsByCriteria");
        return cscpWorkGroupOrgService.findByCscpWorkGroupOrgCriteria(cscpWorkGroupOrgCriteria, page);
    }

    /**
     * GET  /cscpWorkGroupOrgs/:id : get the "id" cscpWorkGroupOrg.
     *
     * @param id the id of the cscpWorkGroupOrgDTO to retrieve
     * @return the ResponseEntity with status 200 (OK)
     * and with body the cscpWorkGroupOrgDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpWorkGroupOrgs/{id}")

    public ResponseEntity<CscpWorkGroupOrgDTO> getCscpWorkGroupOrg(@PathVariable Long id) {
        log.debug("REST request to get CscpWorkGroupOrg : {}", id);
        CscpWorkGroupOrgDTO cscpWorkGroupOrgDTO = cscpWorkGroupOrgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpWorkGroupOrgDTO));
    }

    /**
     * DELETE  /cscpWorkGroupOrgs/:id : delete the "id" cscpWorkGroupOrg.
     *
     * @param id the id of the cscpWorkGroupOrgDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpWorkGroupOrgs/{id}")

    public ResponseEntity<Void> deleteCscpWorkGroupOrg(@PathVariable  Long id) {
        log.debug("REST request to delete CscpWorkGroupOrg : {}", id);
        cscpWorkGroupOrgService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
