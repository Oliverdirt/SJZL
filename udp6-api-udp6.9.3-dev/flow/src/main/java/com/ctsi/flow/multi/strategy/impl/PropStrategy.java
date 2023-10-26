package com.ctsi.flow.multi.strategy.impl;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import com.ctsi.flow.multi.strategy.IMulti;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-12-01 16:46
 * @description ：比例通过
 */

@Multi(name = "比例通过", sort = 5)
public class PropStrategy implements IMulti {
    @Override
    public MultiInstanceDecisionResult run(MultiInstanceDecisionParam param) {

        Integer scheme = param.getCscpFlowTaskMultiRule().getScheme();
        int nrOfReject = param.getNrOfReject();
        int nrOfApprove = param.getNrOfApprove();
        int nrOfInstances = param.getNrOfInstances();

        BigDecimal numOfApprove = new BigDecimal(nrOfApprove);
        BigDecimal numOfReject = new BigDecimal(nrOfReject);
        BigDecimal numOfInstances = new BigDecimal(nrOfInstances);
        BigDecimal numScheme = new BigDecimal(scheme);

        if (numOfApprove.divide(numOfInstances,6,ROUND_HALF_UP)
                .compareTo(numScheme.divide(new BigDecimal(100))) >= 0) {
            return MultiInstanceDecisionResult.approve();
        }
        if ((numOfInstances.subtract(numOfReject).divide(numOfInstances,6,ROUND_HALF_UP)
                .compareTo(numScheme.divide(new BigDecimal(100))) < 0)) {
            return MultiInstanceDecisionResult.reject();
        }

        return MultiInstanceDecisionResult.waiting();
    }
}
