package com.ctsi.flow.multi.strategy.impl;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.strategy.IMulti;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-11-24 14:17
 * @description ：一票通过策略
 */

@Multi(name = "一票通过", sort = 3)
public class OneApproveStrategy implements IMulti {
    @Override
    public MultiInstanceDecisionResult run(MultiInstanceDecisionParam param) {
        int nrOfReject = param.getNrOfReject();
        int nrOfInstances = param.getNrOfInstances();
        if (param.getCurrentVote() == 1) {
            return MultiInstanceDecisionResult.approve();
        }
        if (nrOfInstances == nrOfReject) {
            return MultiInstanceDecisionResult.reject();
        }
        return MultiInstanceDecisionResult.waiting();
    }
}
