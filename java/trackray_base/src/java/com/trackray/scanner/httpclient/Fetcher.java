package com.trackray.scanner.httpclient;

import java.util.Map;
import java.util.Random;

public class Fetcher extends HttpClient{

    public void run(CrawlerPage page){

        if (!page.getProxys().isEmpty())
        {
            Map.Entry<String, Integer> e = page.getProxys().get(new Random().nextInt(page.getProxys().size()));
            page.setProxy(new HttpProxy(e.getKey(),e.getValue()));
        }

        if (page.getProxy()!=null)
            setProxy(page.getProxy());

        request(page);
    }
    public void sync(CrawlerPage page){run(page);}

    public void syncFetch(CrawlerPage page){
        new Thread(){
            @Override
            public void run() {
                sync(page);
            }
        }.start();
    }

}
