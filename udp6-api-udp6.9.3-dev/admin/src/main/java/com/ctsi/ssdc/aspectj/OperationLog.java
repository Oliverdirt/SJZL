package com.ctsi.ssdc.aspectj;

import java.lang.annotation.*;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-02-23 14:24
 * @description ：controller aspectj  log
 * @version ：
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块
     */
    String operModul() default "";

    /**
     * 操作类型
     */
    String operType() default "";

    /**
     * 操作描述
     */
    String operDesc() default "";
}
