package com.trackray.base.plugin;

import com.trackray.base.attack.Crawler;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import org.javaweb.core.net.HttpURLRequest;

/**
 * 爬虫插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class CrawlerPlugin {

    public Crawler crawler;
    public CrawlerPage crawlerPage;
    public Fetcher fetcher = new Fetcher();
    protected HttpURLRequest request = new HttpURLRequest();
    public String target;
    public abstract boolean check();
    public abstract void process();

    public void executor(){
        if (check()){
            process();
        }
    }

    public void addVul(Vulnerable vulnerable){
        crawlerPage.getTask().getResult().getItems().get(crawlerPage.getBase()).getVulns().add(vulnerable);
    }

}
