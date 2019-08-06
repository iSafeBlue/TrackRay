package com.trackray.web.service;

import com.trackray.base.bean.ResultCode;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.plugin.WebSocketPlugin;
import net.sf.json.JSONArray;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PluginService {
    JSONArray findPlugins();

    Map<String,MVCPlugin> findMVCPlugins();

    void usePlugin(Map<String, String> map, HttpServletRequest request, HttpServletResponse response) throws IOException;

    void getPlugin(String task, HttpServletResponse response) throws IOException;

    boolean usePlugin(WebSocketSession session, String key) throws IOException;

    WebSocketPlugin buildPlugin(WebSocketSession session, Map<String, String> map, String key);

    ResultCode killPlugin(String task);

    ModelAndView fetchPlugin(String plugin, String function, Map<String, String> map, ModelAndView model, HttpServletRequest request, HttpServletResponse response);

    List<Map> findWebsocketPlugins();
}
