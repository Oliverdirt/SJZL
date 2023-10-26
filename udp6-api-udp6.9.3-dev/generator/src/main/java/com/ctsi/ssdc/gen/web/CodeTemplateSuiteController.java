package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuite;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuiteExample;
import com.ctsi.ssdc.gen.service.CodeTemplateSuiteService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
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
public class CodeTemplateSuiteController {

    private final Logger log = LoggerFactory.getLogger(CodeTemplateSuiteController.class);

    private static final String ENTITY_NAME = "codeTemplateSuite";

    private final CodeTemplateSuiteService codeTemplateSuiteService;

    public CodeTemplateSuiteController(CodeTemplateSuiteService codeTemplateSuiteService) {
        this.codeTemplateSuiteService = codeTemplateSuiteService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 新增自定义模板
     * @param codeTemplateSuite 模板对象
     * @param multipartFile 模板文件
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/codeTemplateSuites")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "createCodeTemplateSuite")
    @Log(message = "新增自定义模板")
    public ResponseEntity<CodeTemplateSuite> createCodeTemplateSuite(@ModelAttribute CodeTemplateSuite codeTemplateSuite,@RequestParam MultipartFile multipartFile) throws URISyntaxException {
        log.debug("REST request to save CodeTemplateSuite : {}", codeTemplateSuite);
        if (codeTemplateSuite.getId() != null) {
            throw new BadRequestAlertException("A new codeTemplateSuite cannot already have an id", ENTITY_NAME, "idexists");
        }
        CodeTemplateSuite result = codeTemplateSuiteService.insert(codeTemplateSuite,multipartFile);
        return ResponseEntity.created(new URI("/api/codeTemplateSuites" + "/" +result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 更新自定义模板
     * @param codeTemplateSuite 模板对象
     * @param request 请求体
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/codeTemplateSuites/edit")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "updateCodeTemplateSuite")
    @Log(message = "更新自定义模板")
    public ResponseEntity<CodeTemplateSuite> updateCodeTemplateSuite(@ModelAttribute CodeTemplateSuite codeTemplateSuite,HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update CodeTemplateSuite : {}", codeTemplateSuite);
        MultipartFile multipartFile = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart){
            MultipartHttpServletRequest multipartRequest =
                    WebUtils.getNativeRequest(request,MultipartHttpServletRequest.class);
            multipartFile = multipartRequest.getFile("multipartFile");
        }
	    if (codeTemplateSuite.getId() == null) {
	    	return createCodeTemplateSuite(codeTemplateSuite,multipartFile);
	    }
        CodeTemplateSuite result = codeTemplateSuiteService.update(codeTemplateSuite,multipartFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 根据条件分页查询模板列表
     * @param codeTemplateSuiteExample 查询条件
     * @param page 分页条件
     * @return
     */
    @GetMapping("/codeTemplateSuitesByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuitesByCriteria")
    @Log(message = "根据条件分页查询模板列表" )
    public PageResult<CodeTemplateSuite> getCodeTemplateSuitesByCriteria(CodeTemplateSuiteExample codeTemplateSuiteExample, Pageable page) {
        log.debug("REST request to get CodeTemplateSuitesByCriteria");
        return codeTemplateSuiteService.findByExample(codeTemplateSuiteExample, page);
    }

    /**
     * 根据条件查询全部模板列表
     * @param codeTemplateSuiteExample 查询条件
     * @return
     */
    @GetMapping("/codeTemplateSuitesList")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuitesList")
    @Log(message = "根据条件查询全部模板列表")
    public PageResult<CodeTemplateSuite> getCodeTemplateSuitesList(CodeTemplateSuiteExample codeTemplateSuiteExample) {
        log.debug("REST request to get CodeTemplateSuitesList");
        return codeTemplateSuiteService.findByExample(codeTemplateSuiteExample);
    }

    /**
     * 根据主键获取模板信息
     * @param id 主键id
     * @return
     */
    @GetMapping("/codeTemplateSuites/{id}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCodeTemplateSuite")
    @Log(message = "根据主键获取模板信息")
    public ResponseEntity<CodeTemplateSuite> getCodeTemplateSuite(@PathVariable Long id) {
        log.debug("REST request to get CodeTemplateSuite : {}", id);
        CodeTemplateSuite codeTemplateSuite = codeTemplateSuiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeTemplateSuite));
    }

    /**
     * 根据主键id删除模板
     * @param id 主键
     * @return
     */
    @DeleteMapping("/codeTemplateSuites/{id}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCodeTemplateSuite")
    @Log(message = "根据主键id删除模板")
    public ResponseEntity<Void> deleteCodeTemplateSuite(@PathVariable Long id) {
        log.debug("REST request to delete CodeTemplateSuite : {}", id);
        codeTemplateSuiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * 根据主键列表，批量删除模板
     * @param ids 主键列表
     * @return
     */
    @DeleteMapping("/codeTemplateSuites/delAll")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCodeTemplateSuite")
    @Log(message = "根据主键列表，批量删除模板")
    public ResponseEntity<Void> deleteCodeTemplateSuite(Long[] ids) {

        log.debug("REST request to delete ids");

        codeTemplateSuiteService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

}
