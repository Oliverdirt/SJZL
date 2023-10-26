package com.ctsi.ssdc.database.register;

import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import com.ctsi.ssdc.database.enums.EnumDatabaseName;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * 代理mapper注册bean工厂逻辑类
 *
 * @author fuxiang
 */
public class InjectByDataBaseTypeRegister {

    private String dataBase = null;

    public void injectByDataBaseType(Class<?> currentClass, BeanDefinitionRegistry registry, Environment env)
			throws Exception {

        // 根据环境变量中的数据库信息获取的DataSource初始化数据库类型
        if (dataBase == null) {
            dataBase = getDataBase(env);
        }

        // 获取注解
        InjectByDataBaseType injectByDataBaseType = currentClass
                .getAnnotation(InjectByDataBaseType.class);

        if (injectByDataBaseType == null) {
            throw new NoUniqueBeanDefinitionException(currentClass, 2, "Repository 类名重复，请检查类名");
        }

        // 判断目前的数据库类型是否需要子类实现
        EnumDatabaseName[] includes = injectByDataBaseType.includes();
        boolean needChildClass = false;
        if (includes != null && includes.length > 0) {
            needChildClass = needChildClass(dataBase, includes);
        }

        // 获取注解中配置的子类包名，通过反射获取子类class
        Class<?> targetClass = getTargetClass(injectByDataBaseType, needChildClass, currentClass, dataBase);

        // 使用mybatis-spring中间件获取已动态代理的bean，通过spring的beanFactory实现动态注入bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(MapperFactoryBean.class);
        beanDefinitionBuilder.addPropertyReference("sqlSessionFactory", "sqlSessionFactory");
        beanDefinitionBuilder.addPropertyValue("mapperInterface", targetClass);
        String beanName = StringUtils.uncapitalize(currentClass.getSimpleName());
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

    }

    private Class<?> getTargetClass(InjectByDataBaseType injectByDataBaseType, boolean needChildClass,
                                    Class<?> currentClass, String dataBase) throws ClassNotFoundException {
        String connector = injectByDataBaseType.connector();
        String packageName = currentClass.getPackage().getName();
        String className = currentClass.getSimpleName();
        if (needChildClass) {
            className += connector + dataBase;
            packageName += "." + dataBase.toLowerCase();
        }
        String packageClassName = packageName + "." + className;
        return Class.forName(packageClassName);
    }

    private boolean needChildClass(String dataBase, EnumDatabaseName[] includes) {
        boolean needChildClass = false;
        for (EnumDatabaseName includeDataBase : includes) {
            if (dataBase.equals(includeDataBase.getDatabaseName())) {
                needChildClass = true;
            }
        }
        return needChildClass;
    }

    private static String getDataBase(Environment env) throws Exception {
        HikariConfig config = new HikariConfig();
        String encSalt = env.getProperty("jasypt.encryptor.password");
        String prefix = env.getProperty("jasypt.encryptor.property.prefix");
        String suffix = env.getProperty("jasypt.encryptor.property.suffix");
        String password = env.getProperty("spring.datasource.password");
        config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        boolean flag = checkEnc(encSalt, prefix, suffix, password);
        //判断是否是enc加密密码，如果是enc密码，需要解密成明文放在config中
        if (flag) {
            String encyptPwd = password.substring(prefix.length(), password.length() - suffix.length());
            String decyptPwd = encToPassword(encSalt, encyptPwd);
            config.setPassword(decyptPwd);
        } else {
            config.setPassword(password);
        }
        String driverClassName = env.getProperty("spring.datasource.driverClassName");
        Assert.notNull(driverClassName, "driverClassName不能为空");
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(1);
        String databaseProductName;
        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        }
        return EnumDatabaseName.getByType(databaseProductName).getDatabaseName();
    }

	/**
	 * 判断密码是enc加密密码
	 * @param encSalt
	 * @param prefix
	 * @param suffix
	 * @param password
	 * @return
	 * @throws Exception
	 */
    private static boolean checkEnc(String encSalt, String prefix, String suffix, String password) throws Exception {
        boolean flag = false;
        if (StringUtils.isNotEmpty(encSalt) && StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(suffix) && StringUtils.isNotEmpty(password)) {
            boolean start = password.startsWith(prefix);
            boolean end = password.endsWith(suffix);
            if (start && end) {
                flag = true;
            }
        }
        return flag;
    }

	/**
	 * 将password密文转为明文
	 * @param salt
	 * @param encryptPassword
	 * @return
	 * @throws Exception
	 */
    public static String encToPassword(String salt, String encryptPassword) throws Exception {
        StandardPBEStringEncryptor standardPbeStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword(salt);
        standardPbeStringEncryptor.setConfig(config);
        String encryptedText = encryptPassword;
        String plainText = standardPbeStringEncryptor.decrypt(encryptedText);
        return plainText;
    }

    public static void main(String[] args) {
        String ss = null;
        Assert.notNull(ss, "111");
    }

}
