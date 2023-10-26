package com.ctsi.flow.multi.strategy.impl;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.strategy.IMulti;


/**
 * @author gyp
 */
@Multi(name = "全票通过", sort = 2)
public class AllApproveStrategy implements IMulti {
    @Override
    public MultiInstanceDecisionResult run(MultiInstanceDecisionParam param) {

        int nrOfApprove = param.getNrOfApprove();
        int nrOfReject = param.getNrOfReject();
        int nrOfInstances = param.getNrOfInstances();

        if (nrOfInstances == nrOfApprove) {
            return MultiInstanceDecisionResult.approve();
        }else if (nrOfInstances == nrOfApprove + nrOfReject) {
            return MultiInstanceDecisionResult.reject();
        }else {
            return MultiInstanceDecisionResult.waiting();
        }
    }
}
