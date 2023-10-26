package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpDept;
import com.ctsi.ssdc.admin.domain.CscpDeptExample;
import com.ctsi.ssdc.admin.domain.CscpUserDetailLike;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpDeptService;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.TreeSelect;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * REST controller for managing CscpDept.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-dept-controller"})
@RestController
@RequestMapping("/api")
public class CscpDeptController {

    private final Logger log = LoggerFactory.getLogger(CscpDeptController.class);

    private static final String ENTITY_NAME = "cscpDept";

    private final CscpDeptService cscpDeptService;

    @Autowired
    CscpUserDetailService cscpUserDetailService;


    public CscpDeptController(CscpDeptService cscpDeptService) {
        this.cscpDeptService = cscpDeptService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpDepts : Create a new cscpDept.
     *
     * @param cscpDept the cscpDept to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpDept, or with status 400 (Bad Request) if the cscpDept has already an deptId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpDept",name = "cscpDept",value = "the cscpDept to create")
    })
    @ApiOperation(value = "POST  /cscpDepts : create a new cscpDept.",notes = "POST  /cscpDepts : create a new cscpDept.",httpMethod = "POST")
    @PostMapping("/cscpDepts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpDept")
    public ResponseEntity<Map> createCscpDept(@RequestBody CscpDept cscpDept) throws URISyntaxException {
        log.debug("REST request to save CscpDept : {}", cscpDept);
        Map resMap = new HashMap();
        CscpDeptExample cscpDeptExample = new CscpDeptExample();
        cscpDeptExample.setDeptName((StringCriteria)new StringCriteria().setEquals(cscpDept.getDeptName()));
        PageResult<CscpDept> depts = cscpDeptService.findByExample(cscpDeptExample);
        if(depts.getData().size()>0){
            resMap.put("code",1);
            resMap.put("msg","部门名称已存在，不可重复！");
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "部门名称已存在，不可重复！"))
                    .body(resMap);
        }
        CscpDept result = cscpDeptService.insert(cscpDept);
        resMap.put("code",0);
        resMap.put("msg","新增成功！");
        resMap.put("data",result);
        return ResponseEntity.created(new URI("/api/cscpDepts" + "/" +result.getDeptId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDeptId().toString()))
            .body(resMap);
    }
	
    /**
     * PUT  /cscpDepts : Updates an existing cscpDept.
     *
     * @param cscpDept the cscpDept to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpDept,
     * or with status 400 (Bad Request) if the cscpDept is not valid,
     * or with status 500 (Internal Server Error) if the cscpDept couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpDept",name = "cscpDept",value = "the cscpDept to update")
    })
    @ApiOperation(value = "PUT  /cscpDepts : updates an existing cscpDept.",notes = "PUT  /cscpDepts : updates an existing cscpDept.",httpMethod = "PUT")
    @PutMapping("/cscpDepts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpDept")
    public ResponseEntity<Map> updateCscpDept(@RequestBody CscpDept cscpDept)  {
        log.debug("REST request to update CscpDept : {}", cscpDept);
        Map resMap = new HashMap();
        CscpDept oldDept = cscpDeptService.findOne(cscpDept.getDeptId());
        CscpDeptExample cscpDeptExample = new CscpDeptExample();
        cscpDeptExample.setDeptName((StringCriteria)new StringCriteria().setEquals(cscpDept.getDeptName()));
        PageResult<CscpDept> depts = cscpDeptService.findByExample(cscpDeptExample);
        if(!oldDept.getDeptName().equals(cscpDept.getDeptName()) && depts.getData().size()>0){
            resMap.put("code",1);
            resMap.put("msg","部门名称已存在，不可重复！");
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "部门名称已存在，不可重复！"))
                    .body(resMap);
        }
        CscpDept result = cscpDeptService.update(cscpDept);
        resMap.put("code",0);
        resMap.put("msg","修改成功！");
        resMap.put("data",result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getDeptId().toString()))
            .body(resMap);
    }

    /**
     * GET  /cscpDepts/:deptId : get the "deptId" cscpDept.
     * 根据部门编号获取详细信息
     * @param deptId the id of the cscpDept to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpDept, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "deptId",value = "the deptId of the cscpDept to retrieve")
    })
    @ApiOperation(value = "GET  /cscpDepts/deptId : get the deptId cscpDept.",notes = "GET  /cscpDepts/deptId : get the deptId cscpDept.",httpMethod = "GET")
    @GetMapping("/cscpDepts/{deptId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpDept")
    public ResponseEntity<CscpDept> getCscpDept(@PathVariable Long deptId) {
        log.debug("REST request to get CscpDept : {}", deptId);
        CscpDept cscpDept = cscpDeptService.findOne(deptId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpDept));
    }

    /**
    * GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.
    * 获取部门数据列表
    * @return the ResponseEntity with status 200 (OK) and the list of cscpDepts in body
    */
    @ApiOperation(value = "GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.",notes = "GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpDepts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpDeptsList")
    public PageResult<CscpDept> getCscpDeptsList(CscpDeptExample cscpDeptExample) {
        log.debug("REST request to get CscpDeptsList");
        return cscpDeptService.findByExample(cscpDeptExample);
//        return cscpDeptService.findByExample(cscpDeptExample,pageable);
    }

    /**
     * GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.
     * 查询部门列表（排除节点）
     * *@return the ResponseEntity with status 200 (OK) and the list of cscpDepts in body
     */
    @ApiOperation(value = "GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.",notes = "GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpDepts/exclude/{deptId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "excludeChild")
    public PageResult<CscpDept> excludeChild(@PathVariable Long deptId) {
        PageResult<CscpDept> pageResult = cscpDeptService.findAll();
        List<CscpDept> cscpDeptList = pageResult.getData();
        Iterator<CscpDept> it = cscpDeptList.iterator();
        while (it.hasNext())
        {
            CscpDept d = (CscpDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        pageResult.setData(cscpDeptList);
        return  pageResult;
    }


    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/cscpDepts/treeselect")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "treeselect")
    public ResponseEntity<List<TreeSelect>> treeselect(CscpDeptExample cscpDeptExample)
    {
        PageResult<CscpDept> pageResult = cscpDeptService.findByExample(cscpDeptExample);
        List<CscpDept> cscpDeptList = pageResult.getData();
        List<TreeSelect> treeSelects = cscpDeptService.buildDeptTreeSelect(cscpDeptList);
        return ResponseEntity.ok().body(treeSelects);
    }
	
    /**
     * DELETE  /cscpDepts/:deptId : delete the "deptId" cscpDept.
     * 删除部门信息
     * @param deptId the id of the cscpDept to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "deptId",value = "the deptId of the cscpDept to delete")
    })
    @ApiOperation(value = "DELETE  /cscpDepts/deptId : delete the deptId cscpDept.",notes = "DELETE  /cscpDepts/deptId : delete the deptId cscpDept.",httpMethod = "DELETE")
    @DeleteMapping("/cscpDepts/{deptId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpDept")
    public ResponseEntity<Map> deleteCscpDept(@PathVariable Long deptId) {
        log.debug("REST request to delete CscpDept : {}", deptId);
        Map resMap = new HashMap();
        CscpDeptExample cscpDeptExample = new CscpDeptExample();
        cscpDeptExample.setParentId((LongCriteria) new LongCriteria().setEquals(deptId));
        PageResult<CscpDept> depts = cscpDeptService.findByExample(cscpDeptExample);
        if(depts.getData().size()>0){
            resMap.put("code",1);
            resMap.put("msg","部门下有子部门不能删除！");
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "部门下有子部门不能删除"))
                    .body(resMap);
        }
        CscpUserDetailLike detailLike = new CscpUserDetailLike();
        detailLike.setDeptIdEquals(deptId.toString());
        Pageable page = PageRequest.of(1, 20);
        PageResult<CscpUserDetailDTO> result = cscpUserDetailService.findByCscpUserDetailDtoOr(detailLike, page);
        if(result.getData().size()>0){
            resMap.put("code",1);
            resMap.put("msg","部门下有用户不能删除！");
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "部门下有用户不能删除"))
                    .body(resMap);
        }
//        CscpDept cscpDeptself = cscpDeptService.findOne(deptId);
//        if("0".equals(cscpDeptself.getStatus())){
//            resMap.put("msg","部门状态正常，请停用后再删除！");
//            return ResponseEntity.ok()
//                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "部门状态正常，请停用后再删除！"))
//                    .body(resMap);
//        }
        cscpDeptService.delete(deptId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, deptId.toString())).build();

    }


    /**
    * Post  /cscpDepts/:deptId : get the "deptId" cscpDept.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpDept, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "deptId",value = "the deptId of the cscpDept to retrieve")
    })
    @ApiOperation(value = "POST  /cscpDepts/export : export the cscpDept to excel",notes = "POST  /cscpDepts/export : export the cscpDept to excel",httpMethod = "POST")
    @PostMapping("/cscpDepts/export")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "export")
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpDept");
        PageResult<CscpDept> result = cscpDeptService.findAll();
        List<CscpDept> list = result.getData();
        ExcelUtil<CscpDept> util = new ExcelUtil<CscpDept>(CscpDept.class);
        return util.exportExcel(list, "cscpDept");
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "roleDeptTreeselect")
    @ApiOperation("加载对应角色部门列表树")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<CscpDept> depts = cscpDeptService.selectDeptList(new CscpDept());
        AjaxResult ajax = AjaxResult.success();
        List<Long> roleIdList = cscpDeptService.selectDeptListByRoleId(roleId);
        ajax.put("checkedKeys",StringUtils.join(roleIdList,","));
        ajax.put("depts", cscpDeptService.buildDeptTreeSelect(depts));
        return ajax;
    }
    
}
