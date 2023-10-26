package com.ctsi.ssdc.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import net.sf.json.JSONObject;
import static com.ctsi.ssdc.consts.ComponentConstant.UTIL;

/**
 * request工具类
 * @author ctsi
 *
 */
public class RequestUtil {
	/**
	 * 获取URI
	 * @return 请求URI
	 */
	
		public static String getURI() {
		
		return getRequest().getRequestURI();
		
	}
	/**
	 * 获取IP
	 * @return 请求IP
	 */
	

	public static String getIp() {
		
		return getRequest().getRemoteHost();
		
	}
	/**
	 * 获取请求参数
	 * @return 请求参数的JSON字符串
	 */
	

	public static String getParams() {
		
		return JSONObject.fromObject(getRequest().getParameterMap()).toString();
		
	}

	/**
	 * 获取REQUEST
	 * @return HttpServletRequest
	 */
	

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
	}
}
