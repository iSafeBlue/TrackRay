package com.trackray.web.config;

import com.trackray.web.ws.HandshakeInterceptor;
import com.trackray.web.ws.MsfHandler;
import com.trackray.web.ws.PluginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/9 13:38
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private PluginHandler pluginHandler;
    @Autowired
    private MsfHandler msfHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(pluginHandler, "plugin.ws").
                setAllowedOrigins("*");
        registry.addHandler(msfHandler, "msf.ws").
                setAllowedOrigins("*");
    }



}