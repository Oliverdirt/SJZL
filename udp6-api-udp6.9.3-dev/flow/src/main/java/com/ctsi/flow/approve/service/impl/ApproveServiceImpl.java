package com.ctsi.flow.approve.service.impl;

import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.flow.approve.domain.Approve;
import com.ctsi.flow.approve.domain.ApproveExample;
import com.ctsi.flow.approve.service.ApproveService;
import com.ctsi.flow.approve.repository.ApproveRepository;

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
 * Service Implementation for managing Approve.
 *
 * @author hx
 * @date 2022-11-03 19:50:16
 *
 */

@Service
public class ApproveServiceImpl
	extends StrengthenBaseServiceImpl<ApproveRepository, Approve, Long, ApproveExample>
	implements ApproveService {

    @Autowired
    private ApproveRepository approveRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        approveRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        approveRepository.deleteByPrimaryKey(id);
    }



}
