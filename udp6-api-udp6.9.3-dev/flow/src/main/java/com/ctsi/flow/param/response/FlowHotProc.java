package com.ctsi.flow.param.response;

/**
 * @Description:
 * @Author: sunsheng
 **/
public class FlowHotProc {
    long count;
    long finished;
    String processName;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
