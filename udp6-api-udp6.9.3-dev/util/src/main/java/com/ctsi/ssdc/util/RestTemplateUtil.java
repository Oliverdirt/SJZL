package com.ctsi.ssdc.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RestTemplate调用接口
 * @author ctsi
 * 
 */
public class RestTemplateUtil {
    private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    private static RestTemplate getRestTemplate() {
        RestTemplate rt;
        List<HttpMessageConverter<?>> lstConverters;
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        lstConverters = new ArrayList<HttpMessageConverter<?>>();
        lstConverters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        lstConverters.add(converter);

        rt = new RestTemplate();
        rt.setMessageConverters(lstConverters);

        return rt;
    }
    /**
     * 利用RestTemplate封装GET调用，提供类似于本地方法的调用接口
     * @param url 接口
     * @param responseType 响应类型
     * @return 结果
     */
    

    public static <T> T get(String url, Class<T> responseType) {
        return get(url, responseType, null);
    }
    /**
     * 带url参数的GET调用
     * @param url 接口
     * @param responseType 响应类型
     * @param urlVariables 请求参数
     * @return 结果
     */
    

    public static <T> T get(String url, Class<T> responseType,  Map<String, ?> urlVariables) {
        T result;

        logger.info("[rest get] {}", url);
        logger.info("[responseType] {}", responseType);

        if (urlVariables == null) {
            result = getRestTemplate().getForObject(url, responseType);
        } else {
            for (Map.Entry entry : urlVariables.entrySet()) {
                logger.info("[params] {} = {}", entry.getKey(), entry.getValue());
            }

            result = getRestTemplate().getForObject(url, responseType, urlVariables);
        }

        logger.info("[result] {}", result);

        return result;
    }
    /**
     * 利用RestTemplate封装POST调用，提供类似于本地方法的调用接口
     * @param url 接口
     * @param request 请求
     * @param responseType 响应类型
     * @return 结果
     */
    

    public static <T> T post(String url, Object request, Class<T> responseType) {
        return post(url, request, responseType, null);
    }
    /**
     * 带url参数的POST调用
     * @param url 接口
     * @param request 请求
     * @param responseType 响应类型
     * @param urlVariables 参数
     * @return 结果
     */
    

    public static <T> T post(String url, Object request, Class<T> responseType, Map<String, ?> urlVariables) {
        T result;

        logger.info("[rest post] {}", url);
        logger.info("[request] {}", request);
        logger.info("[responseType] {}", responseType);

        if (urlVariables == null) {
            result = getRestTemplate().postForObject(url, request, responseType);
        } else {
            for (Map.Entry entry : urlVariables.entrySet()) {
                logger.info("[params] {} = {}", entry.getKey(), entry.getValue());
            }

            result = getRestTemplate().postForObject(url, request, responseType, urlVariables);
        }

        logger.info("[result] {}", result);

        return result;
    }

}
