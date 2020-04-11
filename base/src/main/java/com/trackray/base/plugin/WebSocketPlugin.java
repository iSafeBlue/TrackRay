package com.trackray.base.plugin;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Scanner;

/**
 * websocket插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class WebSocketPlugin extends  AbstractPlugin implements Runnable {

    private WebSocketSession session;
    protected volatile String append;

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public void build() {
        if (session==null)
            return;
        if (check(param)){
            before();
            result = start();

            if (session.isOpen() && result!=null)
                println(result.toString());
        }else{
            println(errorMsg);
        }
    }

    @Override
    public void run() {
        build();
    }

    public void sendColorMsg(WebSocketMessage msg){

        if (session!=null &&  session.isOpen())
        {
            synchronized (session){
                try {
                    session.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println(msg.getPayload());
        }
    }

    public void print(String text){
        sendMessage(text);
    }

    public void println(String text){
        sendMessage("\n"+text);
    }

    private void sendMessage(String text){
        TextMessage msg = new TextMessage(text);
        if (session!=null &&  session.isOpen())
        {
            synchronized (session){
                try {
                    session.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println(text);
        }
    }

    public void append(String value){
        //...
        append = value;
    }

    public String getInput(){
        if (session==null)
        {
            System.out.print("请输入:");
            String next = new Scanner(System.in).next();
            return next;
        }
        while (append==null){
        }
        String temp = append;
        append=null;
        return temp;
    }

    public void onClose(){}


}
