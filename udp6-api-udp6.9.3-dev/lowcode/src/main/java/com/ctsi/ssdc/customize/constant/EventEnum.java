package com.ctsi.ssdc.customize.constant;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/10/17 10:23  by xx
 */
public enum  EventEnum {
    /**
     * 按钮绑定事件枚举
     */
    EVENT1("111","页面跳转"),EVENT2("112","自定义接口"),
    EVENT3("113","列表查询"),EVENT4("114","列表删除"),
    EVENT5("115","表单保存"),EVENT6("116","关闭页面"),
    EVENT7("117","关闭弹框");
    public String code;
    public String message;

    EventEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
