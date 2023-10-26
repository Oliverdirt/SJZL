package com.ctsi.flow.server.form.impl;

import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.server.form.FlowFormService;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVformRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 15:32
 * @description ：
 */

@Service
public class FlowFormServiceImpl implements FlowFormService {

    @Resource
    private CscpCustomizeVformRepository customizeVformRepository;

    @Override
    public List<FlowFormDO> getFormList(Collection<Long> ids) {
        List<CscpCustomizeVform> customizeVforms = customizeVformRepository.selectFormInfoByIds(ids);
        List<FlowFormDO> flowFormList = convert(customizeVforms);
        return flowFormList;
    }


    /**
     * @Description: 转换CscpCustomizeVform集合为FlowFormDO集合
     **/
    List<FlowFormDO> convert(List<CscpCustomizeVform> customizeVforms) {
        List<FlowFormDO> flowFormList = customizeVforms.stream().map(cscpCustomizeVform -> {
            FlowFormDO flowForm = new FlowFormDO();
            flowForm.setId(cscpCustomizeVform.getFormId());
            flowForm.setName(cscpCustomizeVform.getFormName());
            flowForm.setFormType(cscpCustomizeVform.getFormType());
            return flowForm;
        }).collect(Collectors.toList());

        return flowFormList;
    }
}
