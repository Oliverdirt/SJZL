package com.ctsi.ssdc.admin.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import java.lang.Long;

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
import com.ctsi.ssdc.admin.service.CscpUserPostService;
import com.ctsi.ssdc.admin.domain.CscpUserPostExample;
import com.ctsi.ssdc.admin.domain.CscpUserPost;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * REST controller for managing CscpUserPost.
 *
 * @author hx
 * @date 2022-08-29 10:56:22
 *
 */

@Api(value = "/api",tags = {"cscp-user-post-controller"})
@RestController
@RequestMapping("/api/system")
public class CscpUserPostController {


    private final Logger log = LoggerFactory.getLogger(CscpUserPostController.class);

    private static final String ENTITY_NAME = "cscpUserPost";

    private final CscpUserPostService cscpUserPostService;

    public CscpUserPostController(CscpUserPostService cscpUserPostService) {
        this.cscpUserPostService = cscpUserPostService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpUserPosts : Create a new cscpUserPost.
     *
     * @param cscpUserPost the cscpUserPost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpUserPost, or with status 400 (Bad Request) if the cscpUserPost has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpUserPost",name = "cscpUserPost",value = "the cscpUserPost to create")
    })
    @ApiOperation(value = "POST  /cscpUserPosts : create a new cscpUserPost.",notes = "POST  /cscpUserPosts : create a new cscpUserPost.",httpMethod = "POST")
    @PostMapping("/cscpUserPosts")
    public ResponseEntity<CscpUserPost> createCscpUserPost(@RequestBody  CscpUserPost cscpUserPost) throws URISyntaxException {

        log.debug("REST request to save CscpUserPost : {}", cscpUserPost);

        CscpUserPost result = cscpUserPostService.insert(cscpUserPost);
        return ResponseEntity.created(new URI("/api/cscpUserPosts/" + result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /cscpUserPosts : Updates an existing cscpUserPost.
     *
     * @param cscpUserPost the cscpUserPost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserPost,
     * or with status 400 (Bad Request) if the cscpUserPost is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserPost couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpUserPost",name = "cscpUserPost",value = "the cscpUserPost to update")
    })
    @ApiOperation(value = "PUT  /cscpUserPosts : updates an existing cscpUserPost.",notes = "PUT  /cscpUserPosts : updates an existing cscpUserPost.",httpMethod = "PUT")
    @PutMapping("/cscpUserPosts")
    public ResponseEntity<CscpUserPost> updateCscpUserPost(@RequestBody  CscpUserPost cscpUserPost) throws URISyntaxException {

        log.debug("REST request to update CscpUserPost : {}", cscpUserPost);

        if (cscpUserPost.getId() == null) {
            return createCscpUserPost(cscpUserPost);
        }
        CscpUserPost result = cscpUserPostService.update(cscpUserPost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpUserPosts/:id : get the "id" cscpUserPost.
     *
     * @param id the id of the cscpUserPost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpUserPost, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpUserPost to retrieve")
    })
    @ApiOperation(value = "GET  /cscpUserPosts/id : get the id cscpUserPost.",notes = "GET  /cscpUserPosts/id : get the id cscpUserPost.",httpMethod = "GET")
    @GetMapping("/cscpUserPosts/{id}")
    public ResponseEntity<CscpUserPost> getCscpUserPost(@PathVariable Long id) {

        log.debug("REST request to get CscpUserPost : {}", id);

        CscpUserPost cscpUserPost = cscpUserPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserPost));
    }

    /**
     * GET  /cscpUserPosts : get the cscpUserPosts .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserPosts in body
     */
    @ApiOperation(value = "GET  /cscpUserPosts ")
    @GetMapping("/cscpUserPosts")
    public PageResult<CscpUserPost> getCscpUserPostsList(CscpUserPostExample cscpUserPostExample,Pageable pageable) {

        log.debug("REST request to get CscpUserPostsList");

        return cscpUserPostService.findByExample(cscpUserPostExample,pageable);
    }

    /**
     * DELETE  /cscpUserPosts/:id : delete the "id" cscpUserPost.
     *
     * @param id the id of the cscpUserPost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpUserPost to delete")
    })
    @ApiOperation(value = "DELETE  /cscpUserPosts/id : delete the id cscpUserPost.",notes = "DELETE  /cscpUserPosts/id : delete the id cscpUserPost.",httpMethod = "DELETE")
    @DeleteMapping("/cscpUserPosts/{id}")
    public ResponseEntity<Void> deleteCscpUserPost(@PathVariable Long id) {
        log.debug("REST request to delete CscpUserPost : {}", id);
        cscpUserPostService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /cscpUserPosts/:id : get the "id" cscpUserPost.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpUserPost, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpUserPosts/export : export the cscpUserPost to excel",notes = "DELETE  /cscpUserPosts/id : delete the id cscpUserPost.",httpMethod = "DELETE")
    @PostMapping("/cscpUserPosts/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpUserPost");

        PageResult<CscpUserPost> result = cscpUserPostService.findAll();
        List<CscpUserPost> list = result.getData();
        ExcelUtil<CscpUserPost> util = new ExcelUtil<CscpUserPost>(CscpUserPost.class);
        return util.exportExcel(list, "cscpUserPost");
    }


    /**
    * DELETE  /ids : delete the cscpUserPost.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the cscpUserPost to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the cscpUserPost.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @DeleteMapping("/cscpUserPosts/delAll")
    public ResponseEntity<Void> deleteCscpUserPost(Long[] ids) {

        log.debug("REST request to delete ids");

        cscpUserPostService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

}
