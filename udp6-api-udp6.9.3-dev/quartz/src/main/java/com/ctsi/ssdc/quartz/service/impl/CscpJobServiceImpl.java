package com.ctsi.ssdc.quartz.service.impl;


import com.ctsi.ssdc.quartz.consts.ScheduleConstants;
import com.ctsi.ssdc.quartz.domain.CscpJob;
import com.ctsi.ssdc.quartz.exception.TaskException;
import com.ctsi.ssdc.quartz.util.ScheduleUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.quartz.domain.CscpJobExample;
import com.ctsi.ssdc.quartz.service.CscpJobService;
import com.ctsi.ssdc.quartz.repository.CscpJobRepository;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Job.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpJobServiceImpl
	extends StrengthenBaseServiceImpl<CscpJobRepository, CscpJob, Long, CscpJobExample>
	implements CscpJobService {

    private final Logger log = LoggerFactory.getLogger(CscpJobServiceImpl.class);

    @Autowired
    CscpJobRepository jobRepository;

    @Autowired
    private Scheduler scheduler;


    /**
    * GET  /jobs : get the jobs firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpJob> findFirstStringColumn(CscpJob job, Pageable pageable){
        String jobName = job.getJobName();
        String status = job.getStatus();
        String jobGroup = job.getJobGroup();
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpJobExample jobExample = new CscpJobExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            jobExample.setOrderByClause(orderBy);
        }
        CscpJobExample.Criteria criteria = jobExample.createCriteria();
        if (StringUtils.isNotEmpty(jobName)){
            criteria.andJobNameLike("%" + jobName +"%");
        }
        if (StringUtils.isNotEmpty(status)){
            criteria.andStatusEqualTo(status);
        }
        if (StringUtils.isNotEmpty(jobGroup)){
            criteria.andJobGroupEqualTo(jobGroup);
        }
        List<CscpJob>  data = jobRepository.selectByExample(jobExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = jobRepository.countByExample(jobExample);
        }
        return new PageResult<CscpJob>(data,count,count);
    }

    @Override
    @Transactional
    public CscpJob insert(CscpJob job)  {
//        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int row = jobRepository.insert(job);
        if (row > 0)
        {
            try {
                ScheduleUtils.createScheduleJob(scheduler, job);
            } catch (SchedulerException e) {
                log.error("新增定时任务发生异常", e);
            } catch (TaskException e) {
                log.error("新增任务,设置定时任务策略异常", e);

            }
        }
        return job;
    }

    @Override
    public CscpJob update(CscpJob job) {
        int row = jobRepository.updateByPrimaryKeySelective(job);
        if (row > 0)
        {
            try {
                updateSchedulerJob(job, job.getJobGroup());
            } catch (SchedulerException e) {
                log.error("更新定时任务发生异常", e);
            } catch (TaskException e) {
                log.error("更新任务,设置定时任务策略异常", e);
            }
        }

        return job;
    }

    /**
     * 更新任务
     *
     * @param job 任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(CscpJob job, String jobGroup) throws SchedulerException, TaskException
    {
        Long jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param
     */
    @Override
    @Transactional
    public void delete(Long jobId) {
        CscpJob job = jobRepository.selectByPrimaryKey(jobId);
        int row = jobRepository.deleteByPrimaryKey(jobId);
        String jobGroup = job.getJobGroup();
        if (row > 0) {
            try {
                scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            } catch (SchedulerException e) {
                log.error("删除定时任务发生异常", e);
            }
        }
        super.delete(jobId);
    }

    @Override
    public CscpJob changeStatus(CscpJob job) {
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            try {
                resumeJob(job);
            } catch (SchedulerException e) {
                log.error("恢复定时任务发生异常", e);
            }
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            try {
                pauseJob(job);
            } catch (SchedulerException e) {
                log.error("暂停定时任务发生异常", e);
            }
        }
        return job;
    }

    private String getPageOrderBy(Pageable page) {
        if(page!= null && page.getSort() != null) {
            StringBuilder sb = new StringBuilder();
            page.getSort().forEach(sort -> sb.append(sort.getProperty())
            .append(" ").append(sort.getDirection()).append(","));
            if(sb.length() > 1) {
                return (sb.substring(0,sb.length()-1));
             }
        }
        return null;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional
    public void resumeJob(CscpJob job) throws SchedulerException
    {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobRepository.updateByPrimaryKey(job);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional
    public void pauseJob(CscpJob job) throws SchedulerException
    {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobRepository.updateByPrimaryKey(job);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional
    public void run(CscpJob job)
    {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        CscpJob properties = jobRepository.selectByPrimaryKey(jobId);
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        try {
            scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
        } catch (SchedulerException e) {
            log.error("运行一次定时任务时发生异常", e);
        }
    }

    /**
     * 批量删除
     * @param jobIds
     */
    @Override
    public void deleteByIds(Long[] jobIds) {
        for (Long jobId : jobIds) {
            delete(jobId);
        }
    }
}
