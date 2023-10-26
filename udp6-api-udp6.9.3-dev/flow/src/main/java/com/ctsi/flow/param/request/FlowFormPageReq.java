package com.ctsi.flow.param.request;

import com.ctsi.flow.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 19:06
 * @description ：
 */


@ApiModel("管理后台 - 动态表单分页项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowFormPageReq extends PageParam {

    @ApiModelProperty(value = "表单名称", example = "请假")
    private String name;
}
