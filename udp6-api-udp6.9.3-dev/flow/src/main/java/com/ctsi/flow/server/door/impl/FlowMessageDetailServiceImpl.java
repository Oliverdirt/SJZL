package com.ctsi.flow.server.door.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ctsi.flow.param.FlowDateMessage;
import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.request.FlowModelHotProcReq;
import com.ctsi.flow.param.response.*;
import com.ctsi.flow.server.door.FlowMessageDetailService;
import com.ctsi.flow.server.useract.CscpUserActService;
import com.ctsi.flow.util.CollectionUtils;
import com.ctsi.flow.util.PageHelperTool;
import com.ctsi.flow.util.PageUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-02 16:16
 * @description ：
 */
@Service
public class FlowMessageDetailServiceImpl implements FlowMessageDetailService {


    @Resource
    private HistoryService historyService;

    @Resource
    private CscpUserActService cscpUserActService;

    @Resource
    private RepositoryService repositoryService;

    @Resource(name = "flow-extra-executor")
    private ThreadPoolTaskExecutor flowExtraExecutor;

    /**
     * 任务处理批次 每次处理量
     */
    private static final int TASK_BATCH_THRESHOLD = 2;

    @Override
    public FlowMessageResp queryMessage(PageParam pageParam) {

        FlowMessageResp flowMessageResp = new FlowMessageResp();

        //异步多线程查询信息
        CompletableFuture<Long> modelCountFuture = getModelCountFuture();
        CompletableFuture<Long> processInstanceCountFuture = getProcessInstanceCountFuture();

        // 主线程查询列表信息
        ModelQuery modelQuery = repositoryService.createModelQuery();
        List<Model> models = modelQuery.orderByCreateTime().deployed().desc()
                .listPage(PageUtils.getStart(pageParam), pageParam.getSize());

        // 结果封装成model的idList,后做排序用
        List<String> deploymentIds = models.stream().map(Model::getDeploymentId).collect(Collectors.toList());

        // 结果封装成key value,后序封装用
        Map<String, String> nameMaps = models.stream().collect(Collectors.toMap(Model::getDeploymentId, Model::getName,(key1, key2) -> key2));

        // 查询每个model的信息包括：总实例数，运行实例数，总任务数，代办任务数
        List<FlowModelCountMessage> flowModelCountMessages = Lists.newArrayList();
        //运用窃取池算出每个model的信息
        if (models.size() == 0) {
            return flowMessageResp;
        }
        // 将大任务分解处理
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<FlowModelCountMessage> result = forkJoinPool.invoke(new AnalysisProcessTask(0, deploymentIds.size(), deploymentIds));

        Map<String, FlowModelCountMessage> flowModelCountMessageMap = result.stream()
                .collect(Collectors.toMap(FlowModelCountMessage::getDeploymentId, Function.identity()));

        // 按查询顺序返回结果
        deploymentIds.forEach(e -> {
            FlowModelCountMessage flowModelCountMessage = flowModelCountMessageMap.get(e);
            flowModelCountMessage.setName(nameMaps.get(e));
            flowModelCountMessages.add(flowModelCountMessage);
        });

        try {
            Long modelCount = modelCountFuture.get();
            Long processInstanceCount = processInstanceCountFuture.get();
            flowMessageResp.setModelCount(modelCount);
            flowMessageResp.setProcessInstanceCount(processInstanceCount);
        } catch (Exception e) {
            throw new RuntimeException("获取信息失败");
        }

        //
        PageInfo<FlowModelCountMessage> pageInfo = new PageInfo<>(flowModelCountMessages);
        PageHelperTool.initPageInfoObj(pageParam.getPage(), (int) modelQuery.count(), pageParam.getSize(), pageInfo);
        flowMessageResp.setFlowModelCountMessageList(pageInfo);

        return flowMessageResp;
    }

