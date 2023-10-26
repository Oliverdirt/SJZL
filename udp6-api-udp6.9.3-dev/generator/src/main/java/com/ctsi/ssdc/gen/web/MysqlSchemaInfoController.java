package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.enums.MysqlTypeEnum;
import com.ctsi.ssdc.gen.domain.CopyTableInfo;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.vo.CscpHxInfoSchemaMysqlVO;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.utils.HxStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The code change the world !!!
 *操作数据库表元数据接口
 * @author guochui
 * @create 2022-07-07 8:21
 **/
@Api(tags = "数据库表元数据操作接口")
@RestController
@RequestMapping("/api/schema")
public class MysqlSchemaInfoController {

    @Autowired
    CscpHxInfoSchemaMysqlService cscpHxInfoSchemaMysqlService;

    /**
     * 获取数据库表列表
     * @return
     */
    @GetMapping("/cscpHxTablesInfo")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "cscpHxTablesInfo")
    @ApiOperation("获取数据库表列表")
    @Log(message = "获取数据库表列表")
    public ResponseEntity<List> cscpHxTablesInfo(){
        List list = cscpHxInfoSchemaMysqlService.getTablesInfo();
        return ResponseEntity.ok().body(list);
    }

    /**
     * 获取数据库视图列表
     * @return
     */
    @GetMapping("/cscpHxViewsInfo")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "cscpHxViewssInfo")
    @ApiOperation("获取数据库视图列表")
    @Log(message = "获取数据视图列表")
    public ResponseEntity<List> cscpHxViewsInfo(){
        List list = cscpHxInfoSchemaMysqlService.getViewsInfo();
        return ResponseEntity.ok().body(list);
    }

    /**
     * 根据表名获取数据库列信息
     * @param tableName
     * @return
     */
    @GetMapping("/getTableFieldInfoByMap")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getTableFieldInfoByMap")
    @ApiOperation("根据表名获取数据库列信息")
    @Log(message = "根据表名获取数据库列信息")
    public List<CscpHxInfoSchemaMysqlVO> getTableFieldInfoByMap(String tableName){
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
//        List<CscpHxInfoSchemaMysql> list = cscpHxInfoSchemaMysqlService.getTableFieldInfoByMap(map);
        List<CscpHxInfoSchemaMysqlVO> list;
        list = cscpHxInfoSchemaMysqlService.getTableFieldInfoByMap(map).stream().map(item -> {
            CscpHxInfoSchemaMysqlVO infVO = new CscpHxInfoSchemaMysqlVO();
            BeanUtils.copyProperties(item, infVO);
            infVO.convertAttribute();
            return infVO;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 创建及更新数据库表字段
     * @param cscpHxFormTable
     * @return
     */
    @PostMapping("/createOrUpdateFormTable")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "createOrUpdateFormTable")
    @ApiOperation("创建及更新数据库表字段")
    @Log(message = "创建及更新数据库表字段")
    public AjaxResult createOrUpdateFormTable(@RequestBody CscpHxFormTable cscpHxFormTable){
        return cscpHxInfoSchemaMysqlService.createOrUpdateFormTable(cscpHxFormTable);
    }

    /**
     * 初始化数据表
     * @param cscpHxFormTable
     * @return
     */
    @PostMapping("/initFormTable")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "initFormTable")
    @ApiOperation("初始化数据表")
    @Log(message = "初始化数据表")
    public AjaxResult initFormTable(@RequestBody CscpHxFormTable cscpHxFormTable){
        return cscpHxInfoSchemaMysqlService.initFormTable(cscpHxFormTable);
    }

    /**
     * 删除数据库表
     * @param tableName 表名
     * @return
     */
    @PostMapping("/deleteFormTable/{tableName}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteFormTable")
    @ApiOperation("删除数据库表")
    @Log(message = "删除数据库表")
    public AjaxResult deleteFormTable(@PathVariable("tableName") String tableName){

        return cscpHxInfoSchemaMysqlService.deleteFormTable(tableName);
    }

    /**
     * 获取数据库字段属性
     * @param
     * @return
     */
    @GetMapping("/getColumnTypeList")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getColumnTypeList")
    @ApiOperation("获取数据库字段属性下拉列表")
    @Log(message = "获取数据库字段属性下拉列表")
    public ResponseEntity<List> getColumnTypeList(String type){
        List<Map> list = MysqlTypeEnum.getTypeList();
        if (HxStringUtils.isEmpty(type)) {
            // type为空过滤table类型
            List<Map> collect = list.stream().filter(map -> !"table".equals(map.get("typeName")))
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(collect);
        }
        return ResponseEntity.ok().body(list);
    }

    /**
     * 复制数据表
     * @param copyTableInfo
     * @return
     */
    @PostMapping("/copyTable")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "copyTable")
    @ApiOperation("复制数据表")
    @Log(message = "复制数据表")
    public AjaxResult copyTable(@RequestBody CopyTableInfo copyTableInfo){
        return cscpHxInfoSchemaMysqlService.copyTable(copyTableInfo);
    }


    /**
     * 判断表是否存在
     * @return
     */
    @GetMapping("/isTableExists")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "isTableExists")
    @ApiOperation("判断表是否存在")
    @Log(message = "判断表是否存在")
    public AjaxResult isTableExists(String tableName){
        boolean exists = cscpHxInfoSchemaMysqlService.isTableExists(tableName);
        if(exists){
            return AjaxResult.success("数据表已存在");
        }else{
            return AjaxResult.error(-1,"数据表不存在");
        }

    }
}
