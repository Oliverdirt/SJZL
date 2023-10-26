package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpLogLoginDTO;
import com.ctsi.ssdc.admin.service.CscpLogLoginService;
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
 * REST controller for managing CscpLogLogin.
 *
 * @author ctsi biyi generator
 *
 */
@RestController
@RequestMapping("/api/system")
public class CscpLogLoginController {

    private final Logger log = LoggerFactory.getLogger(CscpLogLoginController.class);

    private final CscpLogLoginService cscpLogLoginService;

    public CscpLogLoginController(CscpLogLoginService cscpLogLoginService) {
        this.cscpLogLoginService = cscpLogLoginService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
	
    /**
     * GET  /cscpLogLogins : get the cscpLogLogins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpLogLogins in body
     */
    @GetMapping("/cscpLogLogins")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogLogins")
    public PageResult<CscpLogLoginDTO> getCscpLogLogins(CscpLogLoginDTO cscpLogLoginDTO, Pageable page) {
        log.debug("REST request to get CscpLogLogins");
        return cscpLogLoginService.findByCscpLogLoginDTO(cscpLogLoginDTO, page);
    }
    
    /**
     * GET  /cscpLogLogins : get the cscpLogLogins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpLogLogins in body
     */
    @GetMapping("/cscpLogLoginsByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogLoginsByCriteria")
    public PageResult<CscpLogLoginDTO> getCscpLogLoginsByCriteria(
            CscpLogLoginCriteria cscpLogLoginCriteria, Pageable page) {
        log.debug("REST request to get CscpLogLoginsByCriteria");
        return cscpLogLoginService.findByCscpLogLoginCriteria(cscpLogLoginCriteria, page);
    }

    /**
     * GET  /cscpLogLogins/:id : get the "id" cscpLogLogin.
     *
     * @param id the id of the cscpLogLoginDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpLogLoginDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpLogLogins/{id}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpLogLogin")
    public ResponseEntity<CscpLogLoginDTO> getCscpLogLogin(@PathVariable Long id) {
        log.debug("REST request to get CscpLogLogin : {}", id);
        CscpLogLoginDTO cscpLogLoginDTO = cscpLogLoginService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpLogLoginDTO));
    }
}