    /**
     * 查询热门流程信息
     *
     * @param flowModelHotProcReq
     */
    @Override
    public FlowModelHotProcResp queryHotProcMessage(FlowModelHotProcReq flowModelHotProcReq) {
        Date startDate = Date.from(flowModelHotProcReq.getStartTime().toInstant());
        Date endDate = Date.from(flowModelHotProcReq.getEndTime().toInstant());
        ModelQuery modelQuery = repositoryService.createModelQuery();
        List<Model> models = modelQuery.list();
        List<FlowHotProc> respList = null;
        if (CollectionUtils.isAnyEmpty(models)){
           respList = Collections.emptyList();
        } else {
            respList = new ArrayList<>();
            Set<String> deploymentIds = models.stream().map(Model::getDeploymentId).collect(Collectors.toSet());
            List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
            for (ProcessDefinition definition : definitions) {
                HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionId(definition.getId()).startedBefore(endDate).startedAfter(startDate);
                long count = processInstanceQuery.count();
                long finished = processInstanceQuery.finished().count();
                FlowHotProc resp = new FlowHotProc();
                if (count > 0L) {
                    resp.setCount(count);
                    resp.setFinished(finished);
                    resp.setProcessName(definition.getName());
                    respList.add(resp);
                }
            }
        }
        FlowModelHotProcResp hotProcResp = new FlowModelHotProcResp();
        respList = respList.stream().sorted(Comparator.comparing(FlowHotProc::getCount).reversed()).skip(PageUtils.getStart(flowModelHotProcReq.getPageParam())).limit(flowModelHotProcReq.getPageParam().getSize()).collect(Collectors.toList());
        PageInfo<FlowHotProc> pageInfo = new PageInfo<>(respList);
        PageHelperTool.initPageInfoObj(flowModelHotProcReq.getPageParam().getPage(), (int) modelQuery.count(), flowModelHotProcReq.getPageParam().getSize(), pageInfo);
        hotProcResp.setFlowHotProcList(pageInfo);
        return hotProcResp;
    }

    @Override
    public FlowPropMessageResp queryPropMessage() {

        FlowPropMessageResp flowPropMessageResp = new FlowPropMessageResp();
        long processInstanceTodo = historyService.createHistoricProcessInstanceQuery().unfinished().count();
        long processInstanceDown = historyService.createHistoricProcessInstanceQuery().finished().count();
        flowPropMessageResp.setProcessInstanceTodo(processInstanceTodo);
        flowPropMessageResp.setProcessInstanceDown(processInstanceDown);
        flowPropMessageResp.setProcCountProp(String.valueOf(getPercent(processInstanceDown, processInstanceTodo + processInstanceDown)));

        long taskTodo = historyService.createHistoricTaskInstanceQuery().unfinished().count();
        long taskDown = historyService.createHistoricTaskInstanceQuery().finished().count();
        flowPropMessageResp.setTaskTodo(taskTodo);
        flowPropMessageResp.setTaskDown(taskDown);
        flowPropMessageResp.setTaskCountProp(String.valueOf(getPercent(taskDown, taskDown + taskTodo)));
        return flowPropMessageResp;
    }

