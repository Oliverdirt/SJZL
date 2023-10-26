package com.ctsi.ssdc.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 针对HttpRequest的工具类
 * 
 * @author liminqiang
 *
 */
public class HttpRequestUtil {
	/**
	 * 校验ip是否效
	 * @param ip IP
	 * @return 是否有效
	 */
	private static boolean checkIp(String ip) {
		if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip) || "unknown".equalsIgnoreCase(ip)) {
			return false;
		}
		return true;
	}

	/**
	 * 从request请求的头信息里获取客户端IP地址
	 * @param request 请求
	 * @return 客户端IP地址
	 */
	

	public static String queryClientIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (!checkIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (!checkIp(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				// 根据网卡取本机配置的IP
				try {
					ip = InetAddress.getLocalHost().getHostAddress();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (!checkIp(ip)) {
			ip = request.getRemoteAddr();
		}
		/*
		 * 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() = 15
		 */
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}

		return ip;
	}

	/**
	 * 获取本地机器IP地址
	 * @return 本地IP地址
	 */
	

	public static String queryLocalIPAddress() {
		InetAddress inet = null;
		try {
			inet = InetAddress.getLocalHost();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inet != null ? inet.getHostAddress() : null;
	}

	/**
	 * 提取请求中的所有参数
	 * @param request 请求
	 * @return 参数MAP
	 */
	

	public static Map<String, Object> queryParameterMap(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
		if (map != null && !map.isEmpty()) {
			for (Map.Entry<String, String[]> stringEntry : map.entrySet()) {
				String[] values = stringEntry.getValue();
				if (values == null) {
					continue;
				}
				if (values.length == 1) {
					result.put(stringEntry.getKey(), values[0]);
				} else {
					result.put(stringEntry.getKey(), Arrays.toString(values));
				}
			}
		}
		return result;
	}
	
	/**
	 * 校验IP地址是否为IPV6
	 * @param address IP地址
	 * @return 是否为IPV6
	 */
	

	public static boolean isIPv6(String address) {
		boolean result = false;
		String regHex = "(\\p{XDigit}{1,4})";

		// 没有双冒号
		String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "";

		// 双冒号在中间或者没有双冒号
		String regIPv6AbWithColon = "^(" + regHex + "(:|::)){0,6}" + regHex + "";

		// 双冒号开头
		String regIPv6AbStartWithDoubleColon = "^(" + "::(" + regHex + ":){0,5}" + regHex + ")";

		String regIPv6 = "^(" + regIPv6Full + ")|(" + regIPv6AbStartWithDoubleColon + ")|(" + regIPv6AbWithColon + ")";

		// System.out.println("regIPv6 =" + regIPv6);
		// regIPv6
		// =^(^((\p{XDigit}{1,4}):){7}(\p{XDigit}{1,4})$)|(^(::((\p{XDigit}
		// {1,4}):){0,5}(\p{XDigit}{1,4}))$)|(^((\p{XDigit}{1,4})(:|::)){0,6}(\p
		// {XDigit}{1,4})$)$
		// 等价于^(^::$)|(^([\\d|a-fA-F]{1,4}:){7}([\\d|a-fA-F]{1,4})$)|(^(::
		// (([\\d|a-fA-F]{1,4}):){0,5}([\\d|a-fA-F]{1,4}))$)|(^(([\\d|a-fA-F]{1,4})
		// (:|::)){0,6}([\\d|a-fA-F]{1,4})$)$

		// 下面还要处理地址为::的情形和地址包含多于一个的::的情况（非法）
		if (address.indexOf(":") != -1) {
			if (address.length() <= 39) {
				String addressTemp = address;
				int doubleColon = 0;
				if ("::".equals(address)) {
					return true;
				}
				while (addressTemp.indexOf("::") != -1) {
					addressTemp = addressTemp.substring(addressTemp.indexOf("::") + 2, addressTemp.length());
					doubleColon++;
				}
				if (doubleColon <= 1) {
					result = address.matches(regIPv6);
				}
			}
		}
		return result;
	}
	
	/**
	 * 校验请求地址是否IPV6
	 * @param request http请求
	 * @return 是否为IPV6地址
	 */
	

	public static boolean isIpv6(HttpServletRequest request) {
		try {
			String clientAddr = queryClientIPAddress(request);
			if (clientAddr != null && clientAddr.contains("%")) {
				clientAddr = clientAddr.substring(0, clientAddr.indexOf("%"));
			}
			if (clientAddr != null && isIPv6(clientAddr)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
