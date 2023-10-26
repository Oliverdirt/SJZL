package com.ctsi.ssdc.admin.service;


import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.CscpTenantExample;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Service Interface for managing CscpTenant.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpTenantService 
	extends StrengthenBaseService<CscpTenant, Long, CscpTenantExample>{


    int checkTenantAccount(String tenantAccount,String username);

    CscpTenant selectByTenantAccount(String tenantAccount);

    /**
     * 根据租户id删除租户
     * @param tenantId
     */
    void deleteTenantById(Long tenantId);

    /**
     * 批量删除
     * @param ids
     */
    void deleteByIds(Long[] ids);

    /**
     * 插入
     * @param tenant
     */
    void insertTenant(CscpTenant tenant);


    /**
     * 修改
     * @param tenant
     */
    void updateTenant(CscpTenant tenant);

    /**
     * 判断租户账号 是否唯一性
     * @param tenant
     * @return
     */
    boolean checkTenantAccountExist(CscpTenant tenant);
    List<CscpMenusDTO> getCscpTenantMenu(Long tenantId);

    /**
     * 激活或者停用租户
     * @param tenant
     * @return
     */
    CscpTenant changeStatus(CscpTenant tenant);

    /***
     * 获取所有租户
     *
     *
     */
    List<CscpTenant> getAll (String name);

    List<CscpTenant> selectByUsername (String username);
}
