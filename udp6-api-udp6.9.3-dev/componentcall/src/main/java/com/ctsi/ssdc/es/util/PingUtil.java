package com.ctsi.ssdc.es.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * java Ping ip 工具类
 */
public class PingUtil {

    private static final Logger log = LoggerFactory.getLogger(PingUtil.class);

    /**
     * 检测 ip 和 端口 是否能连接
     *
     * @param host
     * @param port
     * @param timeOut 多少毫秒超时
     * @return
     */
    public static boolean connect(String host, int port, int timeOut) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), timeOut);
            //boolean res = socket.isConnected();//通过现有方法查看连通状态
        } catch (IOException e) {
            //当连不通时，直接抛异常，异常捕获即可
            return false;
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error("failed to connect caused by:{} ", e);
            }
        }
        return true;
    }
}
