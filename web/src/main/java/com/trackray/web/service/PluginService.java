package com.trackray.web.service;

import com.trackray.base.bean.ResultCode;
import com.trackray.base.plugin.WebSocketPlugin;
import net.sf.json.JSONArray;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface PluginService {
    JSONArray findPlugins();

    void usePlugin(Map<String, String> map, HttpServletResponse response) throws IOException;

    void getPlugin(String task, HttpServletResponse response) throws IOException;

    boolean usePlugin(WebSocketSession session, String key) throws IOException;

    WebSocketPlugin buildPlugin(WebSocketSession session, Map<String, String> map, String key);

    ResultCode killPlugin(String task);

}
