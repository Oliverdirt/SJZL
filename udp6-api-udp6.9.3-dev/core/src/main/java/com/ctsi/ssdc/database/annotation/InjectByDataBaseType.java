package com.ctsi.ssdc.database.annotation;

import com.ctsi.ssdc.database.enums.EnumDatabaseName;

import java.lang.annotation.*;

/**
 * 根据数据库类型自动注入Mapper注解
 * 
 * 说明：该注解会根据DataSource中数据库的类型自动注入所需的数据库子类实现，
 * 需要子类实现的repository需在includes中声明，子类实现必须放在当前目录下以数据库名小写命名的包中， 可参考EnumDatabaseName
 * 
 * @author fuxiang
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectByDataBaseType {

	/**
	 * 使用指定数据库类型的子类实现
	 * 
	 * @return
	 * @return String[]
	 * @exception @createTime：Mar
	 *                29, 2019
	 * @author: fuxiang
	 */
	public EnumDatabaseName[] includes() default {};

	// /**
	// * 默认当前目录下，每种数据库类型对应包名下匹配
	// *
	// * @return
	// * @return String
	// * @exception @createTime：Mar
	// * 29, 2019
	// * @author: fuxiang
	// */
	// public String packageName();

	/**
	 * 父类名与数据库名的连接符
	 * 
	 * @return
	 * @return String
	 * @exception @createTime：Mar
	 *                29, 2019
	 * @author: fuxiang
	 */
	public String connector() default "For";

}
