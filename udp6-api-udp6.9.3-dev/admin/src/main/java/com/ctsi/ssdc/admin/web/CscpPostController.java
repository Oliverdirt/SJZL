package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.CscpPost;
import com.ctsi.ssdc.admin.domain.CscpPostExample;
import com.ctsi.ssdc.admin.domain.CscpUserPost;
import com.ctsi.ssdc.admin.domain.dto.CscpPostCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpPostDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpPostService;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.admin.service.CscpUserPostService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import static com.ctsi.ssdc.errorcode.enums.ErrorCodeConstants.POST_CODE_DUPLICATE;
import static com.ctsi.ssdc.errorcode.enums.ErrorCodeConstants.POST_NAME_DUPLICATE;
import static com.ctsi.ssdc.errorcode.util.ServiceExceptionUtil.exception;
/**
 * REST controller for managing CscpPost.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-post-controller"})
@RestController
@RequestMapping("/api")
public class CscpPostController {

    private final Logger log = LoggerFactory.getLogger(CscpPostController.class);

    private static final String ENTITY_NAME = "cscpPost";

    private final CscpPostService cscpPostService;

    @Autowired
    private CscpUserDetailService cscpUserDetailService;

    @Autowired
    private CscpUserPostService cscpUserPostService;

    public CscpPostController(CscpPostService cscpPostService) {
        this.cscpPostService = cscpPostService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpPosts : Create a new cscpPost.
     *
     * @param cscpPostDTO the cscpPost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpPost, or with status 400 (Bad Request) if the cscpPost has already an postId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpPost",name = "cscpPost",value = "the cscpPost to create")
    })
    @ApiOperation(value = "POST  /cscpPosts : create a new cscpPost.",notes = "POST  /cscpPosts : create a new cscpPost.",httpMethod = "POST")
    @PostMapping("/cscpPosts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpPost")
    public ResponseEntity<CscpPostDTO> createCscpPost(@RequestBody CscpPostDTO cscpPostDTO) throws URISyntaxException {

        //查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        cscpPostDTO.setTenantId(currentUser.getTenantId());

        cscpPostDTO.setCreateTime(ZonedDateTime.now());
        log.debug("REST request to save CscpPost : {}", cscpPostDTO);
        if (cscpPostDTO.getPostId() != null) {
            throw new BadRequestAlertException("A new cscpPost cannot already have an postId", ENTITY_NAME, "idexists");
        }
        CscpPostDTO result = cscpPostService.insert(cscpPostDTO);
        return ResponseEntity.created(new URI("/api/cscpPosts" + "/" +result.getPostId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getPostId().toString()))
            .body(result);

    }
	
    /**
     * PUT  /cscpPosts : Updates an existing cscpPost.
     *
     * @param cscpPostDTO the cscpPost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpPost,
     * or with status 400 (Bad Request) if the cscpPost is not valid,
     * or with status 500 (Internal Server Error) if the cscpPost couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpPost",name = "cscpPost",value = "the cscpPost to update")
    })
    @ApiOperation(value = "PUT  /cscpPosts : updates an existing cscpPost.",notes = "PUT  /cscpPosts : updates an existing cscpPost.",httpMethod = "PUT")
    @PutMapping("/cscpPosts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpPost")
    public ResponseEntity<CscpPostDTO> updateCscpPost(@RequestBody CscpPostDTO cscpPostDTO) throws URISyntaxException {
        log.debug("REST request to update CscpPost : {}", cscpPostDTO);
        // 查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
	    if (!cscpPostDTO.getTenantId().equals(currentUser.getTenantId())) {
            log.error("update error,not update tenantId");
            throw new BadRequestAlertException("cscpPosts cannot update the tenantID", cscpPostDTO.getTenantId().toString(), "tenantIdError");
	    }
	    if (cscpPostDTO.getPostId() == null){
	        return createCscpPost(cscpPostDTO);
        }
        CscpPostDTO result = cscpPostService.update(cscpPostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getPostId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpPosts/:postId : get the "postId" cscpPost.
     *
     * @param postId the id of the cscpPost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpPost, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "postId",value = "the postId of the cscpPost to retrieve")
    })
    @ApiOperation(value = "GET  /cscpPosts/postId : get the postId cscpPost.",notes = "GET  /cscpPosts/postId : get the postId cscpPost.",httpMethod = "GET")
    @GetMapping("/cscpPosts/{postId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpPost")
    public ResponseEntity<CscpPost> getCscpPost(@PathVariable Long postId) {
        log.debug("REST request to get CscpPost : {}", postId);
        CscpPost cscpPost = cscpPostService.findOne(postId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpPost));
    }

    /**
    * GET  /cscpPosts : get the cscpPosts firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpPosts in body
    */
    @ApiOperation(value = "GET  /cscpPosts : get the cscpPosts firstStringBaseColumn.",notes = "GET  /cscpPosts : get the cscpPosts firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpPosts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpPostsList")
    public PageResult<CscpPost> getCscpPostsList(CscpPostExample cscpPostExample, Pageable pageable) {
        log.debug("REST request to get CscpPostsList");
        return cscpPostService.findByExample(cscpPostExample,pageable);
    }

    @GetMapping("/cscpPost/getList")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpPostList")
    public PageResult<CscpPost> getCscpPostList(CscpPost cscpPost, Pageable pageable){
        log.debug("REST request to get PostList");
        return cscpPostService.getCscpPosts(cscpPost,pageable);
    }
	
    /**
     * DELETE  /cscpPosts/:postId : delete the "postId" cscpPost.
     *
     * @param postId the id of the cscpPost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "postId",value = "the postId of the cscpPost to delete")
    })
    @ApiOperation(value = "DELETE  /cscpPosts/postId : delete the postId cscpPost.",notes = "DELETE  /cscpPosts/postId : delete the postId cscpPost.",httpMethod = "DELETE")
    @DeleteMapping("/cscpPosts/{postId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpPost")
    public ResponseEntity<Map> deleteCscpPost(@PathVariable Long postId) {
        log.debug("REST request to delete CscpPost : {}", postId);
        Map resMap = new HashMap();

        List<CscpUserPost> cscpUserPosts = cscpUserPostService.findByPostId(postId);

        if(cscpUserPosts.size() != 0){
            resMap.put("code",1);
            resMap.put("msg","岗位下有用户不能删除！");
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "岗位下有用户不能删除！"))
                    .body(resMap);
        }
        cscpPostService.delete(postId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, postId.toString())).build();
    }

    /**
    * GET  /cscpPosts/:postId : get the "postId" cscpPost.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpPost, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "postId",value = "the postId of the cscpPost to retrieve")
    })
    @ApiOperation(value = "POST  /cscpPosts/export : export the cscpPost to excel",notes = "POST  /cscpPosts/export : export the cscpPost to excel",httpMethod = "POST")
    @PostMapping("/cscpPosts/export")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "export")
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpPost");
        PageResult<CscpPost> result = cscpPostService.findAll();
        List<CscpPost> list = result.getData();
        ExcelUtil<CscpPost> util = new ExcelUtil<CscpPost>(CscpPost.class);
        return util.exportExcel(list, "cscpPost");
    }

    @ApiOperation(value = "岗位编码重复校验")
    @PostMapping("/cscpPosts/checkCscpPostCode")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkCscpPostCode")
    @Log(message = "岗位编码重复校验")
    public ResponseEntity<Map> checkCscpPostCode(@RequestBody CscpPost cscpPost){
        log.debug("REST check CscpPost ； {}",cscpPost);
        Map<String,Object> retMap = new HashMap<>(500);
        int ret = cscpPostService.checkCscpPostCode(cscpPost);
        if(ret >= 1){
            retMap.put("msg", exception(POST_CODE_DUPLICATE).getCodeMessage());
        }
        retMap.put("code",exception(POST_CODE_DUPLICATE).getCode());
        return ResponseEntity.ok().body(retMap);
    }


    @ApiOperation(value = "岗位名字重复校验")
    @PostMapping("/cscpPosts/checkCscpPostName")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkCscpPostName")
    @Log(message = "岗位名字重复校验")
    public ResponseEntity<Map> checkCscpPostName(@RequestBody CscpPost cscpPost){
        log.debug("REST check CscpPost ； {}",cscpPost);
        Map<String,Object> retMap = new HashMap<>(500);
        int ret = cscpPostService.checkCscpPostName(cscpPost);
        if(ret >= 1){
            retMap.put("msg",exception(POST_NAME_DUPLICATE).getCodeMessage());
        }
        retMap.put("code",exception(POST_NAME_DUPLICATE).getCode());
        return ResponseEntity.ok().body(retMap);
    }

    @GetMapping("/cscpPostsByCriteria")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpPostsByCriteria")
    public PageResult<CscpPostDTO> getCscpPostsByCriteria(CscpPostCriteria cscpPostCriteria, Pageable page){
        log.debug("REST request to get CscpPostsByCriteria");

        //查询租户id
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        CscpUserDetailDTO currentUser = cscpUserDetailService.findByUserId(currentUserId);
        LongCriteria tenantIdCriteria = new LongCriteria();
        tenantIdCriteria.setEquals(currentUser.getTenantId());
        cscpPostCriteria.setTenantId(tenantIdCriteria);
        return cscpPostService.findByCscpPostsCriteria(cscpPostCriteria,page);

    }
    
}
