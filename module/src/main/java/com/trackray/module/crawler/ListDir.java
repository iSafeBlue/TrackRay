package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import org.javaweb.core.utils.StringUtils;

import java.net.MalformedURLException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/22 14:01
 */
@Plugin(title = "列目录漏洞",author = "浅蓝")
public class ListDir extends CrawlerPlugin {
    @Override
    public boolean check() {
        return target.toString().contains(".");
    }

    @Override
    public void process() {

        String url = target.toString();

        url = url.substring(0, url.lastIndexOf("/"));

        try {
            String body = requests.url(url).get().body();

            if (StringUtils.contains(body,"Index of /") && body.contains("a href")){
                Vulnerable vulnerable = Vulnerable.builder()
                        .title("文件遍历漏洞")
                        .address(url)
                        .payload(url)
                        .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                        .level(Vulnerable.Level.MIDDLE.getLevel())
                        .build();

                addVulnerable(vulnerable);

            }

        } catch (MalformedURLException e) {

        }

    }

}
