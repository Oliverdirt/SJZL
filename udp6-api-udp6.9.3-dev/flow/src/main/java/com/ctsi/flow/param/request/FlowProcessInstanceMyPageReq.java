package com.ctsi.flow.param.request;

import com.ctsi.flow.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:09
 * @description ：
 */

@ApiModel("管理后台 - 流程实例的分页 Item Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowProcessInstanceMyPageReq extends PageParam {

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "流程定义的编号", example = "2048")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程实例的状态",  example = "1")
    private Integer status;

    @ApiModelProperty(value = "流程实例的结果",  example = "2")
    private Integer result;

    @ApiModelProperty(value = "流程分类", notes = "参见 bpm_model_category 数据字典", example = "1")
    private String category;

    @ApiModelProperty(value = "开始的创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginCreateTime;

    @ApiModelProperty(value = "结束的创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

}
