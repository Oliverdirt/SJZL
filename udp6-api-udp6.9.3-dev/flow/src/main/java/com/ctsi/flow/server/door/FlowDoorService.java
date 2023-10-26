package com.ctsi.flow.server.door;

import com.ctsi.flow.param.PageParam;
import com.ctsi.flow.param.model.ActOperatePerm;
import com.ctsi.flow.param.model.ActRdTurnTask;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/19 14:37  by xx
 */
public interface FlowDoorService {
    /**查询传阅或转办信息*/
    PageInfo<ActRdTurnTask> qryRdTurnTaskInfo(PageParam pageParam,String type);

    /**新增传阅或转办信息*/
    void createRdTurnTask(ActRdTurnTask actRdTurnTask);


    String qryFormIdByProcessDefinitionId(String id);

}
