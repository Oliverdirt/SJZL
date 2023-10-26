package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserWorkGroupDTO;
import com.ctsi.ssdc.admin.service.CscpUserWorkGroupService;
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
 * REST controller for managing CscpUserWorkGroup.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpUserWorkGroupController {

    private final Logger log = LoggerFactory.getLogger(CscpUserWorkGroupController.class);

    private static final String ENTITY_NAME = "cscpUserWorkGroup";

    private final CscpUserWorkGroupService cscpUserWorkGroupService;

    public CscpUserWorkGroupController(CscpUserWorkGroupService cscpUserWorkGroupService) {
        this.cscpUserWorkGroupService = cscpUserWorkGroupService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpUserWorkGroups : Create a new cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the cscpUserWorkGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body
     * the new cscpUserWorkGroupDTO, or with status 400 (Bad Request)
     * if the cscpUserWorkGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpUserWorkGroups")
    public ResponseEntity<CscpUserWorkGroupDTO> createCscpUserWorkGroup(
            @RequestBody CscpUserWorkGroupDTO cscpUserWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to save CscpUserWorkGroup : {}", cscpUserWorkGroupDTO);
        if (cscpUserWorkGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpUserWorkGroup cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        CscpUserWorkGroupDTO result = cscpUserWorkGroupService.insert(cscpUserWorkGroupDTO);
        return ResponseEntity.created(new URI("/api/cscpUserWorkGroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpUserWorkGroups : Updates an existing cscpUserWorkGroup.
     *
     * @param cscpUserWorkGroupDTO the cscpUserWorkGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserWorkGroupDTO,
     * or with status 400 (Bad Request) if the cscpUserWorkGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserWorkGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUserWorkGroups")

    public ResponseEntity<CscpUserWorkGroupDTO> updateCscpUserWorkGroup(
            @RequestBody CscpUserWorkGroupDTO cscpUserWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to update CscpUserWorkGroup : {}", cscpUserWorkGroupDTO);
        if (cscpUserWorkGroupDTO.getId() == null) {
            return createCscpUserWorkGroup(cscpUserWorkGroupDTO);
        }
        CscpUserWorkGroupDTO result = cscpUserWorkGroupService.update(cscpUserWorkGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpUserWorkGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpUserWorkGroups : get the cscpUserWorkGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserWorkGroups in body
     */
    @GetMapping("/cscpUserWorkGroups")

    public PageResult<CscpUserWorkGroupDTO> getCscpUserWorkGroups(
            CscpUserWorkGroupDTO cscpUserWorkGroupDTO, Pageable page) {
        log.debug("REST request to get CscpUserWorkGroups");
        return cscpUserWorkGroupService.findByCscpUserWorkGroupDTO(cscpUserWorkGroupDTO, page);
    }
    
    /**
     * GET  /cscpUserWorkGroups : get the cscpUserWorkGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserWorkGroups in body
     */
    @GetMapping("/cscpUserWorkGroupsByCriteria")

    public PageResult<CscpUserWorkGroupDTO> getCscpUserWorkGroupsByCriteria(
            CscpUserWorkGroupCriteria cscpUserWorkGroupCriteria, Pageable page) {
        log.debug("REST request to get CscpUserWorkGroupsByCriteria");
        return cscpUserWorkGroupService.
                findByCscpUserWorkGroupCriteria(cscpUserWorkGroupCriteria, page);
    }

    /**
     * GET  /cscpUserWorkGroups/:id : get the "id" cscpUserWorkGroup.
     *
     * @param id the id of the cscpUserWorkGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and
     * with body the cscpUserWorkGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpUserWorkGroups/{id}")

    public ResponseEntity<CscpUserWorkGroupDTO> getCscpUserWorkGroup(@PathVariable Long id) {
        log.debug("REST request to get CscpUserWorkGroup : {}", id);
        CscpUserWorkGroupDTO cscpUserWorkGroupDTO = cscpUserWorkGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserWorkGroupDTO));
    }

    /**
     * DELETE  /cscpUserWorkGroups/:id : delete the "id" cscpUserWorkGroup.
     *
     * @param id the id of the cscpUserWorkGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpUserWorkGroups/{id}")

    public ResponseEntity<Void> deleteCscpUserWorkGroup(@PathVariable  Long id) {
        log.debug("REST request to delete CscpUserWorkGroup : {}", id);
        cscpUserWorkGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
