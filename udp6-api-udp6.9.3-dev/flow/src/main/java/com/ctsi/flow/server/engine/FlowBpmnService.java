package com.ctsi.flow.server.engine;

import com.ctsi.flow.param.request.FlowModeImportReq;
import com.ctsi.flow.param.request.FlowModelCreateReq;
import com.ctsi.flow.param.request.FlowModelPageReq;
import com.ctsi.flow.param.request.FlowModelUpdateReq;
import com.ctsi.flow.param.response.FlowModelPageItemResp;
import com.ctsi.flow.param.response.FlowModelResp;
import com.github.pagehelper.PageInfo;
import org.activiti.bpmn.model.BpmnModel;

import javax.validation.Valid;
import java.io.InputStream;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 15:26
 * @description ：
 */
public interface FlowBpmnService {

    String createModel(@Valid FlowModelCreateReq createRet, String bpmnXml);

    String importModel(@Valid FlowModeImportReq createRet, InputStream inputStream);

    void deleteModel(String id);

    void updateModel(@Valid FlowModelUpdateReq updateRet);

    PageInfo<FlowModelPageItemResp> getModelPage(FlowModelPageReq page);

    FlowModelResp getModel(String id);

    void deployModel(String id);

    void updateModelState(String id, Integer state);

    /**
     * 获得流程模型编号对应的 BPMN Model
     *
     * @param id 流程模型编号
     * @return BPMN Model
     */
    BpmnModel getBpmnModel(String id);
}
