package com.ctsi.flow.util;

import com.ctsi.flow.param.PageParam;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-28 10:42
 * @description ：
 */


public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPage() - 1) * pageParam.getSize();
    }
}
