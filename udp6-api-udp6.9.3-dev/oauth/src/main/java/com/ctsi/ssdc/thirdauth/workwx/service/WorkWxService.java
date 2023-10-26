package com.ctsi.ssdc.thirdauth.workwx.service;

import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxDepartment;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxLogin;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxUserInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface WorkWxService {

    public String getAccessToken();
    public String getUserId(String code);

    WorkWxUserInfo getUserInfo(String userId);

    WorkWxLogin loginQrCode() throws UnsupportedEncodingException;

    CscpUserDetailDTO oauth2Login(String code);

    List<WorkWxUserInfo> getAllUser(List<WorkWxDepartment> departmentList);

    List<WorkWxDepartment> getDepartment(Long id);

    void syncDept(List<WorkWxDepartment> departmentList);
    void syncUser(List<WorkWxUserInfo> user);
}
