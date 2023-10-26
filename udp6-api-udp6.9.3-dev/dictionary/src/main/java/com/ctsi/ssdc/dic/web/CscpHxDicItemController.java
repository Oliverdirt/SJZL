package com.ctsi.ssdc.dic.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.dic.consts.CscpDicAttrType;
import com.ctsi.ssdc.dic.domain.CscpHxDic;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.dic.domain.CscpHxDicItemExample;
import com.ctsi.ssdc.dic.model.TreeDicItemSelect;
import com.ctsi.ssdc.dic.service.CscpHxDicItemService;
import com.ctsi.ssdc.dic.service.CscpHxDicService;
import com.ctsi.ssdc.dic.validator.ValidatorDicEnums;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * REST controller for managing CscpHxDicItem.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-dic-item-controller"})
@RestController
@RequestMapping("/api/dic")
public class CscpHxDicItemController {

    private final Logger log = LoggerFactory.getLogger(CscpHxDicItemController.class);

    private static final String ENTITY_NAME = "cscpHxDicItem";

    private final CscpHxDicItemService cscpHxDicItemService;
    private final CscpHxDicService  cscpHxDicService;

    public CscpHxDicItemController(CscpHxDicItemService cscpHxDicItemService,CscpHxDicService  cscpHxDicService) {
        this.cscpHxDicItemService = cscpHxDicItemService;
        this.cscpHxDicService = cscpHxDicService;
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpHxDicItems : Create a new cscpHxDicItem.
     *
     * @param cscpHxDicItem the cscpHxDicItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxDicItem, or with status 400 (Bad Request) if the cscpHxDicItem has already an itemId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDicItem",name = "cscpHxDicItem",value = "the cscpHxDicItem to create")
    })
    @ApiOperation(value = "POST  /cscpHxDicItems : create a new cscpHxDicItem.",notes = "POST  /cscpHxDicItems : create a new cscpHxDicItem.",httpMethod = "POST")
    @PostMapping("/cscpHxDicItems")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "createCscpHxDicItem")
    @Log
    public ResponseEntity<CscpHxDicItem> createCscpHxDicItem(@RequestBody CscpHxDicItem cscpHxDicItem) throws URISyntaxException {
        log.debug("REST request to save CscpHxDicItem : {}", cscpHxDicItem);
        CscpHxDicItem result = cscpHxDicItemService.insert(cscpHxDicItem);
        return ResponseEntity.created(new URI("/api/cscpHxDicItems" + "/" +result.getItemId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getItemId().toString()))
            .body(result);
    }


    /**
     * PUT  /cscpHxDicItems : Updates an existing cscpHxDicItem.
     *
     * @param cscpHxDicItem the cscpHxDicItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxDicItem,
     * or with status 400 (Bad Request) if the cscpHxDicItem is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxDicItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDicItem",name = "cscpHxDicItem",value = "the cscpHxDicItem to update")
    })
    @ApiOperation(value = "PUT  /cscpHxDicItems : updates an existing cscpHxDicItem.",notes = "PUT  /cscpHxDicItems : updates an existing cscpHxDicItem.",httpMethod = "PUT")
    @PutMapping("/cscpHxDicItems")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "updateCscpHxDicItem")
    @Log
    public ResponseEntity<CscpHxDicItem> updateCscpHxDicItem(@RequestBody CscpHxDicItem cscpHxDicItem)  {
        log.debug("REST request to update CscpHxDicItem : {}", cscpHxDicItem);
        CscpHxDicItem result = cscpHxDicItemService.update(cscpHxDicItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getItemId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpHxDicItems/:itemId : get the "itemId" cscpHxDicItem.
     *
     * @param itemId the id of the cscpHxDicItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDicItem, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "itemId",value = "the itemId of the cscpHxDicItem to retrieve")
    })
    @ApiOperation(value = "GET  /cscpHxDicItems/itemId : get the itemId cscpHxDicItem.",notes = "GET  /cscpHxDicItems/itemId : get the itemId cscpHxDicItem.",httpMethod = "GET")
    @GetMapping("/cscpHxDicItems/{itemId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicItem")
    @Log
    public ResponseEntity<CscpHxDicItem> getCscpHxDicItem(@PathVariable Long itemId) {
        log.debug("REST request to get CscpHxDicItem : {}", itemId);
        CscpHxDicItem cscpHxDicItem = cscpHxDicItemService.findOne(itemId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxDicItem));
    }

    /**
    * GET  /cscpHxDicItems : get the cscpHxDicItems firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxDicItems in body
    */
    @ApiOperation(value = "GET  /cscpHxDicItems : get the cscpHxDicItems firstStringBaseColumn.",notes = "GET  /cscpHxDicItems : get the cscpHxDicItems firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxDicItems")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicItemsList")
    @Log
    public PageResult<CscpHxDicItem> getCscpHxDicItemsList(CscpHxDicItemExample cscpHxDicItemExample, Pageable pageable) {
        log.debug("REST request to get CscpHxDicItemsList");
        return cscpHxDicItemService.findByExample(cscpHxDicItemExample,pageable);
    }

    @ApiOperation(value = "GET  /cscpHxDicItems : get the cscpHxDicItems firstStringBaseColumn.",notes = "GET  /cscpHxDicItems : get the cscpHxDicItems firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxDicItems/list")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicItemsList")
    @Log
    public PageResult<CscpHxDicItem> getCscpHxDicItemsList(CscpHxDicItemExample cscpHxDicItemExample) {
        log.debug("REST request to get CscpHxDicItemsList");
        return cscpHxDicItemService.findByExample(cscpHxDicItemExample);
    }

    /**
     * DELETE  /cscpHxDicItems/:itemId : delete the "itemId" cscpHxDicItem.
     *
     * @param itemId the id of the cscpHxDicItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "itemId",value = "the itemId of the cscpHxDicItem to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxDicItems/itemId : delete the itemId cscpHxDicItem.",notes = "DELETE  /cscpHxDicItems/itemId : delete the itemId cscpHxDicItem.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDicItems/{itemId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteCscpHxDicItem")
    @Log
    public ResponseEntity<Void> deleteCscpHxDicItem(@PathVariable Long itemId) {
        log.debug("REST request to delete CscpHxDicItem : {}", itemId);
        cscpHxDicItemService.delete(itemId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, itemId.toString())).build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "itemId",value = "the itemId of the cscpHxDicItem to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxDicItems/itemId : delete the itemId cscpHxDicItem.",notes = "DELETE  /cscpHxDicItems/itemId : delete the itemId cscpHxDicItem.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDicItems/tree/{itemId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteCscpHxDicItem")
    @Log
    public AjaxResult deleteCscpHxTreeDicItem(@PathVariable Long itemId) {
        log.debug("REST request to delete CscpHxDicItem : {}", itemId);
        boolean delFlag = cscpHxDicItemService.checkIsTreeItemDel(itemId);
        if(delFlag){
            return AjaxResult.error("字典项存在子项，不允许删除");
        }
        cscpHxDicItemService.delete(itemId);
        return AjaxResult.success("删除成功");
    }

    /**
    * GET  /cscpHxDicItems/:itemId : get the "itemId" cscpHxDicItem.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDicItem, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "itemId",value = "the itemId of the cscpHxDicItem to retrieve")
    })
    @ApiOperation(value = "POST  /cscpHxDicItems/export : export the cscpHxDicItem to excel",notes = "POST  /cscpHxDicItems/export : export the cscpHxDicItem to excel",httpMethod = "POST")
    @PostMapping("/cscpHxDicItems/export")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "exportHxDicItems")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxDicItem");
        PageResult<CscpHxDicItem> result = cscpHxDicItemService.findAll();
        List<CscpHxDicItem> list = result.getData();
        ExcelUtil<CscpHxDicItem> util = new ExcelUtil<CscpHxDicItem>(CscpHxDicItem.class);
        return util.exportExcel(list, "cscpHxDicItem");
    }


    /**
     * 获取下拉树列表
     */
    @GetMapping("/cscpHxDicItems/treeselect/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "treeselect")
    @Log
    public ResponseEntity<List<TreeDicItemSelect>> treeselect(@PathVariable Long dicId )
    {
        List<CscpHxDicItem> cscpHxDicItemList = cscpHxDicItemService.selectByDicId(dicId);
        List<TreeDicItemSelect> treeDicItemSelects = cscpHxDicItemService.buildDicItemTreeSelect(cscpHxDicItemList);
        return ResponseEntity.ok().body(treeDicItemSelects);
    }

    /**
     * 获取字典项列表根据dicId
     * @param dicId
     * @return
     */
    @GetMapping("/cscpHxDicItems/query/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getItemByDicId")
    @Log
    public ResponseEntity<List<CscpHxDicItem>> getItemByDicId(@PathVariable Long dicId){
        List<CscpHxDicItem> cscpHxDicItemList = cscpHxDicItemService.selectByDicId(dicId);
        return ResponseEntity.ok().body(cscpHxDicItemList);
    }
    /**
     * 批量删除
     * @param itemIds
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "itemIds", value = "the itemId of the tenant to delete")
    })
    @ApiOperation(value = "DELETE  /jobs : delete the CscpHxDicItems.", notes = "DELETE  /itemIds : delete the itemIds .", httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDicItems/delAll")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteAll")
    @Log
    public ResponseEntity<Void> deleteAll(Long[] itemIds) {
        log.debug("REST request to delete Job : {}", itemIds);
        cscpHxDicItemService.deleteByIds(itemIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(itemIds))).build();
    }
    @ApiOperation(value = "字典编码重复校验")
    @PostMapping("/cscpHxDicItems/checkCscpHxItemCode")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "checkCscpHxItemCode")
    @Log
    public ResponseEntity<Map> checkCscpHxItemCode(@RequestBody CscpHxDicItem item) {
        log.debug("REST check CscpHxDic : {}", item);
        Map<String,Object> retMap = new HashMap(500);
        int ret = cscpHxDicItemService.checkCscpHxItemCode(item);
        if(ret >= 1){
            retMap.put("msg", ValidatorDicEnums.DIC_ITEM_CODE_REP.getMsg());
        }
        retMap.put("code",ret);
        return ResponseEntity.ok().body(retMap);
    }

    @ApiOperation(value = "根据dicCode查询字典项")
    @GetMapping("/cscpHxDicItems/getItems")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "checkCscpHxItemCode")
    @Log
    public ResponseEntity<Map> checkCscpHxItemCode(CscpHxDicExample cscpHxDicExample) {
        log.debug("REST check CscpHxDic : {}", cscpHxDicExample);
        Map<String,Object> retMap = new HashMap(500);
        PageResult<CscpHxDic> byExample = cscpHxDicService.findByExample(cscpHxDicExample);
        List<CscpHxDic> data = byExample.getData();
        if(data.size() == 1){
            List<CscpHxDicItem> cscpHxDicItemList = cscpHxDicItemService.selectByDicId(data.get(0).getDicId());
            if(cscpHxDicItemList.size()>0){
                retMap.put("data",cscpHxDicItemList);
                retMap.put("msg", "获取成功");
                retMap.put("code", HttpStatus.OK);
            }else{
                retMap.put("msg", "获取失败");
                retMap.put("code",HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }else{
            retMap.put("msg", "获取失败");
            retMap.put("code",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body(retMap);
    }

    @ApiOperation(value = "根据dicCode查询基础字典项")
    @GetMapping("/cscpHxDicItems/getCscpBasicHxItemCode")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpBasicHxItemCode")
    @Log
    public ResponseEntity<Map> getCscpBasicHxItemCode(String  dicCode) {
        log.debug("REST check dicCode : {}", dicCode);
        Map<String,Object> retMap = new HashMap(500);
        CscpHxDicExample cscpHxDicExample = new CscpHxDicExample();
        cscpHxDicExample.createCriteria().andDicCodeEqualTo(dicCode).andDicAttrNotEqualTo(CscpDicAttrType.TREE);
        PageResult<CscpHxDic> byExample = cscpHxDicService.findByExample(cscpHxDicExample);
        List<CscpHxDic> data = byExample.getData();
        if(data.size() == 1){
            List<CscpHxDicItem> cscpHxDicItemList = cscpHxDicItemService.selectByDicId(data.get(0).getDicId());
            if(cscpHxDicItemList.size()>0){
                retMap.put("data",cscpHxDicItemList);
                retMap.put("msg", "获取成功");
                retMap.put("code", HttpStatus.OK);
            }else{
                retMap.put("msg", "获取失败");
                retMap.put("code",HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }else{
            retMap.put("msg", "获取失败");
            retMap.put("code",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body(retMap);
    }

}
