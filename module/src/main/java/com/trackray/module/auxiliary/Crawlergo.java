package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.stream.WebsocketOutputStream;
import com.trackray.module.inner.CrawlergoInner;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/31 17:38
 */
@Plugin(value = "crawlergoScan" ,title = "Crawlergo爬虫" , author = "浅蓝")
@Rule(websocket = true,
        params = {
                @Param(key = "url",defaultValue = "",desc = "扫描目标"),
                @Param(key = "maxtab" ,defaultValue = "20" , desc = "浏览器最大标签数"),
                @Param(key = "mode",defaultValue="smart",desc = "url过滤模式 [simple,smart,strict]"),
                @Param(key = "proxy",defaultValue = "" , desc = "扫描结束后推送请求的代理地址，可设置为你的被动扫描器，如burpsuite或xray，不填默认不推送，e.g:http://127.0.0.1:8080/")
        }
)
public class Crawlergo extends WebSocketPlugin {

    @Autowired
    private CrawlergoInner crawlergoInner;

    /*public class WebsocketsOutputStream extends PrintStream{

        private WebSocketSession webSocketSession;


        public WebsocketsOutputStream(OutputStream out , WebSocketSession webSocketSession) {
            super(out);
            this.webSocketSession = webSocketSession;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            Charset cs = Charset.forName("UTF-8");
            ByteBuffer bb = ByteBuffer.allocate(buf.length);
            bb.put(buf);
            bb.flip();
            CharBuffer cb = cs.decode(bb);
            char[] array = cb.array();
            StringBuffer buffer = new StringBuffer();
            buffer.append(array,off,len);
            //new BinaryMessage()
            TextMessage textMessage = new TextMessage(buffer.toString());
            try {
                webSocketSession.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            System.out.println(1);
            super.write(b);
        }
    }*/

   /* public static void main(String[] args) {

        CommandLine commandLine = CommandLine.parse("E:\\source\\trackray-framework\\release\\resources\\include\\crawlergo\\crawlergo_windows_amd64\\crawlergo.exe");

        DefaultExecutor defaultExecutor = new DefaultExecutor();
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        WebsocketsOutputStream websocketsOutputStream = new WebsocketsOutputStream(stream);

        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(websocketsOutputStream);

        defaultExecutor.setStreamHandler(pumpStreamHandler);
        *//*commandLine.addArgument("-c");
        commandLine.addArgument("\"" + "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" + "\"");
        commandLine.addArgument("-t");
        commandLine.addArgument(String.valueOf(20));
        commandLine.addArgument("http://testphp.vulnweb.com/");*//*
        try {
            defaultExecutor.execute(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
    @Override
    public boolean check(Map param) {
        if (crawlergoInner.check(param)){
            return true;
        }
        this.setErrorMsg("crawlergo检测未通过");
        return false;
    }


    @Override
    public Object start() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WebsocketOutputStream outputStream = new WebsocketOutputStream(byteArrayOutputStream,getSession());
        String url = param.get("url").toString();
        int maxtab = Integer.parseInt(param.get("maxtab").toString());
        String mode = param.get("mode").toString();
        String proxy = param.get("proxy").toString();
        crawlergoInner.executor();
        crawlergoInner.addUrl(url);
        CrawlergoInner.GlobalOption option = crawlergoInner.option();
        option.maxCrawledCount(maxtab);
        option.filterMode(mode);
        if (StringUtils.isNotEmpty(proxy)){
            option.pushProxy(proxy);
        }else {
            option.pushProxy("");
        }
        CommandLine commandLine = crawlergoInner.buildCommandLine();
        crawlergoInner.setOutputStream(outputStream);
        crawlergoInner.setSync(false);
        DefaultExecutor run = crawlergoInner.run(commandLine);
        return "";
    }
}
