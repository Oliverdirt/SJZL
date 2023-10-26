package com.ctsi.ssdc.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 需要校验是否登陆的注解，可以是整个controller，也可以是某个方法 
 * 校验规则是判断session中，是否存在SESSION_USER_ID
 * 
 * @author liminqiang
 *
 */
public @interface Validate {

}
