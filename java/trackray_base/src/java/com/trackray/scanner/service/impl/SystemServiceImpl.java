package com.trackray.scanner.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.enums.WEBServer;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import com.trackray.scanner.service.SystemService;
import com.trackray.scanner.utils.PageUtils;
import com.trackray.scanner.utils.SysLog;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service("systemService")
public class SystemServiceImpl implements SystemService{

    @Override
    public ResultCode scan(Task task) {
        this.scanLanguage(task);
        this.scanServer(task);
        if (task.getRule().port)
           this.nmap(task);
        
        return ResultCode.SUCCESS;
    }

    private void nmap(Task task) {
    }

    private void scanServer(Task task) {
        String url = task.getTargetStr();
        CrawlerPage page = new CrawlerPage();
        Fetcher fetcher = new Fetcher();
        page.getRequest().setUrl(url.concat("/s9d9a12j39s9a"));
            fetcher.run(page);
            if (page.getResponse().getStatus().getStatusCode() != 200)
            {
                String content = page.getResponse().getStatus().getContent();
                for (WEBServer webServer : WEBServer.values()) {
                    if (!webServer.getKeywords().isEmpty() && content.contains(webServer.getText())){
                        task.getResult().getItems().get(task.getTargetStr()).getSystemInfo().setWebServer(webServer);
                        SysLog.info("已识别到服务器类型为"+webServer.name());
                        return;
                    }
                }

            }
    }

    private void scanLanguage(Task task) {

        String url = task.getTargetStr();

        /******** 通过响应头判断网站代码类型 *********/
        CrawlerPage page = new CrawlerPage();
        page.getRequest().setUrl(url);
        Fetcher fetcher = new Fetcher();
        fetcher.run(page);
        Header[] responseHeader = page.getResponse().getStatus().getHeaders();
        PageUtils.fingerServer(responseHeader,task.getTargetStr(),task);

        /*****通过robots****/
        page.getRequest().setUrl(url.concat("/robots.txt"));
        fetcher.run(page);
        if (page.getResponse().getStatus().getStatusCode() == 200){
            String content = page.getResponse().getStatus().getContent();
            PageUtils.fingerLang(content,task.getTargetStr(),task);
        }
    }
}
