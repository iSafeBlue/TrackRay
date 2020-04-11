package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.stream.WebsocketOutputStream;
import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/2/8 20:10
 */
@Plugin(value = "nmapScan" ,title = "nmap扫描" , author = "浅蓝")
@Rule(websocket = true,
        params = {
        @Param(key = "args" , defaultValue = "", desc = "nmap参数命令，eg:-sS -p1-65535 --open -A "),
        @Param(key = "target" , defaultValue = "", desc = "eg:192.168.1.1 or 192.168.1.0/24")
})
public class NmapScan extends WebSocketPlugin {

    private OutputStream outputStream;
    private DefaultExecutor executor;
    @Override
    public boolean check(Map param) {
        this.outputStream = new WebsocketOutputStream(new ByteArrayOutputStream(),getSession());
        return true;
    }

    @Override
    public void onClose() {
        if (executor!=null&&executor.getWatchdog().isWatching()){
            executor.getWatchdog().destroyProcess();
        }
    }

    @Override
    public Object start() {
        CommandLine nmap = CommandLine.parse("nmap");
        String args = param.get("args").toString();
        String target = param.get("target").toString();
        nmap.addArgument(args);
        nmap.addArgument(target);

        println("请稍等...");

        DefaultExecutor executor = new DefaultExecutor();
        final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(pumpStreamHandler);
        executor.setWatchdog(watchdog);
        try {
            executor.execute(nmap,new DefaultExecuteResultHandler());
        } catch (IOException e) {
            println(e.getMessage());
        }


        return "";
    }
}
