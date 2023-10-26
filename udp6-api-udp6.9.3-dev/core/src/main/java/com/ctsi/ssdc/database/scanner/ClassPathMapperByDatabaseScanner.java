package com.ctsi.ssdc.database.scanner;

import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @InjectByDataBaseType 注解扫描类
 * @author fuxiang
 */
public class ClassPathMapperByDatabaseScanner {

	public Set<Class<?>> getInjectByDatabaseTypeAnnotation(List<String> basePackages) {
		Set<Class<?>> classes = new HashSet<>();
		for (String basePackage : basePackages) {
			Reflections reflections = new Reflections(basePackage);
			Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(InjectByDataBaseType.class,true);
			classes.addAll(classesList);
		}
		return classes;
	}

}