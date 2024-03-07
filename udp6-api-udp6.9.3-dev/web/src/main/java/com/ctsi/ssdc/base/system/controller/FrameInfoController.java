package com.ctsi.ssdc.base.system.controller;

import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoUpdateBo;
import com.ctsi.ssdc.base.system.domain.vo.FrameInfoVo;
import com.ctsi.ssdc.base.system.service.IFrameInfoService;
import com.ctsi.ssdc.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 框架管理模块
 * @author
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/frameInfo")
public class FrameInfoController {

    @Autowired
    private IFrameInfoService iFrameInfoService;

    /**
     * 添加框架数据
     * @param bo
     * @return
     */
    @PostMapping("/insertFrameInfo")
    public R<String> insertFrameInfo(@RequestBody FrameInfoBo bo) {
        iFrameInfoService.insertFrameInfo(bo);
        return R.ok("");
    }


    /**
     * 修改框架数据
     * @param bo
     * @return
     */
    @PostMapping("/updateFrameInfo")
    public R<Boolean> updateFrameInfo(@RequestBody FrameInfoBo bo) {
        return R.ok(iFrameInfoService.updateFrameInfo(bo));
    }

    /**
     * 根据类型查询框架数据
     * @param bo
     * @return
     */
    @PostMapping("/getInfoByType")
    public R<FrameInfoVo> getInfoByType(@RequestBody FrameInfoUpdateBo bo) {
        return R.ok(iFrameInfoService.getInfoByType(bo));
    }

}
