package com.ctsi.monitor.websocket.domain;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/7/8 8:30  by xx
 */
public class LoggerQueue {
    //队列大小
    private static final int QUEUE_MAX_SIZE = 10000;
    private static LoggerQueue alarmMessageQueue = new LoggerQueue();
    //阻塞队列
    private BlockingQueue blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private LoggerQueue() {
    }

    public static LoggerQueue getInstance() {
        return alarmMessageQueue;
    }

    /**
     * 消息入队
     * @param log
     * @return
     */
    public boolean push(LoggerMessage log) {
        //队列满了就抛出异常，不阻塞
        return this.blockingQueue.add(log);
    }

    /**
     * 消息出队
     * @return
     */
    public LoggerMessage poll() {
        LoggerMessage result = null;
        try {
            result = (LoggerMessage) this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
