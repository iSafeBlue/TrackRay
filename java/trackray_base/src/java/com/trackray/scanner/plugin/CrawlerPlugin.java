package com.trackray.scanner.plugin;

import com.trackray.scanner.attack.Crawler;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;

import java.util.List;

public abstract class CrawlerPlugin {

    public Crawler crawler;
    public CrawlerPage crawlerPage;
    public Fetcher fetcher = new Fetcher();
    public abstract boolean check();
    public abstract void process();

    public void executor(){
        if (check()){
            process();
        }
    }

}
