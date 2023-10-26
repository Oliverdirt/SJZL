package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpWorkGroupDTO;
import com.ctsi.ssdc.admin.service.CscpWorkGroupService;
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
 * REST controller for managing CscpWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpWorkGroupController {

    private final Logger log = LoggerFactory.getLogger(CscpWorkGroupController.class);

    private static final String ENTITY_NAME = "cscpWorkGroup";

    private final CscpWorkGroupService cscpWorkGroupService;
    
    public CscpWorkGroupController(CscpWorkGroupService cscpWorkGroupService) {
        this.cscpWorkGroupService = cscpWorkGroupService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpWorkGroups : Create a new cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the cscpWorkGroupDTO to create
     * @return the ResponseEntity with status 201 (Created)
     * and with body the new cscpWorkGroupDTO, or with status 400
     * (Bad Request) if the cscpWorkGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpWorkGroups")

    public ResponseEntity<CscpWorkGroupDTO> createCscpWorkGroup(
            @RequestBody CscpWorkGroupDTO cscpWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to save CscpWorkGroup : {}", cscpWorkGroupDTO);
        if (cscpWorkGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpWorkGroup cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        CscpWorkGroupDTO result = cscpWorkGroupService.insert(cscpWorkGroupDTO);
        return ResponseEntity.created(new URI("/api/cscpWorkGroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
                    result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpWorkGroups : Updates an existing cscpWorkGroup.
     *
     * @param cscpWorkGroupDTO the cscpWorkGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpWorkGroupDTO,
     * or with status 400 (Bad Request) if the cscpWorkGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpWorkGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpWorkGroups")

    public ResponseEntity<CscpWorkGroupDTO> updateCscpWorkGroup(
            @RequestBody CscpWorkGroupDTO cscpWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to update CscpWorkGroup : {}", cscpWorkGroupDTO);
        if (cscpWorkGroupDTO.getId() == null) {
            return createCscpWorkGroup(cscpWorkGroupDTO);
        }
        CscpWorkGroupDTO result = cscpWorkGroupService.update(cscpWorkGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpWorkGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpWorkGroups : get the cscpWorkGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpWorkGroups in body
     */
    @GetMapping("/cscpWorkGroups")

    public PageResult<CscpWorkGroupDTO> getCscpWorkGroups(CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page) {
        log.debug("REST request to get CscpWorkGroups");
        return cscpWorkGroupService.findByCscpWorkGroupDTO(cscpWorkGroupDTO, page);
    }
    
    /**
     * GET  /cscpWorkGroups : get the cscpWorkGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpWorkGroups in body
     */
    @GetMapping("/cscpWorkGroupsByCriteria")

    public PageResult<CscpWorkGroupDTO> getCscpWorkGroupsByCriteria(
            CscpWorkGroupCriteria cscpWorkGroupCriteria, Pageable page) {
        log.debug("REST request to get CscpWorkGroupsByCriteria");
        return cscpWorkGroupService.findByCscpWorkGroupCriteria(cscpWorkGroupCriteria, page);
    }

    /**
     * GET  /cscpWorkGroups/:id : get the "id" cscpWorkGroup.
     *
     * @param id the id of the cscpWorkGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK)
     * and with body the cscpWorkGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpWorkGroups/{id}")

    public ResponseEntity<CscpWorkGroupDTO> getCscpWorkGroup(@PathVariable Long id) {
        log.debug("REST request to get CscpWorkGroup : {}", id);
        CscpWorkGroupDTO cscpWorkGroupDTO = cscpWorkGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpWorkGroupDTO));
    }

    /**
     * DELETE  /cscpWorkGroups/:id : delete the "id" cscpWorkGroup.
     *
     * @param id the id of the cscpWorkGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpWorkGroups/{id}")

    public ResponseEntity<Void> deleteCscpWorkGroup(@PathVariable  Long id) {
        log.debug("REST request to delete CscpWorkGroup : {}", id);
        cscpWorkGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/cscpWorkGroupsOr")

    public PageResult<CscpWorkGroupDTO> getCscpWorkGroupsOr(CscpWorkGroupDTO cscpWorkGroupDTO, Pageable page) {
        log.debug("REST request to get CscpWorkGroupsByCriteria");
        return cscpWorkGroupService.findByCscpWorkGroupDtoOr(cscpWorkGroupDTO, page);
    }
    
}
