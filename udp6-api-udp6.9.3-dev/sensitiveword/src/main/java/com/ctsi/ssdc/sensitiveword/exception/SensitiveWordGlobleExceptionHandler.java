package com.ctsi.ssdc.sensitiveword.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * The code change the world !!!
 *
 * @author hx
 * @create 2022-08-24 15:40
 **/
@ControllerAdvice(basePackages = "com.ctsi.ssdc.sensitiveword.web")
@Slf4j
public class SensitiveWordGlobleExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> vaildExceptionHandler(MethodArgumentNotValidException ex){
        log.error("参数校验出现异常", ex.getMessage(),ex.getClass());
        Map<String,String> resultMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().stream().forEach((item)->{
            resultMap.put(item.getField(),item.getDefaultMessage());
        });
        Map<String,String> erroMsgMap = new HashMap<>();
        erroMsgMap.put("msg","参数校验出错："+ JSONObject.toJSONString(resultMap));
        return ResponseEntity.status(444).body(erroMsgMap);
    }
}
