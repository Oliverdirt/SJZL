package com.ctsi.ssdc.process;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * @author zhaoliangliang
 * @date 2021/9/9
 */
public abstract class BaseAutoIdProcess {

    /**
     * 主键字段
     */
    protected Field field;

    /**
     * 主键名
     */
    protected String primaryKey;

    public BaseAutoIdProcess(Field field){
        this.field = field;
    }

    abstract void setFieldValue(Object entity) throws Exception;

    /**
     * 判断id字段是否已经有值，如果不存在，则需要进行设值
     * @param entity
     * @return
     */
    private boolean isNotExistFieldValue(Object entity) throws Exception {
        //允许反射时访问私有变量
        field.setAccessible(true);
        Object value = field.get(entity);
        return ObjectUtils.isEmpty(value);
    }


    /**
     * 设置主键ID
     * @param entity
     * @throws Exception
     */
    public void process(Object entity) throws Exception{
        if(isNotExistFieldValue(entity)){
            setFieldValue(entity);
        }
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
