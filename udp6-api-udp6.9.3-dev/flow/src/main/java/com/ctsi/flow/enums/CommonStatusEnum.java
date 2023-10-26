package com.ctsi.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 15:03
 * @description ：通用枚举状态
 */

@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    /**
     * 开启状态
     */
    ENABLE(0, "开启"),
    /**
     * 关闭状态
     */
    DISABLE(1, "关闭");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;
}
