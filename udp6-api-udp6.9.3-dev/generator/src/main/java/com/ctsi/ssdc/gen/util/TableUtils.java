package com.ctsi.ssdc.gen.util;


import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成表单相关信息
 */
public class TableUtils {
    private static final Pattern CAMEL_PATTERN = Pattern.compile("_(\\w)");
    private static final Pattern ENGLISH_LETTER_PATTERN = Pattern.compile("[A-Z]");

    private TableUtils() {
    }

    /**
     *
     * @param str
     * @return
     */
    public static String upperFirstWord(String str) {
        String strNew = camel(new StringBuffer(str)).toString();
        return strNew.substring(0, 1).toUpperCase() + strNew.substring(1);
    }

    public static String lowerFirstWord(String str) {
        String strNew = camel(new StringBuffer(str)).toString();
        return strNew.substring(0, 1).toLowerCase() + strNew.substring(1);
    }

    public static List getPrimaryKeyProperties(List<CscpHxFormColumn> list) {
        List keys = new ArrayList();
        for (CscpHxFormColumn column : list) {
            if (column.getIsPk() == 1) {
                keys.add(lowerFirstWord(column.getColumnName()));
            }
        }
        return keys;
    }

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static StringBuffer camel(StringBuffer str) {
        //利用正则删除下划线，把下划线后一位改成大写
//        Pattern pattern = Pattern.compile("_(\\w)");
//        Matcher matcher = pattern.matcher(str);
        Matcher matcher = CAMEL_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb;
        }
        return camel(sb);
    }


    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static StringBuffer underline(StringBuffer str) {
//        Pattern pattern = Pattern.compile("[A-Z]");
//        Matcher matcher = pattern.matcher(str);
        Matcher matcher = ENGLISH_LETTER_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb;
        }
        return underline(sb);
    }


    public static String getPrimaryKeyParamList() {
        return null;
    }
}
