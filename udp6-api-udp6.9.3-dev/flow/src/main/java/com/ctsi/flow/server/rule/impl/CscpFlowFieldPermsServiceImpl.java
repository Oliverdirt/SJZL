package com.ctsi.flow.server.rule.impl;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.flow.param.model.FieldPerm;
import com.ctsi.flow.server.rule.CscpFlowFieldPermsService;
import org.activiti.engine.ActivitiException;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.flow.param.model.CscpFlowFieldPerms;
import com.ctsi.flow.param.model.CscpFlowFieldPermsExample;
import com.ctsi.flow.repository.CscpFlowFieldPermsRepository;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CscpFlowFieldPerms.
 *
 * @author hx
 * @date 2022-10-24 15:47:57
 */

@Service
public class CscpFlowFieldPermsServiceImpl
        extends StrengthenBaseServiceImpl<CscpFlowFieldPermsRepository, CscpFlowFieldPerms, Long, CscpFlowFieldPermsExample>
        implements CscpFlowFieldPermsService {

    @Autowired
    private CscpFlowFieldPermsRepository cscpFlowFieldPermsRepository;


    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpFlowFieldPermsRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpFlowFieldPermsRepository.deleteByPrimaryKey(id);
    }

    /**
     * @Description:
     * @Author: sunsheng
     * @return: void
     **/
    @Override
    public void updateFieldsPerm(CscpFlowFieldPerms cscpFlowFieldPerms) {
        try {
            cscpFlowFieldPermsRepository.updateFieldsPerm(cscpFlowFieldPerms);
        } catch (Exception e) {
            throw new ActivitiException("Failed to update fields permissions");
        }
    }

    /**
     * @Description: 查询字段权限
     * @Author: sunsheng
     * @return: com.ctsi.flow.param.model.CscpFlowFieldPerms
     **/
    @Override
    public CscpFlowFieldPerms findByTaskDefKey(CscpFlowFieldPerms cscpFlowFieldPerms) {
        CscpFlowFieldPerms flowFieldPerms = cscpFlowFieldPermsRepository.findByTaskDefKey(cscpFlowFieldPerms);
        if (flowFieldPerms != null) {
            String perms = flowFieldPerms.getFieldPerms();
            List<FieldPerm> fieldPerms = JSONObject.parseArray(perms, FieldPerm.class);
            flowFieldPerms.setFieldsPerms(fieldPerms);
        }
        return flowFieldPerms;
    }


}
