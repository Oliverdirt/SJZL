package com.ctsi.liteflow.vo;

import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.Data;

/**
 * @Description:
 * @Author: sunsheng
 **/
@Data
public class LiteflowCompMethodRes {
    private String nodeId;

    private NodeTypeEnum nodeType;

    private LiteFlowMethodEnum value;
}
