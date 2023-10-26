package com.ctsi.ssdc.aspectj;

import com.ctsi.ssdc.cache.RepeatSubmitCache;
import com.ctsi.ssdc.annotation.RepeatSubmit;
import com.ctsi.ssdc.exception.RepeatSubmitException;
import com.ctsi.ssdc.security.SecurityHxUtils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zalando.problem.Status;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 重复提交切面
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-15 14:34
 **/
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    public static final String REPEAT_SUBMIT_KEY = "repeat_submit_key:";

    @Resource
    RepeatSubmitCache repeatSubmitCache;

    @Pointcut("execution(public * com.ctsi.ssdc..*(..))")
    public void webLog() {
    }

    @Before("webLog() && @annotation(repeatSubmit)")
    public void before(final JoinPoint joinPoint, RepeatSubmit repeatSubmit) throws RepeatSubmitException {
        if (repeatSubmit != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            String key = getRepeatSubmitKey(joinPoint);
            String t =  repeatSubmitCache.get(key);
            if (null == t) {
                    createKey(key);
                } else{
                    throw new RepeatSubmitException("repeatSubmit", Status.LOCKED,
                            repeatSubmit.message(), "error.repeatSubmit", request.getRequestURI());
                }

        }
    }

    private void createKey(String key) {
        String uuid = UUID.randomUUID().toString();

        long now = System.currentTimeMillis();
        String value = uuid + "_" + now;
        repeatSubmitCache.put(key,value);
        log.info("key={};value={}",key, value);
    }

    /**
     * 获取重复提交key
     * @param joinPoint
     * @return
     */
    public String getRepeatSubmitKey(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.asList(joinPoint.getArgs()).stream().map(i -> String.valueOf(i)).collect(Collectors.joining());

        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        StringBuilder key = new StringBuilder(REPEAT_SUBMIT_KEY);
        key.append(currentUserId).append("_").append(methodName).append(args);
        return key.toString();
    }

}