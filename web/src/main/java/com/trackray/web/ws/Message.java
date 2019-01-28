package com.trackray.web.ws;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/9 14:13
 */
public class Message{
    private static WebSocketMessage msg(String msg){
        return new TextMessage(msg);
    }
    public static WebSocketMessage NORMAL(String msg){
        return msg(msg);
    }

    public static WebSocketMessage GREEN(String msg){
        msg = "<font color='green'>"+msg+"</font>";
        return msg(msg);
    }
    public static WebSocketMessage RED(String msg){
        msg = "<font color='red'>"+msg+"</font>";
        return msg(msg);
    }
    public static WebSocketMessage BLUE(String msg){
        msg = "<font color='blue'>"+msg+"</font>";
        return msg(msg);
    }
    public static WebSocketMessage WHITE(String msg){
        msg = "<font color='white'>"+msg+"</font>";
        return msg(msg);
    }
    public static WebSocketMessage YELLOW(String msg){
        msg = "<font color='yellow'>"+msg+"</font>";
        return msg(msg);
    }

    public static WebSocketMessage BLACK(String msg){
        msg = "<font color='black'>"+msg+"</font>";
        return msg(msg);
    }

}
