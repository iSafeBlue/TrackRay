package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(value = "netcat", title = "NetCat 简易版" , author = "浅蓝")
@Rule(params = {@Param(key = "port",defaultValue = "10010" , desc = "监听的端口，尽量避免小于10000的端口"),},
        sync = true,  websocket = true )
public class NetCat extends WebSocketPlugin {
    @Override
    public boolean check(Map param) {
        port = Integer.parseInt(param.get("port").toString());
        if (port<10000){
            return false;
        }
        return true;
    }
    private int port;
    private ServerSocket server;
    private Socket client;
    private ExecutorService pool;
    private OutputStream os;
    private PrintWriter pw;
    @Override
    public void before() {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool = Executors.newSingleThreadExecutor();
        send("port:"+port);
        send("等待客户端连接");
        listen();
    }

    private void listen() {
        try {
            client = server.accept();
            if (client.isConnected()){
                send("客户端已连接:"+client.getInetAddress().getHostAddress());
                os = client.getOutputStream();
                pw = new PrintWriter(os, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    echo();
                }
            }
        });
    }

    private void echo() {
        try {
            if (client.isConnected()){
                InputStream is = client.getInputStream();
                BufferedReader bri = new BufferedReader(new InputStreamReader(
                        is));
                String msg ="";
                String temp;
                while ((temp = bri.readLine())!=null && !"".equals(temp)) {
                    //msg=msg.concat(temp);
                    send(temp);
                }
                /*if (StringUtils.isNotBlank(msg)){
                    send(msg);
                }*/

            }
        }catch (Exception e){
            send(e.getMessage());
        }
    }

    @Override
    public Object start() {

        while (client!=null && client.isConnected()){
            String input = getInput();
            if (StringUtils.isNotBlank(input)){
                pw.println(input);
            }
        }
        send("主机已下线");
        return null;
    }

    @Override
    public void onClose() {
        try {
            pool.shutdownNow();
            if (client!=null && !client.isClosed())
            {
                client.close();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
