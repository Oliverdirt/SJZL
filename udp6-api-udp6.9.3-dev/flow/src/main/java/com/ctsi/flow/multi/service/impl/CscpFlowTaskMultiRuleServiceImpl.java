package com.ctsi.flow.multi.service.impl;

import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRuleExample;
import com.ctsi.flow.multi.service.CscpFlowTaskMultiRuleService;
import com.ctsi.flow.multi.repository.CscpFlowTaskMultiRuleRepository;

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
 * Service Implementation for managing CscpFlowTaskMultiRule.
 *
 * @author hx
 * @date 2022-10-24 16:49:41
 *
 */

@Service
public class CscpFlowTaskMultiRuleServiceImpl
	extends StrengthenBaseServiceImpl<CscpFlowTaskMultiRuleRepository, CscpFlowTaskMultiRule, Long, CscpFlowTaskMultiRuleExample>
	implements CscpFlowTaskMultiRuleService {

    @Autowired
    private CscpFlowTaskMultiRuleRepository cscpFlowTaskMultiRuleRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpFlowTaskMultiRuleRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpFlowTaskMultiRuleRepository.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteMultiByKey(String taskKey) {
        cscpFlowTaskMultiRuleRepository.deleteMultiByKey(taskKey);
    }

    @Override
    public CscpFlowTaskMultiRule selectMultiByKey(String taskKey) {
        CscpFlowTaskMultiRuleExample example = new CscpFlowTaskMultiRuleExample();
        example.or().andTaskDefinitionKeyEqualTo(taskKey);
        List<CscpFlowTaskMultiRule> cscpFlowTaskMultiRules = cscpFlowTaskMultiRuleRepository.selectByExample(example);
        if (CollectionUtils.isNotEmpty(cscpFlowTaskMultiRules)) {
            return cscpFlowTaskMultiRules.get(0);
        }else {
            return null;
        }
    }

    @Override
    public CscpFlowTaskMultiRule updateMulti(CscpFlowTaskMultiRule cscpFlowTaskMultiRule) {
        CscpFlowTaskMultiRule response;
        CscpFlowTaskMultiRule result = this.selectMultiByKey(cscpFlowTaskMultiRule.getTaskDefinitionKey());
        if (result == null) {
            response = insert(cscpFlowTaskMultiRule);
        }else {
            cscpFlowTaskMultiRuleRepository.updateByKey(cscpFlowTaskMultiRule);
            response = cscpFlowTaskMultiRule;
        }
        return response;
    }

}
