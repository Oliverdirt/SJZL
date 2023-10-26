package com.ctsi.ssdc.util;

import org.apache.commons.lang.StringUtils;
import java.util.regex.Pattern;

/**
 * BASE64
 * @author lym
 */
public class Base64Util {
	
	/**
     * 判断字符串是否为BASE64编码图片
     * 格式：data:image/{图片类型};base64,{BASE64编码}
     * 1.第一部分，第一个分号前为图片类型,data:image/{图片类型}，图片类型png, jpeg, gif, jpg
     * 2.第二部分，第一个逗号前base64
     * 3.第三部分
     * 	3.1 字符串只可能包含A-Z，a-z，0-9，+，/，=字符
     * 	3.2 字符串长度是4的倍数
     *  3.3 =只会出现在字符串最后，可能没有或者一个等号或者两个等号
     * @param str
     * @return 是BASE64编码则返回true
     */

    public static boolean validateBASE64(String str) {
    	if(StringUtils.isNotBlank(str)) {
    		String base64Pattern = "^data:image/(png|jpeg|gif|jpg);base64,([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
         	return Pattern.matches(base64Pattern, str);
    	}
    	return false;
    }
    
}
