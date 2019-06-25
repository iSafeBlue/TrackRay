package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/6/24 18:18
 */
@Plugin(title = "后台登录页面泄漏" ,author = "浅蓝" )
@Rule()
public class LoginFormLeak extends CrawlerPlugin {
    @Override
    public boolean check() {
        Document parse = response.parse();

        if (parse!=null){
            Elements select = parse.select("input[type=password]");
            return !select.isEmpty();
        }

        return false;
    }

    @Override
    public void process() {

        addVulnerable(
                Vulnerable.builder()
                        .title("登录页面泄漏")
                        .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                        .level(Vulnerable.Level.LOW.getLevel())
                        .address(target.toString())
                        .build()
        );

    }
}
