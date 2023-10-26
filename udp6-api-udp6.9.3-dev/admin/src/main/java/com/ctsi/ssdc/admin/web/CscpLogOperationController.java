package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogOperationDTO;
import com.ctsi.ssdc.admin.service.CscpLogOperationService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


/**
 * REST controller for managing CscpLogOperation.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpLogOperationController {

    private final Logger log = LoggerFactory.getLogger(CscpLogOperationController.class);

    private final CscpLogOperationService cscpLogOperationService;

    public CscpLogOperationController(CscpLogOperationService cscpLogOperationService) {
        this.cscpLogOperationService = cscpLogOperationService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 

    /**
     * GET  /cscpLogOperations : get the cscpLogOperations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpLogOperations in body
     */
    @GetMapping("/cscpLogOperations")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogOperations")
    public PageResult<CscpLogOperationDTO> getCscpLogOperations(
            CscpLogOperationDTO cscpLogOperationDTO, Pageable page) {
        log.debug("REST request to get CscpLogOperations");
        return cscpLogOperationService.findByCscpLogOperationDTO(cscpLogOperationDTO, page);
    }
    
    /**
     * GET  /cscpLogOperations : get the cscpLogOperations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpLogOperations in body
     */
    @GetMapping("/cscpLogOperationsByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogOperationsByCriteria")
    public PageResult<CscpLogOperationDTO> getCscpLogOperationsByCriteria(
            CscpLogOperationCriteria cscpLogOperationCriteria, Pageable page) {
        log.debug("REST request to get CscpLogOperationsByCriteria");
        return cscpLogOperationService.findByCscpLogOperationCriteria(cscpLogOperationCriteria, page);
    }

    /**
     * GET  /cscpLogOperations/:id : get the "id" cscpLogOperation.
     *
     * @param id the id of the cscpLogOperationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK)
     * and with body the cscpLogOperationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpLogOperations/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogOperation")
    public ResponseEntity<CscpLogOperationDTO> getCscpLogOperation(@PathVariable Long id) {
        log.debug("REST request to get CscpLogOperation : {}", id);
        CscpLogOperationDTO cscpLogOperationDTO = cscpLogOperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpLogOperationDTO));
    }
}
