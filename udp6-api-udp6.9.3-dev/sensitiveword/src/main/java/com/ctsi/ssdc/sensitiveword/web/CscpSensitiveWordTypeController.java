package com.ctsi.ssdc.sensitiveword.web;

import cn.hutool.core.text.StrFormatter;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.sensitiveword.constants.SensitiveWordConstant;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordType;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordTypeExample;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordTypeService;
import com.ctsi.ssdc.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing CscpSensitiveWordType.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(tags="敏感词分类管理",value="敏感词分类")
@RestController
@RequestMapping("/api/system")
public class CscpSensitiveWordTypeController {

    private final Logger log = LoggerFactory.getLogger(CscpSensitiveWordTypeController.class);

    private static final String ENTITY_NAME = "cscpSensitiveWordType";

    private final CscpSensitiveWordTypeService cscpSensitiveWordTypeService;

    public CscpSensitiveWordTypeController(CscpSensitiveWordTypeService cscpSensitiveWordTypeService) {
        this.cscpSensitiveWordTypeService = cscpSensitiveWordTypeService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpSensitiveWordTypes : Create a new cscpSensitiveWordType.
     *
     * @param cscpSensitiveWordType the cscpSensitiveWordType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpSensitiveWordType, or with status 400 (Bad Request) if the cscpSensitiveWordType has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value="敏感词分类-新增", notes="新增")
    @PostMapping("/cscpSensitiveWordTypes")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "createCscpSensitiveWordType")
    public AjaxResult createCscpSensitiveWordType(@RequestBody @Valid CscpSensitiveWordType cscpSensitiveWordType) throws URISyntaxException {
        log.debug("REST request to save CscpSensitiveWordType : {}", cscpSensitiveWordType);
        if (cscpSensitiveWordType.getId() != null) {
            throw new BadRequestAlertException("A new cscpSensitiveWordType cannot already have an id", ENTITY_NAME, "idexists");
        }
        cscpSensitiveWordType.setUpdateTime(new Date());
        // 判断判断分类名是否重复
        CscpSensitiveWordTypeExample cscpSensitiveWordTypeExample = new CscpSensitiveWordTypeExample();
        cscpSensitiveWordTypeExample.createCriteria().andSenTypeNameEqualTo(cscpSensitiveWordType.getSenTypeName());
        PageResult<CscpSensitiveWordType> sensitiveWordTypeServiceBySenTypeName = cscpSensitiveWordTypeService.findByExample(cscpSensitiveWordTypeExample);
        if(sensitiveWordTypeServiceBySenTypeName.getData().size()>0){
            // 重复
            return AjaxResult.error(StrFormatter.format("新增失败，分类名{}已存在",cscpSensitiveWordType.getSenTypeName()));
        }else{
            CscpSensitiveWordType result = cscpSensitiveWordTypeService.insert(cscpSensitiveWordType);
            return AjaxResult.success("新增成功");
        }
    }
	
    /**
     * PUT  /cscpSensitiveWordTypes : Updates an existing cscpSensitiveWordType.
     *
     * @param cscpSensitiveWordType the cscpSensitiveWordType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpSensitiveWordType,
     * or with status 400 (Bad Request) if the cscpSensitiveWordType is not valid,
     * or with status 500 (Internal Server Error) if the cscpSensitiveWordType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value="敏感词分类-修改", notes="修改")
    @PutMapping("/cscpSensitiveWordTypes")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "updateCscpSensitiveWordType")
    public AjaxResult updateCscpSensitiveWordType(@RequestBody @Valid CscpSensitiveWordType cscpSensitiveWordType) throws URISyntaxException {
        log.debug("REST request to update CscpSensitiveWordType : {}", cscpSensitiveWordType);
	    if (cscpSensitiveWordType.getId() == null) {
	    	return createCscpSensitiveWordType(cscpSensitiveWordType);
	    }
	    //判断分类名是否重复
        CscpSensitiveWordTypeExample cscpSensitiveWordTypeExample =new CscpSensitiveWordTypeExample();
        cscpSensitiveWordTypeExample.createCriteria().andSenTypeNameEqualTo(cscpSensitiveWordType.getSenTypeName());
        List<CscpSensitiveWordType> sensitiveWordTypeList = cscpSensitiveWordTypeService.findByExample(cscpSensitiveWordTypeExample).getData();


        if(sensitiveWordTypeList.size() >0 && !sensitiveWordTypeList.get(0).getId().equals(cscpSensitiveWordType.getId())){
        // 非唯一
            return AjaxResult.error(StrFormatter.format("更新失败,分类名{}已存在",cscpSensitiveWordType.getSenTypeName()));
        }else{
            cscpSensitiveWordType.setUpdateTime(new Date());
            CscpSensitiveWordType result = cscpSensitiveWordTypeService.update(cscpSensitiveWordType);
            return AjaxResult.success("更新成功。",result);
        }
    }
    
    /**
     * GET  /cscpSensitiveWordTypes : get the cscpSensitiveWordTypes with page.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpSensitiveWordTypes in body
     */
    @ApiOperation(value="敏感词分类-分页查询", notes="分页查询")
    @GetMapping("/cscpSensitiveWordTypesByCriteria")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getCscpSensitiveWordTypesByCriteria")
    public PageResult<CscpSensitiveWordType> getCscpSensitiveWordTypesByCriteria(CscpSensitiveWordTypeExample cscpSensitiveWordTypeExample, Pageable page) {
        log.debug("REST request to get CscpSensitiveWordTypesByCriteria");
        return cscpSensitiveWordTypeService.findByExample(cscpSensitiveWordTypeExample, page);
    }
    
    /**
     * GET  /cscpSensitiveWordTypes : get the cscpSensitiveWordTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpSensitiveWordTypes in body
     */
    @ApiOperation(value="敏感词分类-列表查询", notes="列表查询")
    @GetMapping("/cscpSensitiveWordTypesList")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getCscpSensitiveWordTypesList")
    public PageResult<CscpSensitiveWordType> getCscpSensitiveWordTypesList(CscpSensitiveWordTypeExample cscpSensitiveWordTypeExample) {
        log.debug("REST request to get CscpSensitiveWordTypesList");
        return cscpSensitiveWordTypeService.findByExample(cscpSensitiveWordTypeExample);
    }

    @ApiOperation(value="敏感词分类-非空列表查询", notes="非空列表查询")
    @GetMapping("/cscpSensitiveWordTypesListNonempty")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getSensitiveWordTypesListNonempty")
    public AjaxResult getSensitiveWordTypesListNonempty() {
        log.debug("REST request to get cscpSensitiveWordTypesListNonempty");
        List<CscpSensitiveWordType> cscpSensitiveWordTypeList= cscpSensitiveWordTypeService.getSensitiveWordTypesListNonempty();
        return  new AjaxResult(200,"查询成功",cscpSensitiveWordTypeList);
    }

    /**
     * GET  /cscpSensitiveWordTypes/:id : get the "id" cscpSensitiveWordType.
     *
     * @param id the id of the cscpSensitiveWordType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpSensitiveWordType, or with status 404 (Not Found)
     */
    @ApiOperation(value="敏感词分类-单体查询", notes="单体查询")
    @GetMapping("/cscpSensitiveWordTypes/{id}")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "getCscpSensitiveWordType")
    public ResponseEntity<CscpSensitiveWordType> getCscpSensitiveWordType(@PathVariable Long id) {
        log.debug("REST request to get CscpSensitiveWordType : {}", id);
        CscpSensitiveWordType cscpSensitiveWordType = cscpSensitiveWordTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpSensitiveWordType));
    }
	
    /**
     * DELETE  /cscpSensitiveWordTypes/:id : delete the "id" cscpSensitiveWordType.
     *
     * @param id the id of the cscpSensitiveWordType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value="敏感词分类-删除", notes="删除")
    @DeleteMapping("/cscpSensitiveWordTypes/{id}")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "deleteCscpSensitiveWordType")
    public AjaxResult deleteCscpSensitiveWordType(@PathVariable Long id) {
        log.debug("REST request to delete CscpSensitiveWordType : {}", id);
        return cscpSensitiveWordTypeService.deleteBysenTypeId(id);
    }

    @ApiOperation(value="敏感词分类-批量删除", notes="批量删除")
    @DeleteMapping("/cscpSensitiveWordTypes/delAll")
    @ComponentCallAnno(component = SensitiveWordConstant.SENSITIVEWORD,method = "deleteCscpSensitiveWordType")
    public AjaxResult deleteCscpSensitiveWordType(Long[] ids) {
        log.debug("REST request to delete ids");

        return cscpSensitiveWordTypeService.deleteByIds(ids);
    }
    
}
