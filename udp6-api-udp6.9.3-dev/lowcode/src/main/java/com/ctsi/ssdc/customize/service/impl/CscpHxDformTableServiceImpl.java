package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformTable;
import com.ctsi.ssdc.customize.domain.CscpHxDformTableExample;
import com.ctsi.ssdc.customize.repository.CscpHxDformColumnRepository;
import com.ctsi.ssdc.customize.repository.CscpHxDformTableRepository;
import com.ctsi.ssdc.customize.service.CscpHxDTableManagerService;
import com.ctsi.ssdc.customize.service.CscpHxDformTableService;
import com.ctsi.ssdc.customize.service.CscpHxDformColumnService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/14 11:14  by xx
 */
@Service
public class CscpHxDformTableServiceImpl
        extends StrengthenBaseServiceImpl<CscpHxDformTableRepository, CscpHxDformTable, Long, CscpHxDformTableExample>
        implements CscpHxDformTableService {

    @Resource
    CscpHxDformTableRepository cscpHxDformTableRepository;
    @Resource
    CscpHxDformColumnRepository cscpHxDformColumnRepository;

    @Autowired
    CscpHxDformColumnService cscpHxDformColumnService;

    @Autowired
    CscpHxDTableManagerService cscpHxDTableManagerService;


    private String getPageOrderBy(Pageable page) {
        if(page!= null && page.getSort() != null) {
            StringBuilder sb = new StringBuilder();
            page.getSort().forEach(sort -> sb.append(sort.getProperty())
                    .append(" ").append(sort.getDirection()).append(","));
            if(sb.length() > 1) {
                return (sb.substring(0,sb.length()-1));
            }
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxDformTable insert(CscpHxDformTable  cscpHxDformTable) {
        //默认数据库未同步
//        CscpHxDformTable.setIsDbSynch(0);
        super.insert(cscpHxDformTable);
        if(CollectionUtils.isEmpty(cscpHxDformTable.getColumns())) {
            return cscpHxDformTable;
        }
        for (CscpHxDformColumn column : cscpHxDformTable.getColumns()){
            if (ObjectUtils.isEmpty(column)) {
                continue;
            }
            column.setTableId(cscpHxDformTable.getTableId());
            cscpHxDformColumnService.insert(column);
        }
        return cscpHxDformTable;

    }


    @Override
    @Transactional
    public CscpHxDformTable update(CscpHxDformTable CscpHxDformTable) {
        //更新table表
        cscpHxDformTableRepository.updateByPrimaryKey(CscpHxDformTable);
        //更新field表
        List<CscpHxDformColumn> CscpHxDformColumns = CscpHxDformTable.getColumns();
        //没有字段信息
        if (null == CscpHxDformColumns){
            cscpHxDformColumnService.deleteByTable(CscpHxDformTable.getTableId());
            return CscpHxDformTable;
        }
        List<CscpHxDformColumn> columnsOld = cscpHxDformColumnService.getFieldListByTable(CscpHxDformTable.getTableId());
        for (CscpHxDformColumn column : CscpHxDformColumns){
            //没有id为新增字段
            if (null == column.getColumnId()){
                column.setTableId(CscpHxDformTable.getTableId());
                cscpHxDformColumnService.insert(column);
                continue;
            }
            //更新字段
            Iterator<CscpHxDformColumn> iterator = columnsOld.iterator();
            while (iterator.hasNext()){
                CscpHxDformColumn it = iterator.next();
                if (column.getColumnId().equals(it.getColumnId())){
                    //判断字段名称有没有修改
                    if (!it.getColumnName().equals(column.getColumnName())){
                        column.setColumnNameOld(it.getColumnName());
                    }
                    //移除更新的字段，剩下的则是删除字段
                    iterator.remove();
                }
            }
            cscpHxDformColumnRepository.updateByPrimaryKey(column);
        }
        for (CscpHxDformColumn column:columnsOld){
            cscpHxDformColumnService.delete(column.getColumnId());
        }
        return CscpHxDformTable;
    }

    @Override
    public PageResult<CscpHxDformTable> findAll() {
        return super.findAll();
    }

    /**
     * 同步数据库
     *
     * @param id
     * @return
     */
    @Override
    public CscpHxDformTable syncDatabase(Long id) {
        CscpHxDformTable CscpHxDformTable = this.findOne(id);
        List<CscpHxDformColumn> columns = cscpHxDformColumnService.getFieldListByTable(id);
        cscpHxDTableManagerService.syncDatabase(CscpHxDformTable,columns);
        columns.forEach(column -> column.setColumnNameOld(""));
        CscpHxDformTable.setIsDbSynch(1);
        CscpHxDformTable.setColumns(columns);
        this.update(CscpHxDformTable);
        return CscpHxDformTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxDformTable syncDatabaseWithModelDesign(Long id) {
        CscpHxDformTable CscpHxDformTable = this.findOne(id);
        List<CscpHxDformColumn> columns = cscpHxDformColumnService.getFieldListByTable(id);
        syncDatabaseWithModelDesign(CscpHxDformTable,columns);
        columns.forEach(column -> column.setColumnNameOld(""));
        CscpHxDformTable.setIsDbSynch(1);
        CscpHxDformTable.setColumns(columns);
        this.update(CscpHxDformTable);
        return CscpHxDformTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxDformTable createTable(CscpHxDformTable CscpHxDformTable,List<CscpHxDformColumn> columns) {
        syncDatabaseWithModelDesign(CscpHxDformTable,columns);
        return CscpHxDformTable;
    }

    private void syncDatabaseWithModelDesign(CscpHxDformTable cscpHxDFormTable, List<CscpHxDformColumn> CscpHxDformColumns) {
        String tableName = cscpHxDFormTable.getTableName();
        if(cscpHxDTableManagerService.isTableExists(cscpHxDFormTable)){
            cscpHxDTableManagerService.updateTable(cscpHxDFormTable,CscpHxDformColumns);
            //临时表用于导入功能
            cscpHxDFormTable.setTableName("imp_temp_"+cscpHxDFormTable.getTableName());
            cscpHxDTableManagerService.updateTable(cscpHxDFormTable,CscpHxDformColumns);
        } else {
            //如果表不存在，则新建表
            cscpHxDTableManagerService.createTableIfNotExists(cscpHxDFormTable,CscpHxDformColumns);
            //创建临时表用于导入功能
            cscpHxDFormTable.setTableName("imp_temp_"+cscpHxDFormTable.getTableName());
            cscpHxDTableManagerService.createTableIfNotExists(cscpHxDFormTable,CscpHxDformColumns);

        }
        cscpHxDFormTable.setTableName(tableName);
    }

    @Override
    @Transactional
    public void delete(Long p) {
        cscpHxDformColumnService.deleteByTable(p);
        super.delete(p);
    }

    /**
     * 根据example查询是否存在
     *
     * @param CscpHxDformTableExample
     * @return
     */
    @Override
    public long countByExample(CscpHxDformTableExample CscpHxDformTableExample) {
        return   cscpHxDformTableRepository.countByExample(CscpHxDformTableExample);
    }

    /**
     * 查找一个表单的所有信息
     * @param tableId
     * @return
     */
    @Override
    public CscpHxDformTable findOneAll(Long tableId) {
        return r.selectAllById(tableId);
    }

    @Override
    public List<CscpHxDformTable> findsubAll(Long mainTableId) {
        return r.selectSubAllById(mainTableId);
    }

    /**
     * 批量删除
     *
     * @param ids
     *
     */
    @Override
    @Transactional
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        //删除column表
        cscpHxDformColumnService.deleteByTableIds(delList);
        //删除table表
        cscpHxDformTableRepository.deleteByIds(delList);
    }

}
