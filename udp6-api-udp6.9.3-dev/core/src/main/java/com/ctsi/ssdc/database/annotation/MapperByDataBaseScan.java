package com.ctsi.ssdc.database.annotation;

import com.ctsi.ssdc.database.register.MapperByDatabaseScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * InjectByDataBaseType注解扫描类（参照@MapperScan）
 * 
 * @author fuxiang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperByDatabaseScannerRegistrar.class)
public @interface MapperByDataBaseScan {

	String[] basePackages() default {};

}
