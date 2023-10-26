package com.ctsi.flow.param.request;

import com.ctsi.flow.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 17:05
 * @description ：
 */

@ApiModel("管理后台 - 流程定义列表项")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlowProcessDefListReq extends PageParam {

    @ApiModelProperty(value = "中断状态", example = "1", notes = "参见 SuspensionState 枚举")
    private Integer suspensionState;
}
