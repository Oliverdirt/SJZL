package com.ctsi.ssdc.admin.service;

import java.util.List;

import com.ctsi.ssdc.model.DicForm;

public interface DicService {
	List<DicForm> selectDicByDicType(Long dicType);

}
