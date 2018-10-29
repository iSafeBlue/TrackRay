package com.trackray.module.plugin;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.handle.Shell;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.plugin.CommonPlugin;
import com.trackray.scanner.utils.ReUtils;

import java.io.IOException;
import java.util.Map;

@Plugin(value = "ms17010Check",title = "MS17-010 SMB远程命令执行检测" , author = "blue")
@Rule(param = {"ip"} , type = AbstractPlugin.Type.TEXT)
public class MS17010 extends CommonPlugin<String> {
    public static final String PATH = BASE +"/MS17-010-Check/ms17010.py";

    @Override
    public boolean check(Map<String, String> param) {
        return ReUtils.isIp(param.get("ip"));
    }
    @Override
    public String start() {
        String ip = param.get("ip");
        Shell shell = shell();
        try {
            shell.block(true).target("python").exec(PATH,ip);
        } catch (IOException e) {
            return "execute error";
        }
        return shell.readAll();
    }
}
