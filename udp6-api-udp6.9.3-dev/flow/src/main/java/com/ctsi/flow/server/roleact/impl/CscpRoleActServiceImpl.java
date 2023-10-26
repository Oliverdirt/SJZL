package com.ctsi.flow.server.roleact.impl;

import com.ctsi.flow.param.model.CscpRoleAct;
import com.ctsi.flow.param.model.CscpRoleActExample;
import com.ctsi.flow.repository.CscpRoleActRepository;
import com.ctsi.flow.server.roleact.CscpRoleActService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing CscpRoleAct.
 *
 * @author hx
 * @date 2022-07-26 15:53:56
 *
 */

@Service
public class CscpRoleActServiceImpl
	extends StrengthenBaseServiceImpl<CscpRoleActRepository, CscpRoleAct, Long, CscpRoleActExample>
	implements CscpRoleActService {

    @Autowired
    private CscpRoleActRepository cscpRoleActRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpRoleActRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpRoleActRepository.deleteByPrimaryKey(id);
    }

    @Override
    public List<CscpRoleAct> findByProcDefId(String id) {
        List<CscpRoleAct> byProcInstId = cscpRoleActRepository.findByProcDefId(id);
        return byProcInstId;
    }
    /**
     * 根据流程模板id删除
     *
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcDefId(String id) {
        cscpRoleActRepository.deleteByProcDefId(id);
    }
    /**
     * 批量插入
     *
     * */
    @Override
    public int insertList(List<CscpRoleAct> record) {
        int i = cscpRoleActRepository.insertList(record);
        return i;
    }
    /**
     * 根据角色id批量查询
     *
     * */
    @Override
    public List<CscpRoleAct> findByRoleIds(List<Long> list,Integer page,Integer size) {
        List<CscpRoleAct> byRoleIds = cscpRoleActRepository.findByRoleIds(list,page,size);
        return byRoleIds;
    }

}
