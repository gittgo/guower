package com.ourslook.guower.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by sang on 16-12-22.
 *
 * https://github.com/lenve/JavaEETest/blob/master/Test20-WebSocket
 *
 * 在Spring Boot框架下使用WebSocket实现消息推送
 *
 * WebSocket与消息推送 https://www.cnblogs.com/best/archive/2016/09/12/5695570.html
 */
@Controller
public class WsController {
    /**
     * MessageMapping 是客户端向服务器发送消息
     */
    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public ResponseMessage say(RequestMessage message) {
        System.out.println(message.getName());
        return new ResponseMessage("welcome," + message.getName() + "！！");
    }
}