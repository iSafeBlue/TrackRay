package com.trackray.module.plugin.windows.smb;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.handle.Shell;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.ReUtils;

import java.io.IOException;
import java.util.Map;

@Plugin(value = "ms17010Check",title = "MS17-010 SMB远程命令执行检测" , author = "浅蓝")
@Rule(params = {@Param(key = "ip",desc = "目标地址")}, type = AbstractPlugin.Type.TEXT)
public class MS17010 extends CommonPlugin<String> {
    public static final String PATH = BASE +"/MS17-010-Check/ms17010.py";

    @Override
    public boolean check(Map<String, Object> param) {
        return ReUtils.isIp(param.get("ip").toString());
    }
    @Override
    public String start() {
        String ip = param.get("ip").toString();
        Shell shell = shell();
        try {
            shell.block(true).target("python").exec(PATH,ip);
        } catch (IOException e) {
            return "execute error";
        }
        return shell.readAll();
    }


}
