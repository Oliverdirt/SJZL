package com.ctsi.ssdc.sensitiveword.service.impl;


import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordExample;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordType;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordTypeExample;
import com.ctsi.ssdc.sensitiveword.repository.CscpSensitiveWordTypeRepository;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordService;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordTypeService;
import com.ctsi.ssdc.sensitiveword.util.SensitiveWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing CscpSensitiveWordType.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpSensitiveWordTypeServiceImpl 
	extends StrengthenBaseServiceImpl<CscpSensitiveWordTypeRepository, CscpSensitiveWordType, Long, CscpSensitiveWordTypeExample>
	implements CscpSensitiveWordTypeService {

	@Autowired
	private CscpSensitiveWordService cscpSensitiveWordService;

	@Resource
	private CscpSensitiveWordTypeRepository cscpSensitiveWordTypeRepository;
	
    @Override
	public AjaxResult deleteBysenTypeId(Long senTypeId) {
		CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
		cscpSensitiveWordExample.createCriteria().andSenTypeIdEqualTo(senTypeId);
		PageResult <CscpSensitiveWord> pageResult = cscpSensitiveWordService.findByExample(cscpSensitiveWordExample);
		if(pageResult.getData().size()>0){
			//当前senTypeId 有被使用
			return AjaxResult.error("删除类型失败,当前类型包含正在使用的敏感词!!");
		}else{
			//当前senTypeId 无被使用
			r.deleteByPrimaryKey(senTypeId);
//			cscpSensitiveWordService.deleteByTypeId(senTypeId);
//			SensitiveWordUtil.deleteBatch(pageResult.getData());
			return AjaxResult.success("删除类型成功");
		}

    }

	@Override
	public List<CscpSensitiveWordType> getSensitiveWordTypesListNonempty() {
		return cscpSensitiveWordTypeRepository.getSensitiveWordTypesListNonempty();
	}

	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult deleteByIds(Long[] ids) {

		// 判断敏感词类型是否被占用
		CscpSensitiveWordExample cscpSensitiveWordExample = new CscpSensitiveWordExample();
		cscpSensitiveWordExample.createCriteria().andSenTypeIdIn(Arrays.asList(ids));
		PageResult<CscpSensitiveWord> wordServiceByExample = cscpSensitiveWordService.findByExample(cscpSensitiveWordExample);

		List<Long> delList = new ArrayList<>(Arrays.asList(ids));
		if(wordServiceByExample.getData().size()>0){
			return AjaxResult.error("删除类型失败,当前类型包含正在使用的敏感词!!");
		}else{
			// 批量删除类型
			cscpSensitiveWordTypeRepository.deleteByIds(delList);
			return AjaxResult.success("批量删除类型成功!!");
		}
	}
}
