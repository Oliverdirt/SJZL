package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.enums.DataBaseEnum;
import com.ctsi.ssdc.enums.MysqlTypeEnum;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.vo.CscpHxInfoSchemaMysqlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * REST controller for managing CscpHxFormTable.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-generator-controller"})
@RestController
@RequestMapping("/api")
public class CscpHxGeneratorController {

    @Autowired
    private CscpHxInfoSchemaMysqlService informationSchemaMysqlService;


    /**
     * 获取表的字段信息
     * @param tableName
     * @param dbName 预留字段，目前直接取配置文件(也可以传入）
     * @return
     */
    @GetMapping("/cscpHxFormColums")
    @ApiOperation("获取表的字段信息")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getTableFields")
    @Log(message = "获取表的字段信息")
    public ResponseEntity<List> getTableFields(@RequestParam String tableName, @RequestParam(required = false) String dbName){
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        map.put("dbName",dbName);
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMap(map);
        List<CscpHxInfoSchemaMysqlVO> list1;
        list1 = list.stream().map(item -> {
            CscpHxInfoSchemaMysqlVO infVO = new CscpHxInfoSchemaMysqlVO();
            BeanUtils.copyProperties(item, infVO);
            infVO.convertAttribute();
            return infVO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(list1);
    }

    /**
     * 获取表的字段信息
     * @param tableName 表名称，用逗号隔开
     * @param dbName 预留字段，目前直接取配置文件(也可以传入）
     * @return
     */
    @GetMapping("/cscpHxFormColumsBatch")
    @ApiOperation("获取表的字段信息")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getTableFields")
    @Log(message = "获取表的字段信息")
    public ResponseEntity<Map> getTableFieldsBatch(@RequestParam String tableName, @RequestParam(required = false) String dbName){
        Map<String,Object> map = new HashMap<>();
        Map<Object, List<CscpHxInfoSchemaMysql>> resultMap = new HashMap<>();
        if(StringUtils.isEmpty(tableName)){
            return ResponseEntity.ok().body(resultMap);
        }
        List<String> tableNames = Arrays.asList(tableName.split(","));
        map.put("tableNames",tableNames);
        map.put("dbName",dbName);
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMapBatch(tableNames,dbName);
        resultMap = list.stream().collect(Collectors.groupingBy(CscpHxInfoSchemaMysql::getTableName));
        return ResponseEntity.ok().body(resultMap);
    }

    /**
     * 获取表的字段信息
     * @param tableName
     * @param dbName 预留字段，目前直接取配置文件(也可以传入）
     * @return
     */
    @GetMapping("/getTableOperateFields")
    @ApiOperation("获取表的可操作字段信息")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getTableOperateFields")
    @Log(message = "获取表的可操作字段信息")
    public ResponseEntity<List> getTableOperateFields(@RequestParam String tableName, @RequestParam(required = false) String dbName){
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        map.put("dbName",dbName);
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMap(map);
        List<CscpHxInfoSchemaMysqlVO> list1;
        list1 = list.stream().map(item -> {
            CscpHxInfoSchemaMysqlVO infVO = new CscpHxInfoSchemaMysqlVO();
            BeanUtils.copyProperties(item, infVO);
            infVO.convertAttribute();
            return infVO;
        }).filter(item->!StringUtils.contains(item.getColumnName(),"id")).collect(Collectors.toList());
        return ResponseEntity.ok().body(list1);
    }

    /**
     * 获取数据库表列表
     * @return
     */
    @GetMapping("/cscpHxTables")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getTables")
    @ApiOperation("获取数据库表列表")
    @Log(message = "获取数据库表列表")
    public ResponseEntity<List> getTables(){
        List list = informationSchemaMysqlService.getTables();
        return ResponseEntity.ok().body(list);
    }


    /**
     * 获取数据库字段属性
     * @param dbType
     * @return
     */
    @GetMapping("/cscpHxColumnTypes")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getFieldTypeList")
    @Log
    public ResponseEntity<List> getFieldTypeList(@RequestParam Integer dbType){
        List list = null;
        switch (DataBaseEnum.getByNumber(dbType)){
            //mysql类型
            case MYSQL: list = MysqlTypeEnum.getTypeList();
                break;
            default: break;
        }
        return ResponseEntity.ok().body(list);
    }
}
