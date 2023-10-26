package com.ctsi.ssdc.sensitiveword.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件路径工具类
 * <p>
 * The code change the world !!!
 *
 * @author hx
 * @create 2022-05-13 17:25
 **/
public class PathUtil {

    private static Pattern FilePattern = Pattern.compile("\\.\\./|\\./|<|>");



    /**
     * 适用于文件名，文件路径特殊字符过滤
     * @param str
     * @return
     */
    public static String filePathFilter(String str) {
        if(null == str){ return null;}
        // 判断是否存在特殊字符，如果存在则替换为空字符
        Matcher matcher = FilePattern.matcher(str);
        String filePath = matcher.replaceAll("");
        return filePath;
    }

    /**
     * Contains special characters
     * @param str
     * @return
     */
    public static boolean checkContainsSpecialCharacters(String str){
        return FilePattern.matcher(str).find();
    }

    public static void main(String[] args) {
        String s = "./sssss.xls";
        System.out.println(checkContainsSpecialCharacters(s));
    }
}

