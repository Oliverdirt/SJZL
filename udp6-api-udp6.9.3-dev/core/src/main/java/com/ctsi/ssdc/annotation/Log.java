package com.ctsi.ssdc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author lym
 * log by custom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,})
public @interface Log {
	String message() default "";
}
