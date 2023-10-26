package com.ctsi.flow.multi.model;

import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import lombok.Data;


/**
 * @author gyp
 */
@Data
public class MultiInstanceDecisionParam {

    private CscpFlowTaskMultiRule cscpFlowTaskMultiRule;
//
    /**
     * 总实例数
     */
    private int nrOfInstances;

    /**
     * 已提交数
     */
    private int nrOfCompletedInstances;

    /**
     * 正在活动的实例数
     */
    private int nrOfActiveInstances;

    /**
     * 当前所在循环数，不是已循环数
     */
    private int loopCounter;

    /**
     * 自定义参数：投票通过数
     */
    private int nrOfApprove;

    /**
     * 自定义参数：投票未通过数
     */
    private int nrOfReject;

    /**
     * 投票人ID
     */
    private String voter;

    /**
     * 当前投票人投票信息，1：通过，0：拒绝
     */
    private Integer currentVote;

}
