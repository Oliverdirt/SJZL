package com.ctsi.flow.multi.strategy.impl;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.strategy.IMulti;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-12-01 16:41
 * @description ：票数通过
 */

@Multi(name = "票数通过", sort = 4)
public class TicketStrategy implements IMulti {
    @Override
    public MultiInstanceDecisionResult run(MultiInstanceDecisionParam param) {

        Integer scheme = param.getCscpFlowTaskMultiRule().getScheme();
        int nrOfApprove = param.getNrOfApprove();
        int nrOfInstances = param.getNrOfInstances();

        if (nrOfApprove >= scheme) {
            return MultiInstanceDecisionResult.approve();
        }
        if (nrOfInstances - nrOfApprove < scheme) {
            return MultiInstanceDecisionResult.reject();
        }

        return MultiInstanceDecisionResult.waiting();
    }
}
