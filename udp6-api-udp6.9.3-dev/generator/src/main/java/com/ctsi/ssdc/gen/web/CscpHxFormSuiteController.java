package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuite;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuiteExample;
import com.ctsi.ssdc.gen.service.CscpHxFormSuiteService;
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

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


/**
 * REST controller for managing CodeTemplateSuite.
 *
 * @author ctsi-biyi-generator
 *
 */
@RestController
@RequestMapping("/api")
public class CscpHxFormSuiteController {

    private final Logger log = LoggerFactory.getLogger(CscpHxFormSuiteController.class);

    private static final String ENTITY_NAME = "codeTemplateSuite";

    private final CscpHxFormSuiteService codeTemplateSuiteService;

    public CscpHxFormSuiteController(CscpHxFormSuiteService codeTemplateSuiteService) {
        this.codeTemplateSuiteService = codeTemplateSuiteService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpHxFormSuites : Create a new codeTemplateSuite.
     *
     * @param codeTemplateSuite the codeTemplateSuite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeTemplateSuite, or with status 400 (Bad Request) if the codeTemplateSuite has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/cscpHxFormSuites")
////    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
//    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "createCodeTemplateSuite")
//    public ResponseEntity<CscpHxFormSuite> createCodeTemplateSuite(@ModelAttribute CscpHxFormSuite codeTemplateSuite, @RequestParam MultipartFile multipartFile) throws URISyntaxException {
//        log.debug("REST request to save CodeTemplateSuite : {}", codeTemplateSuite);
//        if (codeTemplateSuite.getId() != null) {
//            throw new BadRequestAlertException("A new codeTemplateSuite cannot already have an id", ENTITY_NAME, "idexists");
//        }
//        CscpHxFormSuite result = codeTemplateSuiteService.insert(codeTemplateSuite,multipartFile);
//        return ResponseEntity.created(new URI("/api/codeTemplateSuites" + "/" +result.getId() ))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * PUT  /cscpHxFormSuites : Updates an existing codeTemplateSuite.
     *
     * @param codeTemplateSuite the codeTemplateSuite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codeTemplateSuite,
     * or with status 400 (Bad Request) if the codeTemplateSuite is not valid,
     * or with status 500 (Internal Server Error) if the codeTemplateSuite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("//cscpHxFormSuites/edit")
////    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
//    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "updateCodeTemplateSuite")
//    public ResponseEntity<CscpHxFormSuite> updateCodeTemplateSuite(@ModelAttribute CscpHxFormSuite codeTemplateSuite, HttpServletRequest request) throws URISyntaxException {
//        log.debug("REST request to update CodeTemplateSuite : {}", codeTemplateSuite);
//        MultipartFile multipartFile = null;
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        if (isMultipart){
//            MultipartHttpServletRequest multipartRequest =
//                    WebUtils.getNativeRequest(request,MultipartHttpServletRequest.class);
//            multipartFile = multipartRequest.getFile("multipartFile");
//        }
//	    if (codeTemplateSuite.getId() == null) {
//	    	return createCodeTemplateSuite(codeTemplateSuite,multipartFile);
//	    }
//        CscpHxFormSuite result = codeTemplateSuiteService.update(codeTemplateSuite,multipartFile);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /cscpHxFormSuites : get the codeTemplateSuites with page.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of codeTemplateSuites in body
     */
    @GetMapping("/cscpHxFormSuitesByCriteria")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuitesByCriteria")
    @Log
    public PageResult<CscpHxFormSuite> getCodeTemplateSuitesByCriteria(CscpHxFormSuiteExample codeTemplateSuiteExample, Pageable page) {
        log.debug("REST request to get CodeTemplateSuitesByCriteria");
        return codeTemplateSuiteService.findByExample(codeTemplateSuiteExample, page);
    }

    /**
     * GET  /cscpHxFormSuites : get the codeTemplateSuites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of codeTemplateSuites in body
     */
    @GetMapping("/cscpHxFormSuitesList")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuitesList")
    @Log
    public PageResult<CscpHxFormSuite> getCodeTemplateSuitesList(CscpHxFormSuiteExample codeTemplateSuiteExample) {
        log.debug("REST request to get CodeTemplateSuitesList");
        return codeTemplateSuiteService.findByExample(codeTemplateSuiteExample);
    }

    /**
     * GET  /cscpHxFormSuites/:id : get the "id" codeTemplateSuite.
     *
     * @param id the id of the codeTemplateSuite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codeTemplateSuite, or with status 404 (Not Found)
     */
    @GetMapping("/cscpHxFormSuites/{id}")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuite")
    @Log
    public ResponseEntity<CscpHxFormSuite> getCodeTemplateSuite(@PathVariable Long id) {
        log.debug("REST request to get CodeTemplateSuite : {}", id);
        CscpHxFormSuite codeTemplateSuite = codeTemplateSuiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeTemplateSuite));
    }

    /**
     * DELETE  /cscpHxFormSuites/:id : delete the "id" codeTemplateSuite.
     *
     * @param id the id of the codeTemplateSuite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpHxFormSuites/{id}")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCodeTemplateSuite")
    @Log
    public ResponseEntity<Void> deleteCodeTemplateSuite(@PathVariable Long id) {
        log.debug("REST request to delete CodeTemplateSuite : {}", id);
        codeTemplateSuiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
