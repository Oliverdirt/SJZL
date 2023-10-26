package com.ctsi.flow.multi.service;

import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import org.activiti.engine.delegate.DelegateExecution;

import java.util.List;

/**
 * @author gyp
 */
public interface MultiInstanceService {

    /**
     * create by: guoyanpei
     * description: 初始化决策变量
     */
    void install(String parentExecutionId);

    /**
     * create by: guoyanpei
     * description: 更新决策变量
     */
    void update(String parentExecutionId, String vote);

    /**
     * create by: guoyanpei
     * description: 移除决策变量
     */
    void remove(String parentExecutionId);

    /**
     * create by: guoyanpei
     * description: 计算多实例结果
     */
    MultiInstanceDecisionResult decision(CscpFlowTaskMultiRule cscpFlowTaskMultiRule, String executionId, String parentExecutionId, String processDefinitionId, String vote);

    /**
     * create by: guoyanpei
     * description: 注入处理人集合
     */
    List<String> pourAssigneeCollection(DelegateExecution execution);

    /**
     * create by: guoyanpei
     * description: 完成校验
     */
    boolean hasComplete(DelegateExecution execution);

    /**
     * create by: guoyanpei
     * description: 是否多实例
     */
    boolean isMulti(String taskKey);

}
