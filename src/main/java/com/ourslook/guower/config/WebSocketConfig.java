package com.ourslook.guower.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author dazer
 * Web Socket使用
 *
 * https://github.com/lenve/JavaEETest/blob/master/Test20-WebSocket
 * 在Spring Boot框架下使用WebSocket实现消息推送
 * http://www.ruanyifeng.com/blog/2017/05/websocket.html
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        /**
         * 添加端点来建立webscoket连接
         * http://localhost:8981/guower/endpointSang/info html客户端webscoket建立连接请求的方法
         */
        stompEndpointRegistry.addEndpoint("/endpointSang").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }
}
