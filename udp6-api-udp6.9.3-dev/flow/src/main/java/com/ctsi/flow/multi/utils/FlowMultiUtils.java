package com.ctsi.flow.multi.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;


/**
 * @author gyp
 */
public class FlowMultiUtils {
    private static final BeanUtil BEAN_UTIL = new BeanUtil();

    private static final SpringApplicationContextUtil CONTEXT_UTIL = new SpringApplicationContextUtil();

    /**
     * Bean处理
     *
     * @return
     */
    public static BeanUtil bean() {
        return BEAN_UTIL;
    }


    /**
     * 获取spring上下文
     *
     * @return
     */
    public static SpringApplicationContextUtil springContext() {
        return CONTEXT_UTIL;
    }


    /**
     * 判断是为：null、""、length == 0
     *
     * @param value 值
     * @return
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return "".equals(String.valueOf(value).trim());
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() == 0;
        }
        if (value instanceof Map) {
            return ((Map) value).size() == 0;
        }
        if (value.getClass().isArray()) {
            Object[] v = (Object[]) value;
            return Stream.of(v).allMatch(ov -> ov == null);
        }
        return false;
    }

    /**
     * 判断是否不为空
     *
     * @param value 值
     * @return
     */
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    public static <T> T toBean(Class<T> cls, Object obj) {
        ConvertUtils.register(new DateConverter(null), Date.class);
        T dest = null;
        try {
            dest = cls.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        if (dest == null) {
            return null;
        }

        BeanUtils.copyProperties(dest, obj);

        return dest;
    }

    public static JSONArray toJsonArray(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return JSONArray.parseArray(obj instanceof String ? (String) obj : JSON.toJSONString(obj));
    }

    public static JSONObject toJsonObject(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return JSONObject.parseObject(obj instanceof String ? (String) obj : JSON.toJSONString(obj));
    }

    /**
     * 将一个字符串的首字母改为大写或者小写
     *
     * @param srcString 源字符串
     * @param flag      大小写标识，ture小写，false大些
     * @return 改写后的新字符串
     */
    public static String toLowerCaseInitial(String srcString, boolean flag) {
        StringBuilder sb = new StringBuilder();
        if (flag) {
            sb.append(Character.toLowerCase(srcString.charAt(0)));
        } else {
            sb.append(Character.toUpperCase(srcString.charAt(0)));
        }
        sb.append(srcString.substring(1));
        return sb.toString();
    }


}
