package com.ctsi.ssdc.handle;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

/**
 * 数据库密码加解密
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2021-10-15 11:14
 **/
public class JasyptPropertyValueHandler extends PropertyOverrideConfigurer implements EnvironmentAware {

    private static BasicTextEncryptor textEncryptor = null;
    private final static String KEY_SEED = "jasypt.encryptor.password";
    private final static String PREFIX = "ENC(";
    private final static  String SUFFIX = ")";
//    private final static  byte[] tmp_lock = new byte[1];
    private boolean isInit;
    private String seed;
    private Environment environment;

    public JasyptPropertyValueHandler() {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MutablePropertySources propertySources = ((StandardServletEnvironment) environment).getPropertySources();
        convertPropertySources(propertySources);
        super.postProcessBeanFactory(beanFactory);
    }


    public void convertPropertySources(MutablePropertySources propSources) {
        initSeed();
        // 命令行参数SimpleCommandLinePropertySource
        // yml配置文件参数OriginTrackedMapPropertySource
        StreamSupport.stream(propSources.spliterator(), false)
                .filter(ps -> (ps instanceof OriginTrackedMapPropertySource) || (ps instanceof SimpleCommandLinePropertySource))
                .forEach(ps -> {
                    if (ps instanceof OriginTrackedMapPropertySource) {
                        handleConfigFile(ps);
                    } else if (ps instanceof SimpleCommandLinePropertySource) {
                        handleCommandLine(ps);
                    }
                    propSources.replace(ps.getName(), ps);
                });
    }
    private void handleConfigFile(PropertySource ps) {
        //处理spring boot的默认配置文件，例如application.yml或者application.properties中加载所有内容
        Map<String, OriginTrackedValue> result = (Map<String, OriginTrackedValue>) ps.getSource();
        for (Map.Entry<String, OriginTrackedValue> stringOriginTrackedValueEntry : result.entrySet()) {
            OriginTrackedValue value = stringOriginTrackedValueEntry.getValue();
            if (checkNeedProcessOverride(stringOriginTrackedValueEntry.getKey(), String.valueOf(value.getValue()))) {
                System.out.println(value);
                String decryptedValue = decryptValue(seed, String.valueOf(value.getValue()));
                try {
                    Field valueField = OriginTrackedValue.class.getDeclaredField("value");
                    valueField.setAccessible(true);
                    valueField.set(value, decryptedValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleCommandLine(PropertySource ps) {
        //处理命令行中的替换spring boot的参数，例如--spring.datasource.password的参数形式
        try {
            Object commandLineArgs = ps.getSource();
            Field valueField = commandLineArgs.getClass().getDeclaredField("optionArgs");
            valueField.setAccessible(true);
            boolean hasEncrypt = false;
            Map<String, List<String>> result = (Map<String, List<String>>) valueField.get(commandLineArgs);
            for (Map.Entry<String, List<String>> stringListEntry : result.entrySet()) {
                List<String> values = stringListEntry.getValue();
                if (values.size() == 1) {
                    if (checkNeedProcessOverride(stringListEntry.getKey(), String.valueOf(values.get(0)))) {
                        hasEncrypt = true;
                        String decryptedValue = decryptValue(seed, String.valueOf(values.get(0)));
                        values.clear();
                        values.add(decryptedValue);
                    }
                }
            }
            if (hasEncrypt) {
                valueField.set(commandLineArgs, result);
            }


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    private boolean checkNeedProcessOverride(String key, String value) {
        if (KEY_SEED.equals(key)) {
            return false;
        }
        return StringUtils.isNotBlank(value) && value.startsWith(PREFIX) && value.endsWith(SUFFIX);
    }

    private void initSeed() {
        if (!this.isInit) {
            this.isInit = true;
            this.seed = this.environment.getProperty(KEY_SEED);
            if (StringUtils.isNotBlank(this.seed)) {
                return;
            }
            try {
                Properties properties = mergeProperties();
                //从启动命令行中，获取-Djasypt.encryptor.password的值
                this.seed = properties.getProperty(KEY_SEED);
            } catch (Exception e) {
                System.out.println("未配置加密密钥");
            }
        }
    }

    private String decryptValue(String seed, String value) {
        value = value.replace(PREFIX, "").replace(SUFFIX, "");
        value = getEncryptor(seed).decrypt(value);
        return value;
    }

    private static BasicTextEncryptor getEncryptor(String seed) {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(seed);
        return textEncryptor;
    }
}

