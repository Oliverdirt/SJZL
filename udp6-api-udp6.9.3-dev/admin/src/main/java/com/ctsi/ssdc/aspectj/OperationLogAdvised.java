package com.ctsi.ssdc.aspectj;

import com.ctsi.ssdc.serializer.JacksOn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-02-23 14:29
 * @description : aspectj log
 * @version : version1.0.0
 */

@Slf4j
@Aspect
@Component
public class OperationLogAdvised {

    @Around("@annotation(com.ctsi.ssdc.aspectj.OperationLog)")
    public Object aroundLogAspect(ProceedingJoinPoint pjp) throws Throwable {


        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = AnnotatedElementUtils.getMergedAnnotation(method, OperationLog.class);

        if (operationLog == null) {
            return pjp.proceed();
        }

        Map<String, String> paramNameValueMap = buildParamNameValuePairs(pjp.getArgs(), method);
        paramNameValueMap.keySet().removeIf("response"::equals);
        if (MapUtils.isEmpty(paramNameValueMap)) {
            return pjp.proceed();
        }

        String aroundLogContent = "invoke method is " + method.getName() + ", params key-value json is "
                + JacksOn.writeAsString(paramNameValueMap);
        CompletableFuture.runAsync(() -> log.info(aroundLogContent));

        return pjp.proceed();
    }


    @AfterReturning(returning="rvt", pointcut = "@annotation(com.ctsi.ssdc.aspectj.OperationLog)")
    public void afterLogAspect(JoinPoint point, Object rvt) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = AnnotatedElementUtils.getMergedAnnotation(method, OperationLog.class);

        if (operationLog == null) {
            return;
        }

        String afterLogContent = "invoke method " + method.getName() + ", return obj-value json is "
                + JacksOn.writeAsString(rvt);
        CompletableFuture.runAsync(() -> log.info(afterLogContent));

    }


    @AfterThrowing(throwing="e", pointcut = "@annotation(com.ctsi.ssdc.aspectj.OperationLog)")
    public void afterLogAspect(JoinPoint point, Throwable e) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        if (AnnotatedElementUtils.getMergedAnnotation(method, OperationLog.class) == null) {
            return;
        }

        String afterLogContent = " invoke method is " + method.getName() + ", catch an exception is "
                + e.getLocalizedMessage();
        CompletableFuture.runAsync(() -> log.info(afterLogContent));
    }


    /**
     * 将方法的请求参数名和参数值构建成键值对信息
     * @param arguments 方法请求参数信息
     * @param method 方法信息
     * @return 参数名-参数值 键值对信息
     */
    private Map<String, String> buildParamNameValuePairs(Object[] arguments, Method method) {

        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        String[] parameterNames = pnd.getParameterNames(method);
        if (ArrayUtils.isEmpty(parameterNames)) {
            return Collections.emptyMap();
        }

        // 参数名和参数值 组合
        Map<String, String> paramMap = new HashMap<>(1 << 5);
        for (int i = 0; i < parameterNames.length; i++) {

            if (arguments[i] == null || "response".equals(parameterNames[i])) {
                continue;
            }
            paramMap.put(parameterNames[i], JacksOn.writeAsString(arguments[i]));
        }
        return paramMap;
    }
}
