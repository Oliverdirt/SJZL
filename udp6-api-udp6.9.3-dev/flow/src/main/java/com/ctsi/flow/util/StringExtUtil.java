package com.ctsi.flow.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-27 10:39
 * @description ：
 */
public class StringExtUtil {

    public static Set<String> convertToSet( String str) {
        str = str.replace("[","").replace("]","");
        String[] split = str.split(",");
        Set<String> set = Arrays.stream(split).collect(Collectors.toSet());
        return set;
    }
}
