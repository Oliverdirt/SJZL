package com.ctsi.ssdc.sensitiveword.filter;

import com.ctsi.ssdc.sensitiveword.config.SensitiveWordConfig;
import com.ctsi.ssdc.sensitiveword.util.SensitiveWordUtil;
import com.ctsi.ssdc.wrapper.CustomHttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


/**
 * 敏感词过滤器
 *
 * @author hx
 */
@Component
@WebFilter(urlPatterns = "/api/system/test/*", filterName = "sensitiveWordFilter")
public class SensitiveWordFilter extends OncePerRequestFilter {

	@Autowired
	private SensitiveWordConfig sensitiveConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (sensitiveConfig.isGlobalEnable()) {
			String method = request.getMethod();
			if(null != method && ("POST".equals(method.toUpperCase()) || "PUT".equals(method.toUpperCase()) || "DELETE".equals(method.toUpperCase()))) {
				String reuestUri = request.getRequestURI();
				if(reuestUri.indexOf("api/system/cscpSensitiveWords") == -1 &&
						reuestUri.indexOf("api/system/matchCscpSensitiveWord") == -1) {
					//敏感词本身及匹配功能不过滤
					String head = request.getHeader("Content-Type");
					if (head != null) {
						if (!head.contains("application/x-www-form-urlencoded")) {
							// payload
							// 核心,替换了request
							request = new SensitiveWordPayloadRequest(request);
						} else {
							// 获取前端传递的所有参数名的枚举
							Enumeration<String> pNames = request.getParameterNames();
							while (pNames.hasMoreElements()) {
								// 遍历枚举
								// 获取参数名
								String name = pNames.nextElement();
								// 获取参数值
								String value = request.getParameter(name);
								// 对参数值进行敏感词处理,并重新设置到request
								String str = SensitiveWordUtil.replaceSensitiveWord(value, "***");
								request.setAttribute(name, str);
							}
						}
					}
				}
			}
		}
		ServletRequest requestWrapper = null;
		if(request instanceof HttpServletRequest) {
			requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) request);
		}
		if(requestWrapper == null) {
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(requestWrapper, response);
		}
	}

}
