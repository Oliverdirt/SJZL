package com.ctsi.flow.multi.model;

/**
 * @author gyp
 */
public class MultiInstanceDecisionResult {

    /**
     * 是否可以进入下环节，设置下一环节信息
     */
    private boolean intoNextNode = false;

    /**
     * 多实例计算得到结果：1：通过，0：拒绝
     */
    private Integer multiCalVal = -1;

    /**
     * 本次无法产生结果，需要下次投票
     */
    public static MultiInstanceDecisionResult waiting() {
        return new MultiInstanceDecisionResult();
    }

    /**
     * 不通过
     */
    public static MultiInstanceDecisionResult reject() {
        return new MultiInstanceDecisionResult(true, 0);
    }

    /**
     * 通过
     */
    public static MultiInstanceDecisionResult approve() {
        return new MultiInstanceDecisionResult(true, 1);
    }

    private MultiInstanceDecisionResult() {
    }

    private MultiInstanceDecisionResult(boolean intoNextNode, Integer multiCalVal) {
        this.intoNextNode = intoNextNode;
        this.multiCalVal = multiCalVal;
    }

    public boolean isIntoNextNode() {
        return intoNextNode;
    }

    public void setIntoNextNode(boolean intoNextNode) {
        this.intoNextNode = intoNextNode;
    }

    public Integer getMultiCalVal() {
        return multiCalVal;
    }

    public void setMultiCalVal(Integer multiCalVal) {
        this.multiCalVal = multiCalVal;
    }

}
