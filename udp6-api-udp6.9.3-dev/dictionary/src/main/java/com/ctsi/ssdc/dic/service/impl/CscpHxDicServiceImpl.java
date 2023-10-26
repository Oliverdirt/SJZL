package com.ctsi.ssdc.dic.service.impl;

import com.ctsi.ssdc.dic.domain.CscpHxDic;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.dic.repository.CscpHxDicItemRepository;
import com.ctsi.ssdc.dic.repository.CscpHxDicRepository;
import com.ctsi.ssdc.dic.service.CscpHxDicService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing CscpHxDic.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxDicServiceImpl
	extends StrengthenBaseServiceImpl<CscpHxDicRepository, CscpHxDic, Long, CscpHxDicExample>
	implements CscpHxDicService {
	@Autowired
	private CscpHxDicRepository cscpHxDicRepository;
	@Autowired
	private CscpHxDicItemRepository cscpHxDicItemRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByIds(Long[] dicIds) {
		List<Long> delList = new ArrayList<>(Arrays.asList(dicIds));
		cscpHxDicItemRepository.deleteByDicIds(delList);
		// 批量删除
		cscpHxDicRepository.deleteByIds(delList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteById(Long dicId) {
		cscpHxDicItemRepository.deleteByDicId(dicId);
		cscpHxDicRepository.deleteByPrimaryKey(dicId);
	}
	@Override
	public int checkCscpHxDicName(CscpHxDic cscpHxDic) {
		if(cscpHxDic.getDicId() == null){
			return cscpHxDicRepository.checkCscpHxDicName(cscpHxDic);
		}else{
			CscpHxDic cscpHxDic1 = cscpHxDicRepository.selectByPrimaryKey(cscpHxDic.getDicId());
			if(cscpHxDic1.getDicName().equals(cscpHxDic.getDicName())){
				return 0;
			}else{
				return cscpHxDicRepository.checkCscpHxDicName(cscpHxDic);
			}
		}

	}

	@Override
	public int checkCscpHxDicCode(CscpHxDic cscpHxDic) {
		if(cscpHxDic.getDicId() == null){
			return cscpHxDicRepository.checkCscpHxDicCode(cscpHxDic);
		}else{
			CscpHxDic cscpHxDic1 = cscpHxDicRepository.selectByPrimaryKey(cscpHxDic.getDicId());
			if(cscpHxDic1.getDicCode().equals(cscpHxDic.getDicCode())){
				return 0;
			}else{
				return cscpHxDicRepository.checkCscpHxDicCode(cscpHxDic);
			}
		}
	}
}
