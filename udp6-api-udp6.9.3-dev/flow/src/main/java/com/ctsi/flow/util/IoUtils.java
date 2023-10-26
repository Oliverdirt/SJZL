package com.ctsi.flow.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-14 15:07
 * @description ：
 */
public class IoUtils {

    public static String readUtf8(InputStream in) throws IORuntimeException {
        return StrUtil.utf8Str(IoUtil.read(in));
    }
}
