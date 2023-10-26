package com.ctsi.ssdc.annocation;

import com.ctsi.ssdc.process.BaseAutoIdProcess;
import com.ctsi.ssdc.process.SnowFlakeAutoIdProcess;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明主键生成类型
 * @author zhaoliangliang
 * @date 2021/9/9
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoId {
    /**
     * 主键名
     * @return
     */
    String primaryKey();

    /**
     * 主键生成器
     * @return
     */
    Class<? extends BaseAutoIdProcess> type() default SnowFlakeAutoIdProcess.class;

}