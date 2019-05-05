package com.trackray.base.httpclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

/**
 * 请求执行类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Deprecated
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

    public void runThrow(CrawlerPage page) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {

        if (!page.getProxys().isEmpty())
        {
            Map.Entry<String, Integer> e = page.getProxys().get(new Random().nextInt(page.getProxys().size()));
            page.setProxy(new HttpProxy(e.getKey(),e.getValue()));
        }

        if (page.getProxy()!=null)
            setProxy(page.getProxy());


        requestThrow(page);
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
