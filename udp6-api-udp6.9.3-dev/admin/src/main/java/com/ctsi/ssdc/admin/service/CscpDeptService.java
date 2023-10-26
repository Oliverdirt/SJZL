package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpDept;
import com.ctsi.ssdc.admin.domain.CscpDeptExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.TreeSelect;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CscpDept.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpDeptService 
	extends StrengthenBaseService<CscpDept, Long, CscpDeptExample>{


    /**
    * GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.
    */
    PageResult<CscpDept> findFirstStringColumn(String ancestors ,Pageable pageable);

    public List<TreeSelect> buildDeptTreeSelect(List<CscpDept> depts);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<CscpDept> buildDeptTree(List<CscpDept> depts);

    /**
     * 部门模块租户隔离--新增
     * @param var1
     * @return
     */
    @Override
    public CscpDept insert(CscpDept var1);

    /**
     * 部门模块租户隔离--修改
     * @param var1
     * @return
     */
    @Override
    public CscpDept update(CscpDept var1);

    /**
     * 部门模块租户隔离--查询（分页）
     * @param cscpDeptExample
     * @param pageable
     * @return
     */
    @Override
    public PageResult<CscpDept> findByExample(CscpDeptExample cscpDeptExample, Pageable pageable);

    /**
     * 部门模块租户隔离--查询
     * @param cscpDeptExample
     * @return
     */
    @Override
    public PageResult<CscpDept> findByExample(CscpDeptExample cscpDeptExample);

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<CscpDept> selectDeptList(CscpDept dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Long> selectDeptListByRoleId(Long roleId);

    @Override
    void delete(Long aLong);

}
