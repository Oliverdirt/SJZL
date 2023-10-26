package com.ctsi.monitor.websocket.config;

import com.ctsi.monitor.websocket.domain.LoggerMessage;
import com.ctsi.monitor.websocket.domain.LoggerQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;


/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 *
 * @author cczz
 * @version 1.0  2022/7/8 9:04  by xx
 */
@Configuration
@EnableWebSocketMessageBroker
public class LogWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*")
                .withSockJS();
    }

    // 配置消息代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 推送日志到/topic/pullLogger
     */
    @PostConstruct
    public void pushLogger() {
        ExecutorService executorService = newFixedThreadPool(2);
        Runnable runnable = () -> {
            while (true) {
                try {
                    LoggerMessage log = LoggerQueue.getInstance().poll();
                    if (log != null) {
                        if (messagingTemplate != null) {
                            messagingTemplate.convertAndSend("/topic/pullLogger", log);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(runnable);
    }
}
