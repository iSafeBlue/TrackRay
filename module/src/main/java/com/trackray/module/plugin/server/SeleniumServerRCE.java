package com.trackray.module.plugin.server;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.plugin.WebSocketPlugin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Map;


@Rule(websocket = true, params = {@Param(key = "target", desc = "目标地址")})
//@Plugin(title = "Selenium Server 远程命令执行" , author = "浅蓝" )
public class SeleniumServerRCE extends WebSocketPlugin{


    @Override
    public boolean check(Map param) {
        return true;
    }
    @Override
    public Object start() {return "";}
}
