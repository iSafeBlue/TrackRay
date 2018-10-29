package com.trackray.rest.ws;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.trackray.rest.service.PluginService;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.plugin.WebSocketPlugin;
import com.trackray.scanner.utils.StrUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import org.springframework.web.socket.handler.TextWebSocketHandler;


public class PluginHandler extends TextWebSocketHandler {


    private static final Logger log = Logger.getLogger(PluginHandler.class);

    @Autowired
    private PluginService pluginService;

    private Map<String,String> uses = new ConcurrentHashMap<>();
    private Map<String,Map<String,String>> sets = new ConcurrentHashMap<>();
    private Map<String,Map<String,Object>> plugins = new ConcurrentHashMap<>();

    public static String banner = "\n _______             _    _____             \n" +
            "|__   __|           | |  |  __ \\            \n" +
            "   | |_ __ __ _  ___| | _| |__) |__ _ _   _ \n" +
            "   | | '__/ _` |/ __| |/ /  _  // _` | | | |\n" +
            "   | | | | (_| | (__|    | | \\ \\ (_| | |_| |\n" +
            "   |_|_|  \\__,_|\\___|_|\\_\\_|  \\_\\__,_|\\__, |\n" +
            "                                       __/ |\n" +
            "                                      |___/ \n";

    @Override
    public  void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        session.sendMessage(new TextMessage(""+banner+""));
        session.sendMessage(new TextMessage(StrUtils.formatJson(pluginService.findPlugins().toString())));
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();
        if (message.getPayloadLength()>0){
            String payload = message.getPayload();
            if (payload.startsWith("use ")){
                String[] args = payload.split(" ");
                String key = args[1];
                if (StringUtils.isNotBlank(key) && pluginService.usePlugin(session,key)){
                    uses.put(id,key);
                    Map<String, String> map = new ConcurrentHashMap<>();
                    sets.put(id,map);
                }
            }else if (payload.startsWith("set ")){
                String[] args = payload.split(" ");
                String param = args[1];
                String value = args[2];
                String key = null;
                if (StringUtils.isNoneBlank(param,value,(key = uses.get(id))) ){
                    sets.get(id).put(param,value);
                }
            }else if (payload.equals("build")){
                if (StringUtils.isNotBlank(uses.get(id)) && sets.get(id).size()>0){
                    Map<String, String> map = sets.get(id);
                    String key = uses.get(id);
                    WebSocketPlugin plugin = pluginService.buildPlugin(session,map,key);
                    if (plugin!=null){
                        Rule rule = plugin.getClass().getAnnotation(Rule.class);
                        Map<String, Object> classMap = new ConcurrentHashMap<String, Object>();
                        if (rule.sync()){
                            Thread thread = new Thread(plugin);
                            thread.start();
                            classMap.put("thread",thread);
                            classMap.put("plugin",plugin);
                            plugins.put(id,classMap);
                        }else{
                            plugin.build();
                            classMap.put("plugin",plugin);
                            plugins.put(id,classMap);
                        }
                    }
                }
            }/*else if(payload.startsWith("append ")){
                String value = payload.substring(payload.indexOf(" ") + 1, payload.length());
                if (uses.get(id)!=null && plugins.get(id).size()==2){
                    Map<String, Object> classMap = plugins.get(id);
                    WebSocketPlugin plugin = (WebSocketPlugin) classMap.get("plugin");
                    plugin.append(value);
                }
            }*/else if (payload.equals("kill")){
                kill(id);
            }else{
                if (uses.get(id)!=null && plugins.get(id).size()==2){
                    Map<String, Object> classMap = plugins.get(id);
                    WebSocketPlugin plugin = (WebSocketPlugin) classMap.get("plugin");
                    plugin.append(payload);
                }else {
                    session.sendMessage(new TextMessage("command not found"));
                }
            }


        }
        super.handleTextMessage(session, message);

    }

    @Override
    public  void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        log.info("connect websocket closed.......");
        String id = session.getId();
        kill(id);
        super.afterConnectionClosed(session, closeStatus);

    }

    private void kill(String id) {
        Map<String, Object> plugin = plugins.remove(id);
        if (plugin!=null && plugin.containsKey("plugin")){
            WebSocketPlugin p = (WebSocketPlugin) plugin.remove("plugin");
            if (p!=null)
                p.onClose();
        }
        if (plugin!=null && plugin.containsKey("thread")){
            Thread t = (Thread) plugin.remove("thread");
            if (t!=null && t.getState() != Thread.State.TERMINATED){
                t.interrupt();
            }
        }
        sets.remove(id);
        uses.remove(id);
    }


}