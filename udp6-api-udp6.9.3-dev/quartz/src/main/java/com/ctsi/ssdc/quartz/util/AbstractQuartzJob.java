package com.ctsi.ssdc.quartz.util;


import com.ctsi.ssdc.quartz.consts.ScheduleConstants;
import com.ctsi.ssdc.quartz.domain.CscpJob;
import com.ctsi.ssdc.quartz.domain.CscpJobLog;
import com.ctsi.ssdc.quartz.service.CscpJobLogService;
import com.ctsi.ssdc.util.SpringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 抽象quartz调用
 *
 */
public abstract class AbstractQuartzJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        CscpJob sysJob = new CscpJob();
        //BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try
        {
            BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES),sysJob);
            before(context, sysJob);
            if (sysJob != null)
            {
                doExecute(context, sysJob);
            }
            after(context, sysJob, null);
        }
        catch (Exception e)
        {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
    protected void before(JobExecutionContext context, CscpJob sysJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
    protected void after(JobExecutionContext context, CscpJob sysJob, Exception e)
    {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final CscpJobLog cscpJobLog = new CscpJobLog();
        cscpJobLog.setJobName(sysJob.getJobName());
        cscpJobLog.setJobGroup(sysJob.getJobGroup());
        cscpJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        cscpJobLog.setStartTime(startTime);
        cscpJobLog.setStopTime(new Date());
        cscpJobLog.setExecutionTime(ZonedDateTime.now());
        long runMs = cscpJobLog.getStopTime().getTime() - cscpJobLog.getStartTime().getTime();
        cscpJobLog.setJobMessage(cscpJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null)
        {
            cscpJobLog.setStatus(ScheduleConstants.FAIL_STATUS);
            String errorMsg = e.getMessage().substring(0, 2000);
            cscpJobLog.setExceptionInfo(errorMsg);
        }
        else
        {
            cscpJobLog.setStatus(ScheduleConstants.SUCCESS_STATUS);
        }

        // 写入数据库当中
        SpringUtil.getBean(CscpJobLogService.class).insert(cscpJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, CscpJob sysJob) throws Exception;
}
