package com.ctsi.ssdc.sensitiveword.aspect;

import com.ctsi.ssdc.sensitiveword.annotation.SysSensitiveWord;
import com.ctsi.ssdc.sensitiveword.config.SensitiveWordConfig;
import com.ctsi.ssdc.sensitiveword.filter.SensitiveWordPayloadRequest;
import com.ctsi.ssdc.sensitiveword.util.SensitiveWordUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;


/**
 * 敏感词 切面处理
 * 
 */
@Aspect
@Component
@EnableAsync
public class SysSensitiveWordAspect
{
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SensitiveWordConfig sensitiveWordConfig;

    // 配置织入点
    @Pointcut("@annotation(com.ctsi.ssdc.sensitiveword.annotation.SysSensitiveWord)")
    public void filterPointCut()
    {
    }

    @Around("filterPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
    	if(sensitiveWordConfig.isMethodEnable()) {
    		//全局不开启，方法开启
    		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
			if (method != null)
            {
            	SysSensitiveWord sysSensitiveWord =  method.getAnnotation(SysSensitiveWord.class);	// 获得注解
            	if(sysSensitiveWord.filter()) {
            		Object[] args = joinPoint.getArgs();
                	for(int i = 0; i < args.length; i++) {
                		Object obj = args[i];
                		// 如果是字符串类型直接转换
                		if(obj instanceof String) {
							args[i] = SensitiveWordUtil.replaceSensitiveWord((String)obj, "***");
                		}
                		// 如果是HttpServletRequest
                		else if(obj instanceof HttpServletRequest) {
                			HttpServletRequest request = (HttpServletRequest)obj;
                			String head = request.getHeader("Content-Type");
            				if (head != null) {
            					if (!head.contains("application/x-www-form-urlencoded")) {
            						try {
										// 核心,替换了request
    									obj = new SensitiveWordPayloadRequest(request);
    								} catch (IOException e) {
    								}
            					} else {
									// 获取前端传递的所有参数名的枚举
            						Enumeration<String> pNames = request.getParameterNames();
									// 遍历枚举
            						while (pNames.hasMoreElements()) {
										// 获取参数名
            							String name = pNames.nextElement();
										// 获取参数值
            							String value = request.getParameter(name);
										// 对参数值进行敏感词处理,并重新设置到request
            							String str = SensitiveWordUtil.replaceSensitiveWord(value, "***");
            							request.setAttribute(name, str);
            						}
            					}
            				}
                		}
        				else {
        					//替换所有feild
        					Field[] allFields = obj.getClass().getDeclaredFields();
        					for (int ind = 0; ind < allFields.length; ind++) {
        						Field field = allFields[ind];
        						field.setAccessible(true);
        						try {
    								if(field.get(obj) instanceof String) {
    									field.set(obj, SensitiveWordUtil.replaceSensitiveWord((String)field.get(obj), "***"));
    								}
    							} catch (IllegalArgumentException e) {
    								log.error(e.getMessage(), e);
    							} catch (IllegalAccessException e) {
    								log.error(e.getMessage(), e);
    							}
        					}
        				}
                	}
					return joinPoint.proceed(args);
            	}
            }
    	}
    	return joinPoint.proceed();
    }
}
