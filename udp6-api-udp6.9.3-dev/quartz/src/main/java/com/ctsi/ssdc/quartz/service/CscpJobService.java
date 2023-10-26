package com.ctsi.ssdc.quartz.service;



import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.quartz.domain.CscpJob;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.quartz.domain.CscpJobExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing Job.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpJobService
	extends StrengthenBaseService<CscpJob, Long, CscpJobExample>{


    /**
    * GET  /jobs : get the jobs firstStringBaseColumn.
    */
    PageResult<CscpJob> findFirstStringColumn(CscpJob job, Pageable pageable);

    /**
     * 修改定时任务的状态
     * @param job
     * @return
     */
    CscpJob changeStatus(CscpJob job);


    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    void pauseJob(CscpJob job) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    void resumeJob(CscpJob job) throws SchedulerException;


    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 结果
     */
    public void run(CscpJob job);
    /**
     * 批量删除
     * @param jobIds
     */
    void deleteByIds(Long[] jobIds);


}
