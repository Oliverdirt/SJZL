package com.ctsi.ssdc.thirdauth.workwx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

/**
 * 关于URL类的方法使用：https://www.runoob.com/java/java-url-processing.html
 */
@Slf4j
public class SendRequest {

    /**
     * 发送GET请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        JSONObject jsonObject = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        InputStream inputStream = null;
        try {
            String urlName = url;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //针对群晖NAS请求，加一个Cookie
            if (session.getAttribute("sid") != null) {
                conn.addRequestProperty("Cookie", "id=" + session.getAttribute("sid"));
            }
            conn.setConnectTimeout(10000);
            // 建立实际的连接
            conn.connect();
            inputStream = conn.getInputStream();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = JSON.parseObject(sb.toString());
        } catch (Exception e) {
            CompletableFuture.runAsync(() -> log.error("SendRequest-sendGet fail"));
        } finally {
            // 使用finally块来关闭输入流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                CompletableFuture.runAsync(() -> log.error("sendGet关闭流异常"));
            }
        }
        return jsonObject;
    }

}
