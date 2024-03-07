package com.ctsi.ssdc.api.center.controller;

import com.ctsi.ssdc.api.center.domain.dto.DeptDTO;
import com.ctsi.ssdc.api.center.domain.dto.DicInfoDTO;
import com.ctsi.ssdc.api.center.domain.dto.SystemDictDTO;
import com.ctsi.ssdc.api.center.service.SystemService;
import com.ctsi.ssdc.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 统一用户系统中内容
 * @Author Len
 * @Date 2023/6/6 15:05
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {


    @Autowired
    private SystemService systemService;

    /**
     * 获取系统信息
     * @return
     */
    @GetMapping("/getSystemDictInfo")
    public R<List<SystemDictDTO>> getSystemDictInfo() {
        return R.ok(systemService.getSystemDictInfo());
    }

    /**
     * 获取部门信息
     * @return
     */
    @GetMapping("/getDeptInfo")
    public R<List<DeptDTO>> getDeptInfo() {
        return R.ok(systemService.getDeptInfo());
    }

    /**
     * 获取字典信息
     * @param dicType
     * @return
     */
    @GetMapping("/getDicInfoByDicType")
    public R<List<DicInfoDTO>> getDicInfoByDicType(@RequestParam("dicType") String dicType) {
        return R.ok(systemService.getDicInfoByDicType(dicType));
    }

}
