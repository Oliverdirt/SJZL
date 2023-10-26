package com.ctsi.flow.param.response;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Description: 热门流程
 * @Author: sunsheng
 **/
public class FlowModelHotProcResp {

    private PageInfo<FlowHotProc> flowHotProcList;

    public PageInfo<FlowHotProc> getFlowHotProcList() {
        return flowHotProcList;
    }

    public void setFlowHotProcList(PageInfo<FlowHotProc> flowHotProcList) {
        this.flowHotProcList = flowHotProcList;
    }
}
