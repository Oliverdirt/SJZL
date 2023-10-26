package com.ctsi.ssdc.annotation;

import java.lang.annotation.*;

/**
 * 表单重复提交注解
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-15 14:32
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit
{
    /**
     * 提示消息
     */
    public String message() default "不允许重复提交，请稍后再试";
}
