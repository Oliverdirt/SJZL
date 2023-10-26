package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.CscpDept;
import com.ctsi.ssdc.admin.domain.CscpDeptExample;
import com.ctsi.ssdc.admin.repository.CscpDeptRepository;
import com.ctsi.ssdc.admin.repository.CscpRolesRepository;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import com.ctsi.ssdc.admin.service.CscpDeptService;
import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.TreeSelect;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing CscpDept.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpDeptServiceImpl 
	extends StrengthenBaseServiceImpl<CscpDeptRepository, CscpDept, Long, CscpDeptExample> 
	implements CscpDeptService {

    @Autowired
    CscpDeptRepository cscpDeptRepository;

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    private CscpRolesRepository cscpRolesRepository;


    /**
    * GET  /cscpDepts : get the cscpDepts firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpDept> findFirstStringColumn(String ancestors,Pageable pageable){
        String str = ancestors;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpDeptExample cscpDeptExample = new CscpDeptExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpDeptExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpDeptExample.createCriteria();
        } else{
            cscpDeptExample.createCriteria().andAncestorsLike("%" + str +"%");
        }
        List<CscpDept>  data = cscpDeptRepository.selectByExample(cscpDeptExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpDeptRepository.countByExample(cscpDeptExample);
        }
        return new PageResult<CscpDept>(data,count,count);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<CscpDept> depts) {
        List<CscpDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<CscpDept> buildDeptTree(List<CscpDept> depts)
    {
        List<CscpDept> returnList = new ArrayList<CscpDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (CscpDept dept : depts)
        {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<CscpDept> iterator = depts.iterator(); iterator.hasNext();)
        {
            CscpDept dept = (CscpDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<CscpDept> list, CscpDept t)
    {
        // 得到子节点列表
        List<CscpDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CscpDept tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CscpDept> list, CscpDept t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<CscpDept> getChildList(List<CscpDept> list, CscpDept t)
    {
        List<CscpDept> tlist = new ArrayList<CscpDept>();
        Iterator<CscpDept> it = list.iterator();
        while (it.hasNext())
        {
            CscpDept n = (CscpDept) it.next();
            if ((n.getParentId()!=null) && n.getParentId().longValue() == t.getDeptId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
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

    /**
     * 部门模块租户隔离--新增
     * @param dept
     * @return
     */
    @Override
    public CscpDept insert(CscpDept dept){
        CscpDept info = cscpDeptRepository.selectByPrimaryKey(dept.getParentId());

        if (info != null){
            dept.setAncestors(info.getAncestors() + dept.getParentId()+",");
            int length = info.getAncestors().split(",").length;
            if (length > 5) {
                throw new BadRequestAlertException("新增部门失败：部门层级过多","CscpDeptServiceImpl.insert","");
            }
        }
        Long tenantId = null;
        Long deptTenantId = dept.getTenantId();
        if ((deptTenantId == null)) {
            // 获取当前租户id
            tenantId = SecurityHxUtils.getCurrentTenantId();
        } else {
            tenantId = deptTenantId;
        }
        dept.setTenantId(tenantId);
        return super.insert(dept);
    }
    /**
     * 部门模块租户隔离--修改
     * @param dept
     * @return
     */
    @Override
    public CscpDept update(CscpDept dept){
        CscpDept info = cscpDeptRepository.selectByPrimaryKey(dept.getParentId());
        int length = info.getAncestors().split(",").length;
        if (length > 5) {
            throw new BadRequestAlertException("编辑部门失败：部门层级过多","CscpDeptServiceImpl.insert","");
        }
        dept.setAncestors(info.getAncestors() + dept.getParentId()+",");
        // 获取当前租户id
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        dept.setTenantId(tenantId);
        return super.update(dept);
    }
    /**
     * 部门模块租户隔离-查询
     * @param cscpDeptExample
     * @param pageable
     * @return
     */
    @Override
    public PageResult<CscpDept> findByExample(CscpDeptExample cscpDeptExample, Pageable pageable){
        // 获取当前租户id
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        LongCriteria tenant = new LongCriteria();
        tenant.setEquals(tenantId);
        cscpDeptExample.setTenantId(tenant);
        return super.findByExample(cscpDeptExample,pageable);
    }

    /**
     * 部门模块租户隔离-查询
     * @param cscpDeptExample
     * @return
     */
    @Override
    public PageResult<CscpDept> findByExample(CscpDeptExample cscpDeptExample){
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
//        Long tenantId = cscpUserDetailRepository.selectByUserId(currentUserId).getTenantId();
        LongCriteria tenant = new LongCriteria();
        tenant.setEquals(tenantId);
        cscpDeptExample.setTenantId(tenant);
        return super.findByExample(cscpDeptExample);
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<CscpDept> selectDeptList(CscpDept dept)
    {
        // 获取当前租户id
        Long tenantId = SecurityHxUtils.getCurrentTenantId();
        dept.setTenantId(tenantId);
        return cscpDeptRepository.selectDeptList(dept);
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId)
    {
        return cscpDeptRepository.selectDeptListByRoleId(roleId);
    }

    /**
     * 删除部门信息
     * @param p
     */
    @Override
    public void delete(Long p) {
        super.delete(p);
    }

}
