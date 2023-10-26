package com.ctsi.flow.param.request;

import com.ctsi.flow.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

/**
 * @Description: 热门流程信息查询请求参数
 * @Author: sunsheng
 **/
public class FlowModelHotProcReq {
    /**
     * 分页参数
     **/
    PageParam pageParam;

    /**
     * 开始时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    ZonedDateTime startTime;

    /**
     * 结束时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    ZonedDateTime endTime;

    public PageParam getPageParam() {
        return pageParam;
    }

    public void setPageParam(PageParam pageParam) {
        this.pageParam = pageParam;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
