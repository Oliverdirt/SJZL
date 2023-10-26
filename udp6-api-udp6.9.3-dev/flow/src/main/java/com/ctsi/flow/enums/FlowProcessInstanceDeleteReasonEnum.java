package com.ctsi.flow.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程实例的删除原因
 */
@Getter
@AllArgsConstructor
public enum FlowProcessInstanceDeleteReasonEnum {
    // 不通过任务
    REJECT_TASK("不通过任务，原因：{}"), // 修改文案时，需要注意 isRejectReason 方法
    // 取消任务
    CANCEL_TASK("主动取消任务，原因：{}");

    private final String reason;

    /**
     * 格式化理由
     *
     * @param args 参数
     * @return 理由
     */
    public String format(Object... args) {
        return StrUtil.format(reason, args);
    }

    // ========== 逻辑 ==========

    public static boolean isRejectReason(String reason) {
        return StrUtil.startWith(reason, "不通过任务，原因：");
    }

}
