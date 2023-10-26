package com.ctsi.ssdc.aspectj;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.StatisticException;
import com.ctsi.ssdc.log.domain.CscpComponentCallLog;
import com.ctsi.ssdc.log.service.CscpComponentCallLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class ComponentCallLogAdvised {
    private static final Logger logger = LoggerFactory.getLogger(ComponentCallLogAdvised.class);
//    private static String DATE_FORMAT = "yyyy-MM-dd";


    @Autowired
    private CscpComponentCallLogService callLogService;

    // 组件调用日志开关
    @Value("${ctsi.log.component-call-log.enable}")
    private boolean componentCallEnable;

    @Value("${ctsi.log.component-call-log.company}")
    private String company;

    @Value("${ctsi.log.component-call-log.project-name}")
    private String projectName;

    @After("execution(* *(..)) && @annotation(anno)")
    public void after(JoinPoint joinPoint, ComponentCallAnno anno) throws StatisticException {
        try {
            if (componentCallEnable){
                CscpComponentCallLog callLog = new CscpComponentCallLog();
                callLog.setCallTime(new Date());
                callLog.setCompany(company);
                callLog.setProjectName(projectName);
                callLog.setComponentName(anno.component());
                callLog.setComponentMethodName(anno.method());
                callLog.setUploadFlag(0);
                callLogService.insert(callLog);
            }
        } catch (Exception e) {
            throw new StatisticException(e.getMessage(), e);
        }
    }
}
