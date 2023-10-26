package com.ctsi.flow.server.door.impl;

import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.model.ActRdTurnTask;
import com.ctsi.flow.param.model.ActRdTurnTaskExample;
import com.ctsi.flow.repository.ActRdTurnTaskRepository;
import com.ctsi.flow.server.door.FlowDoorService;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/19 14:38  by xx
 */
@Service
public class FlowDoorServiceImpl implements FlowDoorService {
    @Resource
    private ActRdTurnTaskRepository taskRepository;

    @Override
    public PageInfo<ActRdTurnTask> qryRdTurnTaskInfo(PageParam pageParam, String type) {
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        if (ObjectUtils.isEmpty(currentUserId)){
            return new PageInfo<>();
        }
        PageHelper.startPage(pageParam.getPage(),pageParam.getSize());
        ActRdTurnTask actRdTurnTask = new ActRdTurnTask();
        actRdTurnTask.setType(type);
        actRdTurnTask.setUserId(currentUserId);
        return new PageInfo<>(taskRepository.selectRdTurnTask(actRdTurnTask));
    }

    @Override
    public void createRdTurnTask(ActRdTurnTask actRdTurnTask) {
        // 传阅
        if ("1".equals(actRdTurnTask.getType())) {
            ActRdTurnTaskExample actRdTurnTaskExample = new ActRdTurnTaskExample();
            actRdTurnTaskExample.createCriteria().
                    andUserIdEqualTo(actRdTurnTask.getUserId())
                    .andTypeEqualTo(actRdTurnTask.getType())
                    .andProcessIdEqualTo(actRdTurnTask.getProcessId())
                    .andProcessInstidEqualTo(actRdTurnTask.getProcessInstid());
            List<ActRdTurnTask> actRdTurnTasks = taskRepository.selectByExample(actRdTurnTaskExample);
            // 判断是否已经传阅过
            if (!CollectionUtils.isEmpty(actRdTurnTasks)) {
                return;
            }
        }
        actRdTurnTask.setCreateTime(ZonedDateTime.now());
        taskRepository.insert(actRdTurnTask);
    }


    @Override
    public String qryFormIdByProcessDefinitionId(String id) {
        return taskRepository.qryFormIdByProcessDefinitionId(id);
    }


}
