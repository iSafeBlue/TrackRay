package com.trackray.web.ws;

import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Metasploit;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.StrUtils;
import com.trackray.web.service.PluginService;
import com.trackray.web.utils.wagu.Block;
import com.trackray.web.utils.wagu.Board;
import com.trackray.web.utils.wagu.Table;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * msf 处理类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Component
public class MsfHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MsfHandler.class);

    @Autowired
    private Metasploit metasploit;

    private Map<String, Thread> sessions = Collections.synchronizedMap(new HashMap<String,Thread>());
    private boolean flag;
    @Override
    public  void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        if (StringUtils.isNotEmpty(metasploit.getToken())){
            flag = true;
            String banner = metasploit.banner();
            session.sendMessage(Message.RED(banner));
        }else{
            session.sendMessage(Message.RED("[-]metasploit配置有误，请检查!"));
        }
        sessions.put(session.getId(),null);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (flag){
            String response = metasploit.execute(metasploit.list(payload));
            String console = metasploit.getConsole();
            session.sendMessage(Message.NORMAL("console:"+console));
            session.sendMessage(Message.NORMAL(response));
            while (metasploit.isBusy()) {
                Thread.sleep(1200);
                Map map = metasploit.readResult();
                boolean busy = (boolean) map.get("busy");
                metasploit.setBusy(busy);
                String data = StrUtils.unicodeToString(map.get("data").toString());
                if (StringUtils.isNotEmpty(data)){
                    session.sendMessage(Message.NORMAL(data));
                }
            }
        }

        super.handleTextMessage(session, message);
    }

    @Override
    public  void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        log.info("msfconsole closed.......");
        Thread remove = sessions.remove(session.getId());
        if (remove!=null)
            remove.interrupt();
        if (flag)
            metasploit.close();
        super.afterConnectionClosed(session, closeStatus);

    }

}