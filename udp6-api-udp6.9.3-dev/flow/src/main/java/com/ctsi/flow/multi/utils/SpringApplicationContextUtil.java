package com.ctsi.flow.multi.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * @author gyp
 */
@Component
public class SpringApplicationContextUtil implements ApplicationContextAware {

    protected SpringApplicationContextUtil() {
    }

    private static ApplicationContext applicationContext = null;

    @Override
    public synchronized void setApplicationContext(ApplicationContext context) throws BeansException {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    /**
     * 获取applicationContext
     */
    public synchronized ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     */
    public Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    public Object getBean(String name, Class<?> newInstanceImpl) {
        Object t = getBean(name);
        if (t == null) {
            try {
                t = newInstanceImpl.newInstance();
            } catch (Exception e) {
            }
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> clazz) {
        T t = null;
        try {
            String simpleName = FlowMultiUtils.toLowerCaseInitial(clazz.getSimpleName(), true);
            t = ((T) getApplicationContext().getBean(simpleName));
        } catch (Exception e) {
            try {
                t = ((T) getApplicationContext().getBean(clazz));
            } catch (Exception e2) {
                return null;
            }
        }
        return t;
    }

    public <T> T getBean(Class<?> clazz, Class<?> newInstanceImpl) {
        T t = getBean(clazz);
        if (t == null) {
            try {
                t = (T) newInstanceImpl.newInstance();
            } catch (Exception ignored) {
            }
        }
        return t;
    }

}
