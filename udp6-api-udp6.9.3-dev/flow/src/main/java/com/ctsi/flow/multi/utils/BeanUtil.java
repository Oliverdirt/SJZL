package com.ctsi.flow.multi.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author gyp
 */
public class BeanUtil {

    protected BeanUtil() {

    }

    /**
     * 克隆为指定对象集合
     *
     * @param <T>  泛型
     * @param cls  类型
     * @param objs 克隆对象集合
     * @return 克隆结果
     */
    public <T> List<T> toList(Class<T> cls, List<Object> objs) {
        List<T> list = new ArrayList<T>();
        if (CollectionUtils.isEmpty(objs)) {
            return Collections.emptyList();
        }
        for (Object obj : objs) {
            list.add(toBean(cls, obj));
        }
        return list;
    }

    /**
     * 克隆为指定对象集合
     *
     * @param <T>  泛型
     * @param cls  类型
     * @param maps 克隆对象集合
     * @return 克隆结果
     */
    public <T> List<T> toListByMap(Class<T> cls, List<Map<String, Object>> maps) {
        List<T> list = new ArrayList<T>();
        if (CollectionUtils.isEmpty(maps)) {
            return Collections.emptyList();
        }
        for (Map<String, Object> obj : maps) {
            list.add(toBean(cls, obj));
        }
        return list;
    }

    /**
     * 克隆为指定对象
     *
     * @param <T> 泛型
     * @param cls 类型
     * @param obj 克隆对象
     * @return 克隆结果
     */
    public <T> T toBean(Class<T> cls, Object obj) {
        ConvertUtils.register(new DateConverter(null), Date.class);
        T dest = null;
        try {
            dest = cls.newInstance();
        }  catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }
        if (dest == null) {
            return null;
        }
        BeanUtils.copyProperties(dest, obj);
        return dest;
    }

    /**
     * 复制对象
     *
     * @param src  源对象
     * @param dest 目标对象
     */
    public void copy(Object dest, Object src) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
        }
    }

    /**
     * 对象转Map
     *
     * @param obj
     * @return
     */
    public Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    public JSONObject toJsonObject(Object obj) {
        if (FlowMultiUtils.isEmpty(obj)) {
            return null;
        }

        return JSONObject.parseObject(obj instanceof String ? (String) obj : JSON.toJSONString(obj));
    }

    public JSONArray toJsonArray(Object obj) {
        if (FlowMultiUtils.isEmpty(obj)) {
            return null;
        }
        return JSONArray.parseArray(obj instanceof String ? (String) obj : JSON.toJSONString(obj));
    }

    public boolean isJsonObject(Object obj) {
        if (FlowMultiUtils.isEmpty(obj)) {
            return false;
        }
        if (obj instanceof String) {
            return JSONObject.isValidArray((String) obj);
        }

        return JSONObject.isValidObject(JSON.toJSONString(obj));
    }

    public boolean isJsonArray(Object obj) {
        if (FlowMultiUtils.isEmpty(obj)) {
            return false;
        }

        if (obj instanceof String) {
            return JSONArray.isValidArray((String) obj);
        }
        return JSONArray.isValidArray(JSON.toJSONString(obj));
    }


}
