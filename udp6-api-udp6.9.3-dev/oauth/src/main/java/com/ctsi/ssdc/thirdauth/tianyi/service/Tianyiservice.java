package com.ctsi.ssdc.thirdauth.tianyi.service;

import com.ctsi.ssdc.admin.domain.CscpUserDetail;

public interface Tianyiservice {
     String getUnifyAccountLoginUrl(String state,String url);
     String getUserInfo(String accessToken);
     CscpUserDetail getUserInfoByMobile(String mobile,String email);

}
