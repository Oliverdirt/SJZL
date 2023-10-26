package com.ctsi.ssdc.sensitiveword.web;

import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.sensitiveword.annotation.SysSensitiveWord;
import com.ctsi.ssdc.sensitiveword.config.SensitiveWordConfig;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The code change the world !!!
 *
 * @author hx
 * @create 2022-09-15 14:22
 **/
@Api(tags = "敏感词测试", value = "敏感词测试")
@RestController
@RequestMapping("/api/system/test")
@Slf4j
public class CscpSensitiveWordTestController {

    @Autowired
    private SensitiveWordConfig sensitiveConfig;

    @GetMapping("/testMethodAno")
    @SysSensitiveWord
    public AjaxResult testMethodAno(String str,String str2){
        List<String> res = new ArrayList<>();
        res.add(str);
        res.add(str2);
        return AjaxResult.success("ok",res);
    }

    @GetMapping("/testParamObj")
    @SysSensitiveWord
    public AjaxResult testParamObj(CscpSensitiveWord sensitiveWord){
        return AjaxResult.success("ok",sensitiveWord);
    }

    @PostMapping("/testPost")
    @SysSensitiveWord
    public AjaxResult testPost( @RequestBody CscpSensitiveWord sensitiveWord){
        return AjaxResult.success("ok",sensitiveWord);
    }

    @PutMapping("/testPut")
    @SysSensitiveWord
    public AjaxResult testPut( @RequestBody CscpSensitiveWord sensitiveWord){
        return AjaxResult.success("ok",sensitiveWord);
    }

    @GetMapping("/{str}/testPathVar")
    @SysSensitiveWord
    public AjaxResult testPathVar( @PathVariable("str") String str){
        return AjaxResult.success("ok",str);
    }

    @PostMapping("/testGolbelPost")
    public AjaxResult testGolbelPost( @RequestBody CscpSensitiveWord sensitiveWord){
        return AjaxResult.success("ok"+sensitiveConfig.isGlobalEnable(),sensitiveWord);
    }

}
