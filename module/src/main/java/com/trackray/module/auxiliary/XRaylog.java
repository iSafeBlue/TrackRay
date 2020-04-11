package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.XRay;
import com.trackray.base.plugin.WebSocketPlugin;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/4/6 19:03
 */
@Plugin(value = "xrayLog" ,title = "XRay日志" , author = "浅蓝")
@Rule(websocket = true)
public class XRaylog extends WebSocketPlugin{

    @Override
    public boolean check(Map param) {
        return true;
    }

    @Override
    public Object start() {

        if (XRay.logBuffer!=null&&XRay.logBuffer.length()>0){
            println(XRay.logBuffer.toString());
        }else{
            println("暂无日志");
        }


        return null;
    }

    @Override
    public void after() {
        println("\n插件执行结束，如需再次查询请重新执行插件");
    }
}
