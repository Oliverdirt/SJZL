package com.ctsi.ssdc.thirdauth.tianyi.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

/**
* @author: hx
* @description: 简单发送请求工具类
* @date: Created in 12:21 2018/7/24
*/
public class HttpUtil {

	public static String doHttpPost(String url, Map<String, String> req) {
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> entry : req.entrySet()){
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		return doHttpPost(url, sb.toString());
	}
	
	/**
	 * HTTP POST
	 * 
	 * @param url
	 *            请求地址
	 * @param req
	 *            请求参数，格式：name1=value1&name2=value2...
	 * @return 应答参数
	 */
	public static String doHttpPost(String url, String req) {
		PrintWriter outPrintWriter = null;
		BufferedReader inBufferedReader = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream stream = null;
		try {
			URLConnection urlConnection = new URL(url).openConnection(); // 打开和URL之间的连接
			// 设置通用的请求属性
			urlConnection.setRequestProperty("accept", "*/*");
			urlConnection.setRequestProperty("connection", "Keep-Alive");
			urlConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);

			urlConnection.connect();
			outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
			outPrintWriter = new PrintWriter(outputStreamWriter);
			outPrintWriter.print(req);
			outPrintWriter.flush();
			stream = urlConnection.getInputStream();
			inBufferedReader = new BufferedReader(new InputStreamReader(
					stream, "UTF-8"));
			String line = "";
			StringBuffer response = new StringBuffer();
			while ((line = inBufferedReader.readLine()) != null) {
				response.append(line);
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (outPrintWriter != null) {
					outPrintWriter.close();
				}
				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}
				if (inBufferedReader != null) {
					inBufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}