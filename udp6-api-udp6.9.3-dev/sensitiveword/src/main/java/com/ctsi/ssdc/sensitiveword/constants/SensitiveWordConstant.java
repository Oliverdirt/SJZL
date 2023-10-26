package com.ctsi.ssdc.sensitiveword.constants;

import java.util.regex.Pattern;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-09-15 9:52
 **/
public class SensitiveWordConstant{


    /**
     * excel文件后缀
     */
    public static Pattern EXCELFILEPATTERN = Pattern.compile(".*(\\.xls|\\.xlsx){1}$");


    /**
     * 组件名称
     */
    public final static String SENSITIVEWORD="hx-sensitiveword";

}
