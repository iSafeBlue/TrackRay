package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Python;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.Message;
import com.trackray.module.inner.XunfengInner;
import org.apache.commons.lang3.StringUtils;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/23 21:20
 */
@Plugin(value = "xunfeng",title = "巡风插件扫描" , author = "浅蓝")
@Rule(auth = true , sync = true, websocket = true,
        params = {@Param(key = "host",desc = "主机名"),@Param(key = "port",desc = "端口",defaultValue = "80"),@Param(key = "timeout",desc = "超时(秒)",defaultValue = "30")})
public class Xunfeng extends WebSocketPlugin {

    @Override
    public boolean check(Map param) {
        if (XunfengInner.init_state)
            return true;
        Python python = xunfengInner.getPython();
        println("正在初始化Python引擎");
        PythonInterpreter interpreter = python.interpreter();
        File pluginDir = new File(XunfengInner.PLUGIN_PATH);
        println("正在初始化插件");
        for (File plugin : pluginDir.listFiles()) {
            try {
                sendColorMsg(Message.GREEN(plugin.getName()+":初始化中"));
                interpreter.execfile(plugin.getCanonicalPath());
            } catch (IOException e) {
                sendColorMsg(Message.RED(plugin.getName()+":初始化失败"));
            }
        }
        XunfengInner.init_state = true;
        println("插件初始化结束");
        return true;
    }

    @Autowired
    private XunfengInner xunfengInner;

    @Override
    public Object start() {
        String host = param.get("host").toString();
        int port = Integer.parseInt(param.get("port").toString());
        int timeout = Integer.parseInt(param.get("timeout").toString());

        File pluginDir = new File(XunfengInner.PLUGIN_PATH);
        println("================================");
        println("开始扫描");
        for (File plugin : pluginDir.listFiles()) {

            try {
                xunfengInner.setFilename(plugin.getCanonicalPath());
                xunfengInner.setIp(host);
                xunfengInner.setPort(port);
                xunfengInner.setTimeout(timeout);
                xunfengInner.executor();
                String result = xunfengInner.result();
                if(StringUtils.isNotEmpty(result)){
                    sendColorMsg(Message.RED(plugin.getName()+":"+result));
                }else{
                    sendColorMsg(Message.GREEN(plugin.getName()+":无漏洞"));
                }
            } catch (IOException e) {
                continue;
            }
        }
        println("扫描结束");

        return null;
    }
}
