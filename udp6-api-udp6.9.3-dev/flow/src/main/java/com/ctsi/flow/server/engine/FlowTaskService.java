package com.ctsi.flow.server.engine;

import com.ctsi.flow.param.FlowOutLine;
import com.ctsi.flow.param.request.*;
import com.ctsi.flow.param.response.FlowMyProcessInstanceResp;
import com.ctsi.flow.param.response.FlowTaskDonePageItemResp;
import com.ctsi.flow.param.response.FlowTaskResp;
import com.ctsi.flow.param.response.FlowTaskTodoPageItemResp;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-27 17:14
 * @description ：
 */
public interface FlowTaskService {

    PageInfo<FlowTaskTodoPageItemResp> getTodoTaskPage(Long userId, FlowTaskTodoPageReq pageReqVO);

    PageInfo<FlowTaskDonePageItemResp> getDoneTaskPage(Long currentUserId, FlowTaskDonePageReq page);

    String approveTask(Long currentUserId, FlowTaskApproveReq req);

    void nextAssigner(String nextHandler, String procId);


    void updateTaskAssignee(Long currentUserId, FlowTaskUpdateAssigneeReq req);

    List<FlowTaskResp> getTaskListByProcessInstanceId(String processInstanceId);

    PageInfo<FlowMyProcessInstanceResp> getMyProcessPage(Long currentUserId, FlowTaskDonePageReq page);

    /**
     * @Description: 签收任务
     * @Author: sunsheng
     * @parm: [currentUserId, taskId]
     * @return: void
     **/
    void claimTask(Long currentUserId, String taskId);

    /**
     * create by: guoyanpei
     * description: 获取出口路线
     * params: taskId
     * return: List<FlowOutLine>
     */
    List<FlowOutLine> getOutLines(String taskId);

    /**
     * create by: guoyanpei
     * description: 任务回收
     * params: processInstanceId
     * return: Void
     */
    Void backProcess(String processInstanceId);

    /**
     * create by: guoyanpei
     * description: 终止流程
     * params: processInstanceId
     * return: Void
     */
    Void endProcess(String processInstanceId);
}
