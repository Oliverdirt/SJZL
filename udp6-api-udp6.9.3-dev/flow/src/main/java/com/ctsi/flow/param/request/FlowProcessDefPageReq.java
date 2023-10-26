package com.ctsi.flow.param.request;

import com.ctsi.flow.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 17:04
 * @description ：
 */


@ApiModel("管理后台 - 流程定义分页 Request VO")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlowProcessDefPageReq extends PageParam {

    @ApiModelProperty(value = "标识", example = "process1641042089407", notes = "精准匹配")
    private String key;
}
