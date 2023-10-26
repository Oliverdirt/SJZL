package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.service.DicService;
import com.ctsi.ssdc.model.DicForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DicController {
	@Autowired
	private DicService dicService;
	
	@GetMapping("/cscpDicsByCriteria")

	public List<DicForm> getDics(Long dicType) {
		return dicService.selectDicByDicType(dicType);
	}

}
