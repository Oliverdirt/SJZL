package com.ctsi.ssdc.dic.web;

import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.dic.domain.CscpHxDic;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.dic.exception.CustomizedProblem;
import com.ctsi.ssdc.dic.service.CscpHxDicItemService;
import com.ctsi.ssdc.dic.service.CscpHxDicService;
import com.ctsi.ssdc.dic.service.CscpHxSqldicBaseinfoService;
import com.ctsi.ssdc.dic.service.SqlDicService;
import com.ctsi.ssdc.dic.consts.SqlDicConstant;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfo;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Status;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-02-09 18:17
 **/
@RestController
@RequestMapping("/api")
public class SqlDicController {

    @Autowired
    SqlDicService sqlDicService;
    
    @Autowired
    CscpHxSqldicBaseinfoService cscpHxSqldicBaseinfoService;

    @Autowired
    CscpHxDicService cscpHxDicService;

    @Autowired
    CscpHxDicItemService cscpHxDicItemService;



    @GetMapping("/selectSqlDic/{id}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "selectSqlDic")
    @Log
    public List<LinkedHashMap<String, Object>>  selectSqlDic(@PathVariable("id") Long id , HttpServletRequest request){

        // 获取执行结果列表
        List<LinkedHashMap<String, Object>> selectDicList = getDicList(id, request);
        return selectDicList;

    }

    @GetMapping("/selectDicItemByDicCode/{id}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "selectDicItemByDicCode")
    @Log
    public ResponseEntity<List<CscpHxDicItem>> selectDicItemByDicCode(@PathVariable("id") Long  id , HttpServletRequest request){

        String dicCode = cscpHxSqldicBaseinfoService.findOne(id).getDicCode();
        // 查询当前编码字典数据
        CscpHxDicExample cscpHxDicExample = new CscpHxDicExample();
        StringCriteria stringCriteria = new StringCriteria();
        stringCriteria.setEquals(dicCode);
        cscpHxDicExample.setDicCode(stringCriteria);
        PageResult<CscpHxDic> cscpHxDicServiceByDicCode = cscpHxDicService.findByExample(cscpHxDicExample);
        List<CscpHxDicItem> cscpHxDicItems = new ArrayList<>();
        if(cscpHxDicServiceByDicCode.getRecordsTotal()>0){
            Long dicId = cscpHxDicServiceByDicCode.getData().get(0).getDicId();
            cscpHxDicItems = cscpHxDicItemService.selectByDicId(dicId);
        }
        return ResponseEntity.ok().body(cscpHxDicItems);

    }


    private List<LinkedHashMap<String, Object>> getDicList(Long id, HttpServletRequest request) {
        CscpHxSqldicBaseinfo cscpHxSqldicBaseinfoServiceOne = cscpHxSqldicBaseinfoService.findOne(id);
        String dicSelectSql = cscpHxSqldicBaseinfoServiceOne.getDicSelectSql();
        String itemCodeColumn = "";
        if(null != cscpHxSqldicBaseinfoServiceOne.getDicItemCodeColumn()){
            itemCodeColumn = cscpHxSqldicBaseinfoServiceOne.getDicItemCodeColumn().toLowerCase();
        }
        String itemValueColumn = cscpHxSqldicBaseinfoServiceOne.getDicItemValueColumn().toLowerCase();
        String dicItemValueNotes = cscpHxSqldicBaseinfoServiceOne.getDicItemValueNotes();
        // 查询sql结果列表
        List<LinkedHashMap<String, Object>> selectSqlResultList = null;
        try{
            selectSqlResultList = sqlDicService.select(dicSelectSql);
        }catch (Exception e){
            throw new CustomizedProblem("SQL执行失败", Status.BAD_REQUEST,
                    dicSelectSql, e.getMessage(), request.getRequestURI());
        }

        // 封装为字典字段
        List<LinkedHashMap<String, Object>> selectDicList  = new ArrayList<>();
        if (null == selectSqlResultList || selectSqlResultList.size() == 0) { return null;}
        for ( int i = 0; i < selectSqlResultList.size(); i++) {
            LinkedHashMap selectSqlResultMap = selectSqlResultList.get(i);
            LinkedHashMap<String, Object> selectDicMap = new LinkedHashMap<>();
            int finalI = i;
            String finalItemCodeColumn = itemCodeColumn;
            selectSqlResultMap.forEach((k, v) ->{
                // 如果不传递字典编码，默认数字填充
                if ("".equals(finalItemCodeColumn)){
                    selectDicMap.put(SqlDicConstant.DIC_CODE, finalI);
                }else{
                    selectDicMap.put(SqlDicConstant.DIC_CODE,selectSqlResultMap.get(finalItemCodeColumn).toString());
                }
                selectDicMap.put(SqlDicConstant.DIC_NAME,selectSqlResultMap.get(itemValueColumn).toString());

                if(null != selectSqlResultMap.get(dicItemValueNotes)){
                    selectDicMap.put(SqlDicConstant.DIC_NOTE, selectSqlResultMap.get(dicItemValueNotes).toString());
                }

            });

            selectDicList.add(selectDicMap);
        }
        return selectDicList;
    }


    @PostMapping("/syncdic/{id}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "syncDic")
    @Log
    public void syncDic(@PathVariable("id") Long id , HttpServletRequest request){
        // 获取当前sql字典列表
        List<LinkedHashMap<String, Object>> selectDicList = getDicList(id, request);

        // 查询当前编码字典数据
        CscpHxSqldicBaseinfo cscpHxSqldicBaseinfo = cscpHxSqldicBaseinfoService.findOne(id);
        CscpHxDicExample cscpHxDicExample = new CscpHxDicExample();
        StringCriteria stringCriteria = new StringCriteria();
        stringCriteria.setEquals(cscpHxSqldicBaseinfo.getDicCode());
        cscpHxDicExample.setDicCode(stringCriteria);

        PageResult<CscpHxDic> cscpHxDicServiceByDicCode = cscpHxDicService.findByExample(cscpHxDicExample);

        Long dicId ;

        if (cscpHxDicServiceByDicCode.getRecordsTotal() >0){
            // 更新字典项
            CscpHxDic cscpHxDic = cscpHxDicServiceByDicCode.getData().get(0);
            // 删除原有字典项
            List<CscpHxDicItem> cscpHxDicItems = cscpHxDicItemService.selectByDicId(cscpHxDic.getDicId());
            cscpHxDicItems.parallelStream().forEach((t)->{
                cscpHxDicItemService.delete(t.getItemId());
            });
            dicId = cscpHxDic.getDicId();
        } else {
            // 插入字典类型
            CscpHxDic cscpHxDic = new CscpHxDic();
            cscpHxDic.setDicCode(cscpHxSqldicBaseinfo.getDicCode());
            cscpHxDic.setDicName(cscpHxSqldicBaseinfo.getDicName());
            cscpHxDic.setDicAttr(SqlDicConstant.SQL_DIC);
            cscpHxDic.setDescription(cscpHxSqldicBaseinfo.getDicNotes());
            CscpHxDic hxDic = cscpHxDicService.insert(cscpHxDic);
            dicId = hxDic.getDicId();

        }
            // 插入最新字典项 selectDicList
            for (LinkedHashMap<String,Object> selectDicListItem :selectDicList) {
                CscpHxDicItem cscpHxDicItem = new CscpHxDicItem();
                cscpHxDicItem.setDicId(dicId);
                cscpHxDicItem.setItemCode(selectDicListItem.get(SqlDicConstant.DIC_CODE).toString());
                cscpHxDicItem.setItemValue((String) selectDicListItem.get(SqlDicConstant.DIC_NAME));
                cscpHxDicItem.setDescription((String) selectDicListItem.get(SqlDicConstant.DIC_NOTE));
                cscpHxDicItemService.insert(cscpHxDicItem);
            }

    }


}
