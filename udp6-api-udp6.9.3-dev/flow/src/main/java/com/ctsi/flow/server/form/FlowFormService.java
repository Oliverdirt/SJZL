package com.ctsi.flow.server.form;

import cn.hutool.core.collection.CollUtil;
import com.ctsi.flow.param.model.FlowFormDO;
import com.ctsi.flow.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 15:30
 */


public interface FlowFormService {

    default Map<Long, FlowFormDO> getFormMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMap(this.getFormList(ids), FlowFormDO::getId);
    }


    List<FlowFormDO> getFormList(Collection<Long> ids);

}
