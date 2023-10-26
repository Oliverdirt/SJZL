package com.ctsi.ssdc.admin.service;

import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.model.MenuItemBean;
import com.ctsi.ssdc.model.TUserDetail;

import java.util.Optional;

//@Deprecated
public interface UserService {
	
    public Optional<CscpUser> findByUserName(String userName);
    //public List findUserRole(String userId) throws Exception;
    public void updateUserDetailForLogin(String userId);
    public String insertUserInfo(String userName, String password);
    public void updateUserDetail(TUserDetail tUserDetail);
    public MenuItemBean findUserMenuItems(String userId);
    public String saveUserRoles(String userId, String roles);
    
    public TUserDetail findUserDetailByUserId(String userId);
    public void insertUserDetail(TUserDetail detail);
    
    public CscpUser findByUserId(Long userId) throws Exception;
    
    public void updateUser(CscpUser user) throws Exception;
    
    public String findUserDisplayName(String userId) throws Exception ;
    public String findUserDisplayNameByUsername(String userName) throws Exception ;

    public Optional<CscpUserDetailDTO> finUserByUsernameAndTenantId(Long tenantId, String username);
    
}
