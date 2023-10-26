package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.FlowUserTaskInfo;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpUserDetailLike;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserUpdateDTO;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.Base64Util;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing CscpUserDetail.
 *
 * @author ctsi biyi generator
 *
 */

@RestController
@RequestMapping("/api/system")
public class CscpUserDetailController {

    private final Logger log = LoggerFactory.getLogger(CscpUserDetailController.class);

    private static final String ENTITY_NAME = "cscpUserDetail";

    private final CscpUserDetailService cscpUserDetailService;

    public CscpUserDetailController(CscpUserDetailService cscpUserDetailService) {
        this.cscpUserDetailService = cscpUserDetailService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpUserDetails : Create a new cscpUserDetail.
     *
     * @param cscpUserDetailDTO the cscpUserDetailDTO to create
     * @return the ResponseEntity with status 201 (Created)
     * and with body the new cscpUserDetailDTO, or with status 400
     * (Bad Request) if the cscpUserDetail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpUserDetails")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpUserDetail")
    public ResponseEntity<Long> createCscpUserDetail(
            @RequestBody CscpUserDetailDTO cscpUserDetailDTO) throws URISyntaxException {
        log.debug("REST request to save CscpUserDetail : {}", cscpUserDetailDTO);
        if (cscpUserDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpUserDetail cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        CscpUserDetailDTO result = cscpUserDetailService.insert(cscpUserDetailDTO);
        return ResponseEntity.created(new URI("/api/cscpUserDetails/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result.getId());
    }

    /**
     * PUT  /cscpUserDetails : Updates an existing cscpUserDetail.
     *
     * @param cscpUserUpdateDTO the cscpUserDetailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserDetailDTO,
     * or with status 400 (Bad Request) if the cscpUserDetailDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserDetailDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUserDetails/{type}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpUserDetail")
    public ResponseEntity<Long> updateCscpUserDetail(@PathVariable int type,
            @RequestBody CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception {
        log.debug("REST request to update CscpUserDetail : {}", cscpUserUpdateDTO);

        //水平越权问题2021/09/16
        //type代表1为个人中心修改，需要做水平越权处理，0代表用户管理用户编辑修改
        if(type == 1){
            if(!SecurityHxUtils.getCurrentUserId().equals(cscpUserUpdateDTO.getCscpUserDetailDTO().getUserId()) ){
                throw new BadRequestAlertException("该用户无权限修改",ENTITY_NAME,"noAuth");
            }
        }

        //验证base64图片，防止XSS攻击
        if(StringUtils.isNotBlank(cscpUserUpdateDTO.getCscpUserDetailDTO().getImgPath())) {
            if(!Base64Util.validateBASE64(cscpUserUpdateDTO.getCscpUserDetailDTO().getImgPath())) {
                throw new BadRequestAlertException("The imgPath of cscpUserDetail pattern error",
                        ENTITY_NAME, "imgPath");
            }
        }
        if (cscpUserUpdateDTO.getCscpUserDetailDTO().getId() == null) {
            return createCscpUserDetail(cscpUserUpdateDTO.getCscpUserDetailDTO());
        }
        CscpUserDetailDTO result = cscpUserDetailService.updateUserDTO(cscpUserUpdateDTO);
//        CscpUserDetailDTO result = cscpUserDetailService.userPwdUpdate(cscpUserUpdateDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(
                        ENTITY_NAME, cscpUserUpdateDTO.getCscpUserDetailDTO().getId().toString()))
                .body(result.getId());
    }


    /**
     * PUT  /cscpUserDetails : Updates an existing cscpUserDetail.
     *
     * @param cscpUserUpdateDTO the cscpUserDetailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserDetailDTO,
     * or with status 400 (Bad Request) if the cscpUserDetailDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserDetailDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/updateCscpUserPwd/{type}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpUserPwd")
    public ResponseEntity<Long> updateCscpUserPwd(@PathVariable int type,
                                                                  @RequestBody CscpUserUpdateDTO cscpUserUpdateDTO) throws Exception {
        log.debug("REST request to update CscpUserDetail : {}", cscpUserUpdateDTO);

        //水平越权问题2021/09/16
        //type代表1为个人中心修改，需要做水平越权处理，0代表用户管理用户编辑修改
        if(type == 1){
            if(!SecurityHxUtils.getCurrentUserId().equals(cscpUserUpdateDTO.getCscpUserDetailDTO().getUserId()) ){
                throw new BadRequestAlertException("该用户无权限修改",ENTITY_NAME,"noAuth");
            }
        }

        //验证base64图片，防止XSS攻击
        if(StringUtils.isNotBlank(cscpUserUpdateDTO.getCscpUserDetailDTO().getImgPath())) {
            if(!Base64Util.validateBASE64(cscpUserUpdateDTO.getCscpUserDetailDTO().getImgPath())) {
                throw new BadRequestAlertException("The imgPath of cscpUserDetail pattern error",
                        ENTITY_NAME, "imgPath");
            }
        }
        if (cscpUserUpdateDTO.getCscpUserDetailDTO().getId() == null) {
            return createCscpUserDetail(cscpUserUpdateDTO.getCscpUserDetailDTO());
        }
//        CscpUserDetailDTO result = cscpUserDetailService.updateUserDTO(cscpUserUpdateDTO);
        CscpUserDetailDTO result = cscpUserDetailService.userPwdUpdate(cscpUserUpdateDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(
                        ENTITY_NAME, cscpUserUpdateDTO.getCscpUserDetailDTO().getId().toString()))
                .body(result.getId());
    }




    /**
     * GET  /cscpUserDetails : get the cscpUserDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserDetails in body
     */
    @GetMapping("/cscpUserDetails")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserDetails")
    public PageResult<CscpUserDetailDTO> getCscpUserDetails(CscpUserDetailDTO cscpUserDetailDTO, Pageable page) {
        log.debug("REST request to get CscpUserDetails");
        return cscpUserDetailService.findByCscpUserDetailDTO(cscpUserDetailDTO, page);
    }
    
    /**
     * GET  /cscpUserDetails : get the cscpUserDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUserDetails in body
     */
    @GetMapping("/cscpUserDetailsByCriteria")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserDetailsByCriteria")
    public PageResult<CscpUserDetailDTO> getCscpUserDetailsByCriteria(
            CscpUserDetailCriteria cscpUserDetailCriteria, Pageable page) {
        log.debug("REST request to get CscpUserDetailsByCriteria");
        return cscpUserDetailService.findByCscpUserDetailCriteria(cscpUserDetailCriteria, page);
    }

    /**
     * GET  /cscpUserDetails/:id : get the "id" cscpUserDetail.
     *
     * @param id the id of the cscpUserDetailDTO to retrieve
     * @return the ResponseEntity with status 200
     * (OK) and with body the cscpUserDetailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpUserDetails/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserDetail")
    public ResponseEntity<CscpUserDetailDTO> getCscpUserDetail(@PathVariable Long id) {
        log.debug("REST request to get CscpUserDetail : {}", id);
        CscpUserDetailDTO cscpUserDetailDTO = cscpUserDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserDetailDTO));
    }
    
    /**
     * GET  /cscpUserDetails/:id : get the "id" cscpUserDetail.
     *
     * @return the ResponseEntity with status 200 (OK)
     * and with body the cscpUserDetailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpCurrentUserDetails")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUserDetails")
    public ResponseEntity<CscpUserDetailDTO> getCscpUserDetails() {
        Long uid = SecurityHxUtils.getCurrentUserId();
        log.debug("REST request to get CscpUserDetail by userId : {}", uid);
        CscpUserDetailDTO cscpUserDetailDTO = cscpUserDetailService.findByUserId(uid);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserDetailDTO));
    }

    /**
     * DELETE  /cscpUserDetails/:id : delete the "id" cscpUserDetail.
     *
     * @param id the id of the cscpUserDetailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpUserDetails/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpUserDetail")
    public ResponseEntity<Void> deleteCscpUserDetail(@PathVariable  Long id) {
        log.debug("REST request to delete CscpUserDetail : {}", id);
        cscpUserDetailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/cscpUserDetailsOr")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getUserDetailOr")
    public PageResult<CscpUserDetailDTO> getUserDetailOr(CscpUserDetailLike cscpUserDetailLike, Pageable page) {
        log.debug("REST request to get CscpUserDetails");
        return cscpUserDetailService.findByCscpUserDetailDtoOr(cscpUserDetailLike, page);
    }

    @GetMapping("/cscpUserDetailsOrTwo")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "cscpUserDetailsOrTwo")
    public List<CscpUserDetailDTO> cscpUserDetailsOrTwo() {
        log.debug("REST request to get CscpUserDetails");

        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        Long tenantId = cscpUserDetailService.findByUserId(currentUserId).getTenantId();

        List<CscpUserDetailDTO> cscpUserDetailDtos = cscpUserDetailService.selectByTenantId(tenantId);

        return cscpUserDetailDtos;
    }



    
    @GetMapping("/cscpUserDetailsByUserId")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getByUserId")
    public CscpUserDetailDTO getByUserId(Long userId) {
        log.debug("REST request to get CscpUserDetails by user id");
        return cscpUserDetailService.findByUserId(userId);
    }

    @GetMapping("/flowUserTaskInfo")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "flowUserTaskInfo")
    public FlowUserTaskInfo getFlowUserTaskInfo() {
        log.debug("REST request to get FlowUserTaskInfo");
        FlowUserTaskInfo userTaskInfo = cscpUserDetailService.findFlowUserTaskInfo();
        return userTaskInfo;
    }

}
