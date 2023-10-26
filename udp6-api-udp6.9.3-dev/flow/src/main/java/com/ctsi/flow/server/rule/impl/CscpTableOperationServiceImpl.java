package com.ctsi.flow.server.rule.impl;

import com.ctsi.flow.param.model.CscpTableOperation;
import com.ctsi.flow.param.model.CscpTableOperationExample;
import com.ctsi.flow.repository.CscpTableOperationRepository;
import com.ctsi.flow.server.rule.CscpTableOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/26 10:34  by xx
 */
@Service
public class CscpTableOperationServiceImpl implements CscpTableOperationService {
    @Resource
    private CscpTableOperationRepository cscpTableOperationRepository;


    @Override
    public void addTableFieldPerm(CscpTableOperation cscpTableOperation) {
        cscpTableOperationRepository.insert(cscpTableOperation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTableFieldPerm(CscpTableOperation cscpTableOperation) {
        deleteTableFieldPerm(cscpTableOperation);
        if (!CollectionUtils.isEmpty(cscpTableOperation.getFieldIds())) {
            for (String fieldId:cscpTableOperation.getFieldIds()) {
                cscpTableOperation.setFieldId(fieldId);
                cscpTableOperation.setId(null);
                addTableFieldPerm(cscpTableOperation);
            }
        }
    }

    @Override
    public List<CscpTableOperation> qryTableFieldPerm(CscpTableOperation cscpTableOperation) {
        CscpTableOperationExample example = new CscpTableOperationExample();
        if (!StringUtils.isEmpty(cscpTableOperation.getTaskDefinitionKey())
        &&!StringUtils.isEmpty(cscpTableOperation.getTaskDefinitionFlag())) {
            example.createCriteria().andTaskDefinitionKeyEqualTo(cscpTableOperation.getTaskDefinitionKey())
            .andTaskDefinitionFlagEqualTo(cscpTableOperation.getTaskDefinitionFlag());
        }else {
            throw new IllegalArgumentException("任务标识或流程标识不能为空");
        }
        return cscpTableOperationRepository.selectByExample(example);
    }

//    @Override
//    public List<CscpTableOperation> qryTableOperByFormIdAndFieldName(CscpTableOperation cscpTableOperation) {
//        CscpTableOperationExample example = new CscpTableOperationExample();
//        example.createCriteria().andFormIdEqualTo(cscpTableOperation.getFormId())
//        .andFieldIdEqualTo(cscpTableOperation.getFieldId());
//        return cscpTableOperationRepository.selectByExample(example);
//    }

    @Override
    public void deleteTableFieldPerm(CscpTableOperation cscpTableOperation) {
        CscpTableOperationExample example = new CscpTableOperationExample();
        if (!StringUtils.isEmpty(cscpTableOperation.getTaskDefinitionKey())) {
            example.createCriteria().andTaskDefinitionKeyEqualTo(cscpTableOperation.getTaskDefinitionKey());
        }else {
            throw new IllegalArgumentException("任务标识不能为空");
        }
        cscpTableOperationRepository.deleteByExample(example);
    }

//    public void updateTableFieldPermByFormId(CscpTableOperation cscpTableOperation) {
//        cscpTableOperationRepository.updateByPrimaryKeySelective(cscpTableOperation);
//    }
}
