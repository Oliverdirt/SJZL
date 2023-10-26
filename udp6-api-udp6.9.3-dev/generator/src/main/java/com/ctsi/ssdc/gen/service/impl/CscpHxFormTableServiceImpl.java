package com.ctsi.ssdc.gen.service.impl;


import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.repository.CscpHxFormColumnRepository;
import com.ctsi.ssdc.gen.service.CscpHxFormColumnService;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.service.CscpHxTableManagerService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxFormTableExample;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.gen.repository.CscpHxFormTableRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CscpHxFormTable.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxFormTableServiceImpl 
	extends StrengthenBaseServiceImpl<CscpHxFormTableRepository, CscpHxFormTable, Long, CscpHxFormTableExample> 
	implements CscpHxFormTableService {

    @Autowired
    CscpHxFormTableRepository cscpHxFormTableRepository;
    @Autowired
    CscpHxFormColumnRepository cscpHxFormColumnRepository;

    @Autowired
    CscpHxFormColumnService cscpHxFormColumnService;

    @Autowired
    CscpHxTableManagerService cscpHxTableManagerService;

    @Autowired
    private CscpHxInfoSchemaMysqlService cscpHxInfoSchemaMysqlService;


    /**
    * GET  /cscpHxFormTables : get the cscpHxFormTables firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpHxFormTable> findFirstStringColumn(String tableName,Pageable pageable){
        String str = tableName;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpHxFormTableExample cscpHxFormTableExample = new CscpHxFormTableExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpHxFormTableExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpHxFormTableExample.createCriteria();
        } else{
            cscpHxFormTableExample.createCriteria().andTableNameLike("%" + str +"%");
        }
        List<CscpHxFormTable>  data = cscpHxFormTableRepository.selectByExample(cscpHxFormTableExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpHxFormTableRepository.countByExample(cscpHxFormTableExample);
        }
        return new PageResult<CscpHxFormTable>(data,count,count);
    }
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
    public CscpHxFormTable insert(CscpHxFormTable cscpHxFormTable) {
        //默认数据库未同步
        cscpHxFormTable.setIsDbSynch(0);
        super.insert(cscpHxFormTable);
        //如果字段信息为空 直接结束
        if(CollectionUtils.isEmpty(cscpHxFormTable.getColumns())){
            return cscpHxFormTable;
        }
        //设置字段表信息  for 循环里面写插入 需要修改
        for (CscpHxFormColumn column : cscpHxFormTable.getColumns()){
            if (ObjectUtils.isEmpty(column)) {
                continue;
            }
            column.setTableId(cscpHxFormTable.getTableId());
            cscpHxFormColumnService.insert(column);
        }
        return cscpHxFormTable;

    }


    @Override
    @Transactional
    public CscpHxFormTable update(CscpHxFormTable cscpHxFormTable) {
        //更新table表
        cscpHxFormTableRepository.updateByPrimaryKey(cscpHxFormTable);
        //更新field表
        List<CscpHxFormColumn> cscpHxFormColumns = cscpHxFormTable.getColumns();
        //没有字段信息
        if (null == cscpHxFormColumns){
            cscpHxFormColumnService.deleteByTable(cscpHxFormTable.getTableId());
            return cscpHxFormTable;
        }
        List<CscpHxFormColumn> columnsOld = cscpHxFormColumnService.getFieldListByTable(cscpHxFormTable.getTableId());
        for (CscpHxFormColumn column : cscpHxFormColumns){
            //没有id为新增字段
            if (null == column.getColumnId()){
                column.setTableId(cscpHxFormTable.getTableId());
                cscpHxFormColumnService.insert(column);
                continue;
            }
            //更新字段
            Iterator<CscpHxFormColumn> iterator = columnsOld.iterator();
            while (iterator.hasNext()){
                CscpHxFormColumn it = iterator.next();
                if (column.getColumnId().equals(it.getColumnId())){
                    //判断字段名称有没有修改
                    if (!it.getColumnName().equals(column.getColumnName())){
                        column.setColumnOldName(it.getColumnName());
                    }
                    //移除更新的字段，剩下的则是删除字段
                    iterator.remove();
                }
            }
            cscpHxFormColumnRepository.updateByPrimaryKey(column);
        }
        for (CscpHxFormColumn column:columnsOld){
            cscpHxFormColumnService.delete(column.getColumnId());
        }
        return cscpHxFormTable;
    }

    @Override
    public PageResult<CscpHxFormTable> findAll() {
        return super.findAll();
    }

    /**
     * 同步数据库
     *
     * @param id
     * @return
     */
    @Override
    public CscpHxFormTable syncDatabase(Long id) {
        CscpHxFormTable cscpHxFormTable = this.findOne(id);
        List<CscpHxFormColumn> columns = cscpHxFormColumnService.getFieldListByTable(id);
        cscpHxTableManagerService.syncDatabase(cscpHxFormTable,columns);
        columns.forEach(column -> column.setColumnOldName(""));
        cscpHxFormTable.setIsDbSynch(1);
        cscpHxFormTable.setColumns(columns);
        this.update(cscpHxFormTable);
        return cscpHxFormTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxFormTable syncDatabaseWithModelDesign(Long id) {
        CscpHxFormTable cscpHxFormTable = this.findOne(id);
        List<CscpHxFormColumn> columns = cscpHxFormColumnService.getFieldListByTable(id);
        syncDatabaseWithModelDesign(cscpHxFormTable,columns);
        columns.forEach(column -> column.setColumnOldName(""));
        cscpHxFormTable.setIsDbSynch(1);
        cscpHxFormTable.setColumns(columns);
        this.update(cscpHxFormTable);
        return cscpHxFormTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxFormTable createTable(CscpHxFormTable cscpHxFormTable,List<CscpHxFormColumn> columns) {
        syncDatabaseWithModelDesign(cscpHxFormTable,columns);
        return cscpHxFormTable;
    }

    private void syncDatabaseWithModelDesign(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns) {
        if(cscpHxTableManagerService.isTableExists(cscpHxFormTable)){
            cscpHxTableManagerService.updateTable(cscpHxFormTable,cscpHxFormColumns);
        } else {
            //如果表不存在，则新建表
            cscpHxTableManagerService.createTableIfNotExists(cscpHxFormTable,cscpHxFormColumns);
        }
    }

    @Override
    @Transactional
    public void delete(Long p) {
        cscpHxFormColumnService.deleteByTable(p);
        super.delete(p);
    }

    /**
     * 根据example查询是否存在
     *
     * @param cscpHxFormTableExample
     * @return
     */
    @Override
    public long countByExample(CscpHxFormTableExample cscpHxFormTableExample) {
        return   cscpHxFormTableRepository.countByExample(cscpHxFormTableExample);
    }

    /**
     * 查找一个表单的所有信息
     * @param tableId
     * @return
     */
    @Override
    public CscpHxFormTable findOneAll(Long tableId) {
        return r.selectAllById(tableId);
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
        cscpHxFormColumnService.deleteByTableIds(delList);
        //删除table表
        cscpHxFormTableRepository.deleteByIds(delList);
    }


}