    @Override
    public FlowTrendMessageResp queryTrendMessage() {


        FlowTrendMessageResp flowTrendMessageResp = new FlowTrendMessageResp();

        // 获取统计时间起点
        Calendar startDate = getStartDate();
        Date date = startDate.getTime();

        List<String> dateList = getDateList();
        flowTrendMessageResp.setDateList(dateList);
        // 获取返回的日期列表
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<HistoricProcessInstance> processInstances = historyService
                .createHistoricProcessInstanceQuery().or().startedAfter(date).finishedAfter(date).endOr().list();
        List<FlowDateMessage> processInstanceList = Lists.newArrayList();


        processInstances.forEach(e -> {
            FlowDateMessage build = build(e.getId(), e.getStartTime(), e.getEndTime());
            processInstanceList.add(build);
        });

        // 流程启动，流程关闭，任务开启任务关闭
        ArrayList<FlowDateMessage> procStart = Lists.newArrayList();
        ArrayList<FlowDateMessage> procEnd = Lists.newArrayList();
        ArrayList<FlowDateMessage> taskStart = Lists.newArrayList();
        ArrayList<FlowDateMessage> taskEnd = Lists.newArrayList();

        buildMessageList(df, processInstanceList, procStart, procEnd);
        // 发起流程信息
        List<Long> procStartList = getLongList(dateList, procStart);
        flowTrendMessageResp.setProcInstanceStartList(procStartList);

        // 结束流程信息
        if (!CollectionUtil.isEmpty(procEnd)) {
            List<Long> procEndList = getLongList(dateList, procEnd);
            flowTrendMessageResp.setProcInstanceEndList(procEndList);
        }

        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .or().taskCompletedAfter(date).taskCreatedAfter(date).endOr().list();

        List<FlowDateMessage> taskList = Lists.newArrayList();
        tasks.forEach(e -> {
            FlowDateMessage build = build(e.getId(), e.getStartTime(), e.getEndTime());
            taskList.add(build);
        });

        buildMessageList(df, taskList, taskStart, taskEnd);

        // 发起流程信息
        List<Long> taskStartList = getLongList(dateList, taskStart);
        flowTrendMessageResp.setTaskStartList(taskStartList);

        // 结束流程信息
        if (!CollectionUtil.isEmpty(taskEnd)) {
            List<Long> taskEndList = getLongList(dateList, taskEnd);
            flowTrendMessageResp.setTaskEndList(taskEndList);
        }
        return flowTrendMessageResp;
    }

    private void buildMessageList(DateTimeFormatter sdf, List<FlowDateMessage> processInstanceList,
                                  ArrayList<FlowDateMessage> procStart, ArrayList<FlowDateMessage> procEnd) {
        processInstanceList.forEach(e -> {
            FlowDateMessage flowDateMessageStart = new FlowDateMessage();
            flowDateMessageStart.setKey(e.getKey());
            Date startTime = e.getStartTime();
            LocalDateTime localDateTimeStart = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
            String startTimeFormat = localDateTimeStart.format(sdf);
            flowDateMessageStart.setDate(startTimeFormat);
            procStart.add(flowDateMessageStart);
            Date endTime = e.getEndTime();
            if (endTime != null) {
                FlowDateMessage flowDateMessageEnd = new FlowDateMessage();
                flowDateMessageEnd.setKey(e.getKey());
                LocalDateTime localDateTimeEnd = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
                String endTimeFormat = localDateTimeEnd.format(sdf);
                flowDateMessageEnd.setDate(endTimeFormat);
                procEnd.add(flowDateMessageEnd);
            }
        });
    }


    public FlowDateMessage build(String key, Date startTime, Date endTime) {
        FlowDateMessage flowDateMessage = new FlowDateMessage();
        flowDateMessage.setKey(key);
        flowDateMessage.setStartTime(startTime);
        flowDateMessage.setEndTime(endTime);
        return flowDateMessage;
    }

    private List<Long> getLongList(List<String> dateList, ArrayList<FlowDateMessage> procStart) {
        Map<String, List<FlowDateMessage>> collectStart = procStart.stream()
                .collect(Collectors.groupingBy(FlowDateMessage::getDate));
        List<Long> list = Lists.newArrayList();
        dateList.forEach(e -> {
            Long numb = collectStart.get(e) != null ? collectStart.get(e).size() : 0L;
            list.add(numb);
        });
        return list;
    }

