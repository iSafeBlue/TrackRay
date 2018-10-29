package com.trackray.scanner.plugin;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

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

            send("result="+result);
        }else{
            send("检查未通过");
        }
    }

    @Override
    public void run() {
        build();
    }

    public void send(String text){
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
        }
    }

    public void append(String value){
        //...
        append = value;
    }

    public String getInput(){
        while (append==null){
        }
        String temp = append;
        append=null;
        return temp;
    }

    public void onClose(){}
}
