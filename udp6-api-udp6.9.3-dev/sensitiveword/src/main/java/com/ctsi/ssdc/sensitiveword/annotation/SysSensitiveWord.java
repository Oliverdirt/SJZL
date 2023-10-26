package com.ctsi.ssdc.sensitiveword.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 敏感词 方法级注解
 *
 * @author hx
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysSensitiveWord
{
	boolean filter() default true;
}
