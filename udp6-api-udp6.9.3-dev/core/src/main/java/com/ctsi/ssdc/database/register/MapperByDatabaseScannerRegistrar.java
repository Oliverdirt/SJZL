package com.ctsi.ssdc.database.register;

import com.ctsi.ssdc.database.annotation.MapperByDataBaseScan;
import com.ctsi.ssdc.database.scanner.ClassPathMapperByDatabaseScanner;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 多数据库注解扫描、代理mapper注册类
 * 
 * @author fuxiang
 *
 */
public class MapperByDatabaseScannerRegistrar
		implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
	private final Logger log = LoggerFactory.getLogger(MapperByDatabaseScannerRegistrar.class);

	private ResourceLoader resourceLoader;

	private Environment environment;

	@SneakyThrows(value = Exception.class)
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes annoAttrs = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(MapperByDataBaseScan.class.getName()));
		List<String> basePackages = new ArrayList<String>();
		for (String pkg : annoAttrs.getStringArray("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}

		ClassPathMapperByDatabaseScanner scanner = new ClassPathMapperByDatabaseScanner();
		Set<Class<?>> candidates = scanner.getInjectByDatabaseTypeAnnotation(basePackages);
		InjectByDataBaseTypeRegister injectByDataBaseTypeRegister = new InjectByDataBaseTypeRegister();
		for (Class<?> clazz : candidates) {
			try {
				injectByDataBaseTypeRegister.injectByDataBaseType(clazz, registry, environment);
			} catch (ClassNotFoundException | SQLException e) {
				log.error("", e);
			}
		}

	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
