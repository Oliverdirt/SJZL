package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.CscpRolesExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.repository.CscpRoleDeptRepository;
import com.ctsi.ssdc.admin.repository.CscpRoleMenuRepository;
import com.ctsi.ssdc.admin.repository.CscpRolesRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRoleRepository;
import com.ctsi.ssdc.admin.service.CscpRolesService;
import com.ctsi.ssdc.admin.service.mapper.CscpRolesMapper;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.utils.HxStringUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpRoles.
 *
 * @author ctsi biyi generator
 */
@Service
public class CscpRolesServiceImpl implements CscpRolesService {

    private final Logger log = LoggerFactory.getLogger(CscpRolesServiceImpl.class);

    @Autowired
    private CscpRolesRepository cscpRolesRepository;

    private final CscpRolesMapper cscpRolesMapper;

    @Autowired
    private CscpUserRoleRepository cscpUserRoleRepository;


    @Autowired
    private CscpRoleMenuRepository roleMenuRepository;
    @Autowired
    private CscpRoleDeptRepository cscpRoleDeptRepository;

    public CscpRolesServiceImpl(CscpRolesMapper cscpRolesMapper) {
        this.cscpRolesMapper = cscpRolesMapper;
    }

    /**
     * insert a cscpRoles.
     *
     * @param cscpRolesDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpRolesDTO insert(CscpRolesDTO cscpRolesDTO) {
        log.debug("Request to insert CscpRoles : {}", cscpRolesDTO);

        CscpRoles cscpRoles = cscpRolesMapper.toEntity(cscpRolesDTO);
        cscpRolesRepository.insert(cscpRoles);
        return cscpRolesMapper.toDto(cscpRoles);
    }

    /**
     * update a cscpRoles.
     *
     * @param cscpRolesDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpRolesDTO update(CscpRolesDTO cscpRolesDTO) {
        log.debug("Request to update CscpRoles : {}", cscpRolesDTO);

        CscpRoles cscpRoles = cscpRolesMapper.toEntity(cscpRolesDTO);
        cscpRolesRepository.updateByPrimaryKeySelective(cscpRoles);
        return cscpRolesMapper.toDto(cscpRoles);
    }


    /**
     * Get all the cscpRoless.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpRolesDTO> findAll() {
        log.debug("Request to get all CscpRoless");

        List<CscpRolesDTO> data = cscpRolesRepository.selectByExample(null).stream()
                .map(cscpRolesMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpRolesRepository.countByExample(null);
        }

        return new PageResult<CscpRolesDTO>(data, count, count);

    }

    /**
     * Get one cscpRoles.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpRolesDTO findOne(Long id) {
        log.debug("Request to get CscpRoles : {} ", id);

        CscpRoles cscpRoles = cscpRolesRepository.selectByPrimaryKey(id);
        return cscpRolesMapper.toDto(cscpRoles);
    }

    /**
     * Delete the cscpRoles .
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CscpRoles : {} ", id);

        cscpRolesRepository.deleteByPrimaryKey(id);

        //删除用户与角色关联表；
        CscpUserRoleExample example4 = new CscpUserRoleExample();
        com.ctsi.ssdc.admin.domain.CscpUserRoleExample.Criteria cx4 = example4.createCriteria();
        cx4.andRoleIdEqualTo(id);
        cscpUserRoleRepository.deleteByExample(example4);

        //角色和菜单关联表；
        CscpRoleMenuExample rm = new CscpRoleMenuExample();
        CscpRoleMenuExample.Criteria cx = rm.createCriteria();
        cx.andRoleIdEqualTo(id);
        roleMenuRepository.deleteByExample(rm);
    }

    private String getPageOrderBy(Pageable page) {

        if (page != null && page.getSort() != null) {

            StringBuilder sb = new StringBuilder();

            page.getSort().forEach(sort -> sb.append(sort.getProperty())
                    .append(" ").append(sort.getDirection()).append(","));

            if (sb.length() > 1) {
                return (sb.substring(0, sb.length() - 1));
            }
        }

        return null;
    }

    /**
     * Get the cscpRoless.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpRolesDTO> findByCscpRolesDTO(CscpRolesDTO cscpRolesDTO, Pageable page) {

        log.debug("Request to find CscpRoless");

        CscpRolesExample example = new CscpRolesExample();

        example.setPage(page);

        Criteria critieria = example.createCriteria();

        if (cscpRolesDTO.getId() != null) {
            critieria.andIdEqualTo(cscpRolesDTO.getId());
        }
        if (cscpRolesDTO.getParentId() != null) {
            critieria.andParentIdEqualTo(cscpRolesDTO.getParentId());
        }

        if (HxStringUtils.isNotBlank(cscpRolesDTO.getName())) {
            critieria.andNameLike(String.format("%%%s%%", cscpRolesDTO.getName()));
        }
        if (HxStringUtils.isNotBlank(cscpRolesDTO.getRoleExtent())) {
            critieria.andRoleExtentLike(String.format("%%%s%%", cscpRolesDTO.getRoleExtent()));
        }
        if (HxStringUtils.isNotBlank(cscpRolesDTO.getIcon())) {
            critieria.andIconLike(String.format("%%%s%%", cscpRolesDTO.getIcon()));
        }

        String orderBy = getPageOrderBy(page);

        if (HxStringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpRolesDTO> data = cscpRolesMapper.toDto(cscpRolesRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpRolesRepository.countByExample(example);
        }

        return new PageResult<CscpRolesDTO>(data, count, count);

    }

    /**
     * Get the cscpRoless.
     *
     * @param cscpRolesCriteria
     * @param page
     * @return
     */
    @Override
    public PageResult<CscpRolesDTO> findByCscpRolesCriteria(CscpRolesCriteria cscpRolesCriteria, Pageable page) {

        CscpRolesExample example = new CscpRolesExample();

        example.setPage(page);

        Criteria criteria = example.createCriteria();

        if (cscpRolesCriteria != null) {
            cscpRolesCriteria.buildCriteria(criteria);
        }

        String orderBy = getPageOrderBy(page);

        if (HxStringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpRolesDTO> data = cscpRolesMapper.toDto(cscpRolesRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpRolesRepository.countByExample(example);
        }

        return new PageResult<CscpRolesDTO>(data, count, count);

    }

    @Override
    @Transactional
    public String saveUsers(Long roleId, Long[] userIds) {

        try {

            CscpUserRoleExample example = new CscpUserRoleExample();
            example.or().andRoleIdEqualTo(roleId);
            cscpUserRoleRepository.deleteByExample(example);
            if (userIds.length == 0) {
                return "true";
            }
            for (Long userId : userIds) {
                CscpUserRole record = new CscpUserRole();
                record.setUserId(userId);
                record.setRoleId(roleId);
                cscpUserRoleRepository.insert(record);
            }
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    @Override
    public List<CscpUser> getUsersByRoleId(Long roleId) {
        return cscpUserRoleRepository.getUsersByRoleId(roleId);
    }

    @Override
    public List<CscpUser> getUsersByRoleIds(Set<Long> roleIds) {
        return cscpUserRoleRepository.getUsersByRoleIds(roleIds);
    }


    @Override
    public CscpRoles getRoleByTenant(CscpRoles roles) {
        return cscpRolesRepository.getRoleByTenant(roles);
    }

    @Override
    public void checkRoleAllowed(CscpRoles roles) {
        if (HxStringUtils.isNotNull(roles.getId()) && "admin".equals(roles.getName())) {
            throw new BadRequestAlertException("不允许操作管理员角色", roles.toString(), "");
        }
    }

    @Override
    public int authDataScope(CscpRoles roles) {
        cscpRolesRepository.updateByPrimaryKey(roles);
        // 删除角色与部门关联
        cscpRoleDeptRepository.deleteRoleDeptByRoleId(roles.getId());
        return insertRoleDept(roles);
    }


    /**
     * 新增角色部门信息(数据权限)
     *
     * @param roles 角色对象
     */
    public int insertRoleDept(CscpRoles roles) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<CscpRoleDept> list = new ArrayList<>();
        for (Long deptId : roles.getDeptIds()) {
            CscpRoleDept rd = new CscpRoleDept();
            rd.setRoleId(roles.getId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = cscpRoleDeptRepository.batchRoleDept(list);
        }
        return rows;
    }

    @Override
    public List<CscpRoles> getNameById(List<Long> ids) {
        List<CscpRoles> nameById = cscpRolesRepository.getNameById(ids);
        return nameById;
    }
}
