package com.ctsi.flow.server.useract.impl;

import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpUserAct;
import com.ctsi.flow.param.model.CscpUserActExample;
import com.ctsi.flow.repository.CscpFlowProcessDefExtRepository;
import com.ctsi.flow.repository.CscpUserActRepository;
import com.ctsi.flow.server.useract.CscpUserActService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing CscpUserAct.
 *
 * @author hx
 * @date 2022-07-26 15:55:12
 *
 */

@Service
public class CscpUserActServiceImpl
	extends StrengthenBaseServiceImpl<CscpUserActRepository, CscpUserAct, Long, CscpUserActExample>
	implements CscpUserActService {

    @Autowired
    private CscpUserActRepository cscpUserActRepository;


    @Autowired
    private CscpFlowProcessDefExtRepository cscpFlowProcessDefExtRepository;

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpUserActRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpUserActRepository.deleteByPrimaryKey(id);
    }

    @Override
    public List<CscpUserAct> findByProcDefId(String id) {
        List<CscpUserAct> byProcInstId = cscpUserActRepository.findByProcDefId(id);
        return byProcInstId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcDefId(String id) {
        cscpUserActRepository.deleteByProcDefId(id);
    }

    @Override
    public int insertList(List<CscpUserAct> record) {
        int i = cscpUserActRepository.insertList(record);
        return i;
    }

    @Override
    public List<CscpUserAct> findByUserId(Long id) {
        List<CscpUserAct> byUserId = cscpUserActRepository.findByUserId(id);
        return byUserId;
    }

    @Override
    public int updateByProDefId(String proDefId, String isCollect,Long userId, ZonedDateTime collectTime) {
        int i = cscpUserActRepository.updateByProDefId(proDefId, isCollect,userId,collectTime);
        return i;
    }

    @Override
    public List<CscpUserAct> findMyCollect(Long id) {
        List<CscpUserAct> myCollect = cscpUserActRepository.findMyCollect(id);
        return myCollect;
    }

    @Override
    public CscpUserAct selectByUserIdAndProDefId(Long userId, String proDefId) {
        CscpUserAct cscpUserAct = cscpUserActRepository.selectByUserIdAndProDefId(userId, proDefId);
        return cscpUserAct;
    }

    @Override
    public List<CscpFlowProcessDefExt> selectByModelId(String modelId) {
        List<CscpFlowProcessDefExt> bpmProcessDefinition = cscpFlowProcessDefExtRepository.selectByModelId(modelId);
        return bpmProcessDefinition;
    }

    @Override
    public List<String> listAllDeployIdByProcessDefinitionId(String modelId) {
        List<String> bpmProcessDefinitions = cscpFlowProcessDefExtRepository.listAllDeployIdByProcessDefinitionId(modelId);
        return bpmProcessDefinitions;
    }


}
