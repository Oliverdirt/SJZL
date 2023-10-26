package com.ctsi.ssdc.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctsi.ssdc.admin.repository.DicDao;
import com.ctsi.ssdc.admin.service.DicService;
import com.ctsi.ssdc.model.DicForm;

@Service
public class DicServiceImpl implements DicService {

	@Autowired
	private DicDao userDao;

	@Override
	public List<DicForm> selectDicByDicType(Long dicType) {
		return userDao.selectDicByDicType(dicType);
	}

}
