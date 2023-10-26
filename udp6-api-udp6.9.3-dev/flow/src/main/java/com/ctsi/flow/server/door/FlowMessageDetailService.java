package com.ctsi.flow.server.door;

import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.request.FlowModelHotProcReq;
import com.ctsi.flow.param.response.FlowMessageResp;
import com.ctsi.flow.param.response.FlowPropMessageResp;
import com.ctsi.flow.param.response.FlowTrendMessageResp;
import com.ctsi.flow.param.response.FlowModelHotProcResp;

import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-02 16:15
 * @description ：
 */
public interface FlowMessageDetailService {

    /**
     * 查询概览信息
     */
    FlowMessageResp queryMessage(PageParam pageParam);

    /**
     * 查询占比信息
     */
    FlowPropMessageResp queryPropMessage();

    /**
     * 查询一周趋势图
     */
    FlowTrendMessageResp queryTrendMessage();

    /**
     * 查询热门流程信息
     */
    FlowModelHotProcResp queryHotProcMessage(FlowModelHotProcReq flowModelHotProcReq);
}
