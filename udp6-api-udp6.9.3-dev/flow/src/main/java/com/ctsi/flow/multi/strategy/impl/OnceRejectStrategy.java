package com.ctsi.flow.multi.strategy.impl;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.strategy.IMulti;


/**
 * @author ss
 */
@Multi(name = "一票否决", sort = 1)
public class OnceRejectStrategy implements IMulti {
    @Override
    public MultiInstanceDecisionResult run(MultiInstanceDecisionParam param) {

        int nrOfApprove = param.getNrOfApprove();
        int nrOfInstances = param.getNrOfInstances();
        if (param.getCurrentVote() == 0) {
            return MultiInstanceDecisionResult.reject();
        }
        if (nrOfInstances == nrOfApprove) {
            return MultiInstanceDecisionResult.approve();
        }
        return MultiInstanceDecisionResult.waiting();
    }
}
