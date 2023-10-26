package com.ctsi.ssdc.model;

import com.google.common.collect.Lists;

import java.util.List;


public class CheckResult {
    //满足规则个数
    int confirmCount;

    //校验失败原因
    List<String> errorReason = Lists.newArrayList();

    //满足规则记录
    List<String> confirmInfo = Lists.newArrayList();

    public List<String> getConfirmInfo() {
        return confirmInfo;
    }

    public void setConfirmInfo(List<String> confirmInfo) {
        this.confirmInfo = confirmInfo;
    }

    public int getConfirmCount() {
        return confirmCount;
    }

    public void setConfirmCount(int confirmCount) {
        this.confirmCount = confirmCount;
    }

    public List<String> getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(List<String> errorReason) {
        this.errorReason = errorReason;
    }
}