    private Calendar getStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private List<String> getDateList() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startDate = getStartDate();
        ArrayList<String> dateList = Lists.newArrayList();
        String dateFormat = sdf.format(startDate.getTime());
        dateList.add(dateFormat);
        for (int i = 0; i < 6; i++) {
            startDate.add(Calendar.DATE, 1);
            dateFormat = sdf.format(startDate.getTime());
            dateList.add(dateFormat);
        }
        return dateList;
    }

    /**
     * 计算百分比
     */
    private long getPercent(long count, long total) {

        if (total == 0) {
            return 0;
        }
        BigDecimal currentCount = new BigDecimal(count);
        BigDecimal totalCount = new BigDecimal(total);
        BigDecimal divide = currentCount.divide(totalCount, 2, BigDecimal.ROUND_HALF_UP);
        return divide.multiply(new BigDecimal(100)).longValue();
    }

    class AnalysisProcessTask extends RecursiveTask<List<FlowModelCountMessage>> {

        private static final long serialVersionUID = -8035079108507471750L;

        private int start;

        private int end;

        private List<String> deploymentIds;

        AnalysisProcessTask(int start, int end, List<String> deploymentIds) {
            this.start = start;
            this.end = end;
            this.deploymentIds = deploymentIds;
        }

        @Override
        protected List<FlowModelCountMessage> compute() {
            // 当阀值小于等于2则不分解了
            if ((end - start) <= TASK_BATCH_THRESHOLD) {
                return getAnalysisProcess(start, end, deploymentIds);
            } else {
                return segmentRecursiveTasks();
            }
        }

        private List<FlowModelCountMessage> segmentRecursiveTasks() {

            // 如果任务大于阈值，分裂成两个子任务计算
            int middle = (start + end) / 2;
            AnalysisProcessTask leftTask = new AnalysisProcessTask(start, middle, deploymentIds);
            AnalysisProcessTask rightTask = new AnalysisProcessTask(middle, end, deploymentIds);

            // 执行子任务 & 等待任务执行结束 复用线程池
            leftTask.fork();
            rightTask.fork();
            List<FlowModelCountMessage> returnList = Lists.newArrayList();
            returnList.addAll(leftTask.join());
            returnList.addAll(rightTask.join());
            return returnList;
        }
    }


    private List<FlowModelCountMessage> getAnalysisProcess(int start, int end, List<String> deploymentIds) {

        ArrayList<FlowModelCountMessage> resultList = Lists.newArrayList();

        for (int i = start; i < end; i++) {

            FlowModelCountMessage flowModelCountMessage = new FlowModelCountMessage();
            String deploymentId = deploymentIds.get(i);
            flowModelCountMessage.setDeploymentId(deploymentId);
            String key = repositoryService.createModelQuery().deploymentId(deploymentId).singleResult().getKey();

            List<ProcessDefinition> pdfList = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(key).list();

            ArrayList<String> list = Lists.newArrayList();
            pdfList.forEach(e-> {list.add(e.getDeploymentId());});
            // 查询流程实例信息
            long processInstanceAll = historyService.createHistoricProcessInstanceQuery().deploymentIdIn(list)
                    .count();
            long processInstanceDown = historyService.createHistoricProcessInstanceQuery().deploymentIdIn(list)
                    .finished().count();
            flowModelCountMessage.setProcessInstanceAll(processInstanceAll);
            flowModelCountMessage.setProcessInstanceDown(processInstanceDown);
            flowModelCountMessage.setProcCountProp(getPercent(processInstanceDown, processInstanceAll)+"%");

            // 查询任务信息
            long taskAll = historyService.createHistoricTaskInstanceQuery().deploymentIdIn(list).count();
            long taskDown = historyService.createHistoricTaskInstanceQuery().deploymentIdIn(list).finished().count();
            flowModelCountMessage.setTaskAll(taskAll);
            flowModelCountMessage.setTaskDown(taskDown);
            flowModelCountMessage.setTaskCountProp(getPercent(taskDown, taskAll)+"%");

            resultList.add(flowModelCountMessage);
        }

        return resultList;
    }


    private CompletableFuture<Long> getModelCountFuture() {
        return CompletableFuture.supplyAsync(() -> {
            ModelQuery modelQuery = repositoryService.createModelQuery();
            return modelQuery.count();
        }, flowExtraExecutor);
    }

    private CompletableFuture<Long> getProcessInstanceCountFuture() {
        return CompletableFuture.supplyAsync(() -> {
            HistoricProcessInstanceQuery processQuery = historyService.createHistoricProcessInstanceQuery();
            return processQuery.count();
        }, flowExtraExecutor);
    }
}
