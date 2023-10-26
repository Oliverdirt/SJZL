package com.ctsi.ssdc.quartz.consts;

/**
 * 任务调度通用常量
 *
 */

public class ScheduleConstants {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 默认 */
    public static final String MISFIRE_DEFAULT = "-1";

    /** 立即触发执行 */
    public static final String MISFIRE_IGNORE_MISFIRES = "0";

    /** 触发一次执行 */
    public static final String MISFIRE_FIRE_AND_PROCEED = "1";

    /** 不触发立即执行 */
    public static final String MISFIRE_DO_NOTHING = "2";

    /**
     * 执行状态：成功
     */
    public static final String SUCCESS_STATUS = "1";

    /**
     * 执行状态:失败或异常
     */
    public static final String FAIL_STATUS = "0";

    public enum Status
    {
        /**
         * 正常
         */
        NORMAL("1"),
        /**
         * 暂停
         */
        PAUSE("2");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
