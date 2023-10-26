package com.ctsi.flow.param.request;

import com.ctsi.flow.param.FlowFormBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 14:58
 * @description ：
 */

@ApiModel("管理后台 - 动态表单创建项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowFormCreateReq extends FlowFormBase {

    @ApiModelProperty(value = "表单的配置", required = true, notes = "JSON 字符串")
    @NotNull(message = "表单的配置不能为空")
    private String conf;

    @ApiModelProperty(value = "表单项的数组", required = true, notes = "JSON 字符串的数组")
    @NotNull(message = "表单项的数组不能为空")
    private List<String> fields;
}
