package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.constants.AdminFlagConstant;
import com.ctsi.ssdc.admin.constants.TenantStatusConstant;
import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpRolesDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserRoleDTO;
import com.ctsi.ssdc.admin.repository.*;
import com.ctsi.ssdc.admin.service.*;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;


/**
 * Service Implementation for managing CscpTenant.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpTenantServiceImpl 
	extends StrengthenBaseServiceImpl<CscpTenantRepository, CscpTenant, Long, CscpTenantExample> 
	implements CscpTenantService {
    private final Logger log = LoggerFactory.getLogger(CscpTenantServiceImpl.class);
    private

    @Autowired
    CscpTenantRepository cscpTenantRepository;

    @Autowired
    CscpDeptService cscpDeptService;

    @Autowired
    CscpUserService cscpUserService;

    @Autowired
    CscpUserDetailService cscpUserDetailService;

    @Autowired
    CscpUserRoleService cscpUserRoleService;


    @Autowired
    CscpRolesService cscpRolesService;


    @Autowired
    CscpMenusService cscpMenusService;
    
    @Autowired
    CscpUserDetailRepository cscpUserDetailRepository;

    @Autowired
    CscpUserRepository cscpUserRepository;

    @Autowired
    CscpDeptRepository cscpDeptRepository;

    @Autowired
    CscpRoleDeptRepository cscpRoleDeptRepository;

    @Autowired
    CscpUserRoleRepository cscpUserRoleRepository;

    @Autowired
    CscpRolesRepository cscpRolesRepository;

    @Autowired
    CscpRoleMenuRepository cscpRoleMenuRepository;





    @Override
    public int checkTenantAccount(String tenantAccount,String username) {
        CscpTenant cscpTenant = selectByTenantAccount(tenantAccount);
        //0:无租户 //1:租户存在  //2:过期 //3:该租户下，用户不存在
        if(cscpTenant == null){
            return TenantStatusConstant.TENANT_NULL;
        }
        if(cscpTenant.getExpireTime().compareTo(ZonedDateTime.now())<0){
            return TenantStatusConstant.TENANT_EXPIRED;
        }

        if(cscpTenantRepository.checkUserTenant(tenantAccount,username) < 1){
            return TenantStatusConstant.USER_IN_TENANT_NOT_EXIST;
        }
        return TenantStatusConstant.TENANT_EXIST;
    }

    @Override
    public CscpTenant selectByTenantAccount(String tenantAccount) {
        return cscpTenantRepository.selectByTenantAccount(tenantAccount);
    }

    /**
     * 根据租户id删除租户
     * @param tenantId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTenantById(Long tenantId) {
        // 查询租户信息
        CscpTenant tenant = cscpTenantRepository.selectByPrimaryKey(tenantId);
        if (tenant == null) {
            throw new BadRequestAlertException("删除失败：该租户不存在", "CscpTenantServiceImpl.deleteTenantById", "tenant not exist");
        }
        if (TenantStatusConstant.ACTIVE_FLAG.equalsIgnoreCase(tenant.getActiveFlag())) {
            throw new BadRequestAlertException("删除失败：该租户已激活使用中", "CscpTenantServiceImpl.deleteTenantById", "tenant has been actived");
        }
        try {
            // 查询租户下所有用户
            List<Long> delUserIds = new ArrayList<>();
            List<CscpUserDetailDTO> userDetailDtos = cscpUserDetailRepository.selectByTenantId(tenantId);
            for (CscpUserDetailDTO userDetailDTO : userDetailDtos) {
                Long userId = userDetailDTO.getUserId();
                delUserIds.add(userId);
            }
            //根据用户id批量删除用户及用户信息表；
            cscpUserDetailRepository.deleteByUserIds(delUserIds);
            cscpUserRepository.deleteByIds(delUserIds);

            //查询租户下所有部门
            List<Long> deptIds = cscpDeptRepository.selectByTenantId(tenantId);
            // 批量删除部门
            cscpDeptRepository.deleteByIds(deptIds);
            // 批量删除部门角色
            cscpRoleDeptRepository.deleteByDeptIds(deptIds);

            //查询租户下所有角色
            List<Long> roleIds = cscpRolesRepository.selectRolesByTenantId(tenantId);
            //批量删除用户角色关联
            cscpUserRoleRepository.deleteByRoleIds(roleIds);

            //批量删除菜单角色关联表
            cscpRoleMenuRepository.deleteMenusByRoleIds(roleIds);

            //批量删除角色表
            cscpRolesRepository.deleteRolesByRoleIds(roleIds);

            //删除租户信息
            cscpTenantRepository.deleteByPrimaryKey(tenantId);
        }catch (Exception e){
            e.printStackTrace();
           throw new BadRequestAlertException("删除租户失败","CscpTenantServiceImpl.deleteTenantById","");
        }
    }

    /**
     * 批量删除租户
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        ArrayList<Long> emptyList = new ArrayList<>();
        ArrayList<String> activeList = new ArrayList<>();
        for (int i = 0; i < delList.size(); i++) {
            // 查询租户信息
            CscpTenant tenant = cscpTenantRepository.selectByPrimaryKey(delList.get(i));
            if (tenant == null) {
                emptyList.add(delList.get(i));
            }else{
                if (tenant.getActiveFlag() != null && TenantStatusConstant.ACTIVE_FLAG.equalsIgnoreCase(tenant.getActiveFlag())) {
                    activeList.add(tenant.getTenantAccount());
                }
            }


        }
        if (!emptyList.isEmpty()){
            throw new BadRequestAlertException("批量删除失败：选中的租户不存在", "CscpTenantServiceImpl.deleteTenantById", "tenant not exist");
        }
        if (!activeList.isEmpty()){
            throw new BadRequestAlertException("批量删除失败：租户: "+activeList.toString() +" 已激活使用中", "CscpTenantServiceImpl.deleteTenantById", "tenant has been actived");
        }
        try {
            delList = (List<Long>) removeAll(delList, activeList);
            delList =   (List<Long>) removeAll(delList, emptyList);
            if (delList.isEmpty()){
                throw new BadRequestAlertException("批量删除租户失败","CscpTenantServiceImpl.deleteTenantById","");
            }
            // 满足删除条件的租户id

//            delList.toArray();
            // 查询租户下所有用户

            List<Long> userIds = cscpUserDetailRepository.selectUserIdsByTenantIds(delList);

            //根据用户id批量删除用户及用户信息表；
            cscpUserDetailRepository.deleteByUserIds(userIds);
            cscpUserRepository.deleteByIds(userIds);

            //查询租户下所有部门
            List<Long> deptIds = cscpDeptRepository.selectDeptIdsByTenantIds(delList);
            // 批量删除部门
            cscpDeptRepository.deleteByIds(deptIds);
            // 批量删除部门角色
            cscpRoleDeptRepository.deleteByDeptIds(deptIds);

            //查询租户下所有角色
            List<Long> roleIds  =  cscpRolesRepository.selectRolesByTenantIds(delList);
            //批量删除用户角色关联
            cscpUserRoleRepository.deleteByRoleIds(roleIds);
            //批量删除菜单角色关联表
            cscpRoleMenuRepository.deleteMenusByRoleIds(roleIds);

            //批量删除角色
            cscpRolesRepository.deleteRolesByRoleIds(roleIds);

            // 批量删除租户
            cscpTenantRepository.deleteByIds(delList);
        } catch (BadRequestAlertException e) {
            e.printStackTrace();
            throw new BadRequestAlertException("批量删除异常", null, "tenantDelError");
        }
    }

    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTenant(CscpTenant tenant) {
        try {
            //新建租户
            if(checkTenantAccountExist(tenant)){
                throw new BadRequestAlertException("新增失败，租户账号已经存在","","");
            }
            // 创建租户信息
            insert(tenant);

            //新建部门
            CscpDept cscpDept = new CscpDept();
            cscpDept.setDeptName(tenant.getTenantName());
            cscpDept.setTenantId(tenant.getId());
            cscpDept.setParentId(0L);
            cscpDept.setCreateBy(SecurityHxUtils.getCurrentUserName());
            cscpDept.setCreateTime(ZonedDateTime.now());
            cscpDept.setAncestors(",0,");
            cscpDept.setOrderNum(0);
            cscpDept.setLeader(tenant.getContacts());
            cscpDept.setPhone(tenant.getPhone());
            cscpDept.setStatus("0");
            cscpDeptService.insert(cscpDept);

            //新增用户
            CscpUserDetailDTO cscpUserDetailDTO = new CscpUserDetailDTO();
            cscpUserDetailDTO.setUsername("admin");
            cscpUserDetailDTO.setAdminFlag(AdminFlagConstant.COMMON_ADMIN_USER);
            cscpUserDetailDTO.setDeptId(cscpDept.getDeptId());
            cscpUserDetailDTO.setTenantId(tenant.getId());
            cscpUserDetailDTO.setFamilyName(tenant.getTenantAccount());
            cscpUserDetailDTO.setName("admin");
            cscpUserDetailService.insert(cscpUserDetailDTO);

            //新增角色
            CscpRolesDTO cscpRolesDTO = new CscpRolesDTO();
            cscpRolesDTO.setIcon("md-key");
            cscpRolesDTO.setTenantId(tenant.getId());
            cscpRolesDTO.setName("admin");
            CscpRolesDTO rolesDTO = cscpRolesService.insert(cscpRolesDTO);

            //新增用户角色关联
            CscpUserRoleDTO cscpUserRoleDTO = new CscpUserRoleDTO();
            cscpUserRoleDTO.setRoleId(rolesDTO.getId());
            cscpUserRoleDTO.setUserId(cscpUserDetailDTO.getUserId());
            cscpUserRoleService.insert(cscpUserRoleDTO);

            //新增角色菜单关联
            cscpMenusService.saveRoleMenus(rolesDTO.getId(), tenant.getMenus(), tenant.getPermissions());


        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestAlertException("租户新增异常", null, "tenantInsertError");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenant(CscpTenant tenant) {
        try {
            CscpTenant cscpTenant = cscpTenantRepository.selectByPrimaryKey(tenant.getId());
            if(!cscpTenant.getTenantAccount().equals(tenant.getTenantAccount())){
                if(checkTenantAccountExist(tenant)){
                    throw new BadRequestAlertException("更新失败，租户账号已经存在","","");
                }
            }
            update(tenant);
            //获取租户下管理员的角色
            CscpRoles roles = new CscpRoles();
            roles.setName("admin");
            roles.setTenantId(tenant.getId());
            CscpRoles roleByTenant = cscpRolesService.getRoleByTenant(roles);
            if(roleByTenant != null){
                //新增角色菜单关联,并删除旧菜单
                cscpMenusService.saveRoleMenus(roleByTenant.getId(),tenant.getMenus(),tenant.getPermissions());
            }else{
                log.error("获取角色异常");
                throw new BadRequestAlertException("获取角色异常", null, "tenantError");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestAlertException("更新租户异常", null, "tenantDelError");
        }

    }

    @Override
    public boolean checkTenantAccountExist(CscpTenant tenant) {
        int i = cscpTenantRepository.checkTenantAccountExist(tenant);
        if(i == 0){
            return false;
        }
        return true;
    }

    @Override
    public List<CscpMenusDTO> getCscpTenantMenu(Long tenantId) {
        CscpUserDetailDTO cscpUserDetailDTO = cscpUserDetailRepository.finUserByUsernameAndTenantId(tenantId, "admin");
        List<CscpMenusDTO> cscpMenusList = cscpMenusService.findByUserId(cscpUserDetailDTO.getUserId());
        List<CscpMenusDTO> cscpTenantMenuList = new ArrayList<>();

        for (CscpMenusDTO tenantMenu : cscpMenusList) {
            boolean flag = true;
            List<CscpMenus> byParentId = cscpMenusService.findByParentId(tenantMenu.getId());
            for (CscpMenus tenantMenu1 : byParentId){
                if(tenantMenu1.getParentId().equals(tenantMenu.getId())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                cscpTenantMenuList.add(tenantMenu);
            }
        }
        return cscpTenantMenuList;
    }

    /**
     * 激活或者停用租户
     * @param tenant
     * @return
     */
    @Override
    public CscpTenant changeStatus(CscpTenant tenant) {
        String flag = tenant.getActiveFlag();
        Long tenantId = tenant.getId();
        cscpTenantRepository.changeStatus(tenantId,flag);
        return tenant;
    }

    @Override
    public List<CscpTenant> getAll(String name) {
        List<CscpTenant> all = cscpTenantRepository.getAll(name);
        return all;
    }

    @Override
    public List<CscpTenant> selectByUsername(String username) {
        List<CscpTenant> cscpTenants = cscpTenantRepository.selectByUsername(username);
        return cscpTenants;
    }

    /**
     * 去除大集合中的小集合
     * @param collection
     * @param remove
     * @return
     */
    public  List removeAll(Collection collection, Collection remove) {
        List list = new ArrayList();
        Iterator iter = collection.iterator();

        while(iter.hasNext()) {
            Object obj = iter.next();
            if (!remove.contains(obj)) {
                list.add(obj);
            }
        }

        return list;
    }

}
