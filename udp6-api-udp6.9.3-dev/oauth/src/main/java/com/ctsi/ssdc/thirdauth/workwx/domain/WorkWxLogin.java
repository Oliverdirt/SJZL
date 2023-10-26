package com.ctsi.ssdc.thirdauth.workwx.domain;

import lombok.Data;

@Data
public class WorkWxLogin {

    private String appid;

    private String agentid;

    private String redirectUri;
}
