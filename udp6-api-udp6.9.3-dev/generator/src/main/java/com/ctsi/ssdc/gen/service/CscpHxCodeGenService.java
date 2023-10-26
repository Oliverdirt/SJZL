package com.ctsi.ssdc.gen.service;

import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-02-23 15:32
 * @description：
 * @modified By：
 * @version: version$
 */
public interface CscpHxCodeGenService {

    /**
     * description: 代码生成
     */
    public byte[] downloadCode(Long tableId);

    Map<String, String> preview(Long tableId);
}
