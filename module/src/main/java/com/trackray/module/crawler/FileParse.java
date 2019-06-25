package com.trackray.module.crawler;

import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.Language;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.plugin.CrawlerPlugin;
import org.javaweb.core.net.HttpResponse;

import java.net.MalformedURLException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/6/24 18:11
 */
public class FileParse extends CrawlerPlugin {
    @Override
    public boolean check() {
        WEBServer webServer = this.task.getResult().getSystemInfo().getWebServer();
        Language language = this.task.getResult().getSystemInfo().getLanguage();
        String file = this.target.getFile();
        boolean endFlag = file.endsWith("jpg")||file.endsWith("png")||file.endsWith("js")||file.endsWith("css");
        return endFlag && (webServer==WEBServer.IIS7 || webServer==WEBServer.NGINX) && language == Language.PHP;
    }

    @Override
    public void process() {
        String url = this.target.toString().concat("/.php");
        try {
            HttpResponse httpResponse = requests.url(url).get();
            String contentType = httpResponse.getContentType();
            if (contentType.contains("text/html")){
                addVulnerable(
                        Vulnerable.builder()
                                .title("PHP 文件解析漏洞")
                                .address(url)
                                .payload(url)
                                .type(Vulnerable.Type.CODE_EXECUTION.getType())
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .build()
                );
            }
        } catch (MalformedURLException e) {
        }
    }
}
