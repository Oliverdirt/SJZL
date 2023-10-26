package com.ctsi.flow.param.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-05 18:33
 * @description ：
 */

@ApiModel("流程任务一周趋势图返参")
@Data
public class FlowTrendMessageResp {

    @ApiModelProperty(value = "日期列表")
    private List<String> dateList;

    @ApiModelProperty(value = "发起流程数列表")
    private List<Long> procInstanceStartList;

    @ApiModelProperty(value = "结束流程数列表")
    private List<Long> procInstanceEndList;

    @ApiModelProperty(value = "发起流程数列表")
    private List<Long> taskStartList;

    @ApiModelProperty(value = "结束流程数列表")
    private List<Long> taskEndList;
}
