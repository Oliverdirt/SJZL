package com.ctsi.ssdc.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpClient工具类
 * @author fuxiang,liyongsheng
 *
 */
public class HttpClientUtil {
	/*
	* 主要修改问题
    get请求参数的转码问题，如/会转成%2F
    被请求端报错的处理
    请求参数中出现null或者“”的处理
    * 2019/09/23
	* */

	public static final String REQUEST_TYPE_GET = "get";
	public static final String REQUEST_TYPE_POST = "post";

	/**
	 * 模拟请求,默认UTF-8编码
	 *
	 * @param url
	 * @param method
	 * @param params
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String requestDefault(String url, String method, Map<String, String> params,
										Map<String, String> headers) throws ClientProtocolException, IOException {

		// 默认UTF-8编码
		return request(url, method, params, headers, CharEncoding.UTF_8);
	}

	/**
	 * 模拟请求
	 *
	 * @param url
	 * @param method
	 * @param json
	 * @param headers
	 * @return
	 */
	public static String request(String url, String method, Map<String, String> params, Map<String, String> headers,
								 String encoding) throws ClientProtocolException, IOException {

		if (HttpClientUtil.REQUEST_TYPE_GET.equals(method)) {

			// get请求
			return doGetAddHeaderJson(url, params, headers, encoding);

		} else if (HttpClientUtil.REQUEST_TYPE_POST.equals(method)) {

			// post请求
			return doPostAddHeaderJson(url, params, headers, encoding);

		} else {

			// 目前只支持两种请求
			return null;

		}
	}

	/**
	 * get
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doGetAddHeaderJson(String url, Map<String, String> params, Map<String, String> headers,
											String encoding) throws ClientProtocolException, IOException {

		// 创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 处理参数
		if (MapUtils.isNotEmpty(params)) {
			if (url.indexOf("?") != -1) {
				url += "&";
			} else {
				url += "?";
			}
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
				if(StringUtils.isNotEmpty(stringStringEntry.getValue())) {
					sb.append(stringStringEntry.getKey() + "=" + URLEncoder.encode(stringStringEntry.getValue(),encoding) + "&");
				}
			}
			url += sb.append(sb.toString());
			url = url.substring(0, url.length() - 1);
		}

		HttpGet httpGet = new HttpGet(url);

		// 增加header信息
		if (MapUtils.isNotEmpty(headers)) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpGet.setHeader(header.getKey(), header.getValue());
			}
		}

		CloseableHttpResponse response = httpClient.execute(httpGet);

		// 处理结果
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		String resultString = EntityUtils.toString(response.getEntity(), encoding);
		return resultString;

	}

	/**
	 * post
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doPostAddHeaderJson(String url, Map<String, String> params, Map<String, String> headers,
											 String encoding) throws ClientProtocolException, IOException {

		// 创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);

		// 处理参数
		if (MapUtils.isNotEmpty(params)) {
			List<NameValuePair> paramList = new ArrayList<>();
			for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
				if(StringUtils.isNotEmpty(stringStringEntry.getValue())) {
					paramList.add(new BasicNameValuePair(stringStringEntry.getKey(), stringStringEntry.getValue()));
				}
			}
			// 模拟表单
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, encoding);
			httpPost.setEntity(entity);
		}

		// 增加header信息
		if (MapUtils.isNotEmpty(headers)) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpPost.setHeader(header.getKey(), header.getValue());
			}
		}

		// 处理结果
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		String resultString = EntityUtils.toString(response.getEntity(), encoding);
		return resultString;

	}

}
