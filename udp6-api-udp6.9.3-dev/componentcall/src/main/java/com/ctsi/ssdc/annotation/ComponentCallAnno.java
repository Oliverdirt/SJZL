package com.ctsi.ssdc.annotation;

import java.lang.annotation.*;

/**
 * 组件接口调用注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentCallAnno {
    String component() default "";

    String method() default "";
}
