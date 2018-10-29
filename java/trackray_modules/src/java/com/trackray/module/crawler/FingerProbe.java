package com.trackray.module.crawler;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.enums.Language;
import com.trackray.scanner.enums.WEBServer;
import com.trackray.scanner.plugin.CrawlerPlugin;
import com.trackray.scanner.utils.PageUtils;
import org.apache.http.Header;

import java.net.URL;

@Plugin(title = "系统指纹信息探测",author = "blue")
public class FingerProbe extends CrawlerPlugin {
    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void process() {
        Task task = crawlerPage.getTask();
        String content = crawlerPage.getResponse().getStatus().getContent();
        Language language = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getLanguage();
        WEBServer webServer = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getWebServer();
        if(language==null || language==Language.OTHER  || webServer==null || webServer == WEBServer.OTHER){
            Header[] responseHeader = crawlerPage.getResponse().getStatus().getHeaders();
            PageUtils.fingerServer(responseHeader,crawlerPage.getBase(),task);
        }
        language = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getLanguage();
        if(language==null || language==Language.OTHER ) {
            String url = crawlerPage.getRequest().getUrl();
            URL u = PageUtils.getURL(url);
            url = u.getPath();
            PageUtils.fingerLang(url,crawlerPage.getBase(),task);
        }
    }
}
