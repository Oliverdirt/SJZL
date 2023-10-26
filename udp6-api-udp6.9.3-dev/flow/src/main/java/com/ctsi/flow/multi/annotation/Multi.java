package com.ctsi.flow.multi.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gyp
 */
@Target({ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Multi {
    /*
    * 名称
    * */
    String name() default "";

    /*
    * 序号
    * */
    int sort() default 10;

    /*
    * 注销
    * */
    boolean off() default false;
}
