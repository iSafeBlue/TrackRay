package com.trackray.scanner.attack;

import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;

public class SQLMap{

    public static String API;
    private final String NEW_TASK = API+"/task/new";

    private CrawlerPage crawlerPage = new CrawlerPage();
    private Fetcher fetcher = new Fetcher();
    private String taskid;
    private String target;
    private int engineid;
    private int status;
    public SQLMap(String url) {
        target = url;
        init();
        /*new Thread(){
            @Override
            public void run() {
                //
            }
        }.start();*/
    }

    private void init() {
        crawlerPage.getRequest().setUrl(NEW_TASK);
        crawlerPage.getRequest().setHttpMethod(HttpMethod.GET);
        fetcher.run(crawlerPage);
        String content = crawlerPage.getResponse().getStatus().getContent();
        System.out.println(content);
        JSONObject json = crawlerPage.getResponse().getRespFormatJson();
        if (json.optBoolean("success")){
            taskid = json.optString("taskid");
            status = 0;
        }
    }

    public boolean start() {
        boolean flag = true;
        try {
        JSONObject param = new JSONObject();
        param.put("url",target);
        crawlerPage.getRequest().setUrl(API+"/scan/"+taskid+"/start");
        crawlerPage.getRequest().addHttpHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        crawlerPage.getRequest().setParamStr(param.toString());
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        fetcher.run(crawlerPage);
        JSONObject json = crawlerPage.getResponse().getRespFormatJson();
        if (json.containsKey("engineid") && json.optBoolean("success"))
        {

            engineid = json.optInt("engineid");
            status = 2;
        }
        }catch (Exception e){
            flag = false;
        }

        return flag;
    }

    public void stop(){
        crawlerPage.getRequest().setUrl(API+"/scan/"+taskid+"/stop");
        crawlerPage.getRequest().setHttpMethod(HttpMethod.GET);
        fetcher.run(crawlerPage);
        status = 1;
    }

    public int status(){
        int i = 0;
        crawlerPage.getRequest().setUrl(API+"/scan/"+taskid+"/status");
        crawlerPage.getRequest().setHttpMethod(HttpMethod.GET);
        try {
            fetcher.run(crawlerPage);
            JSONObject json = crawlerPage.getResponse().getRespFormatJson();
            String status = json.getString("status");
            switch (status) {
                case "not running":
                    i=0;
                    System.out.println(status);
                    break;
                case "running":
                    i=2;
                    System.out.println(status);
                    break;
                case "terminated":
                    i=1;
                    System.out.println(status);
                    break;

            }
        }catch (Exception e){return i;}
        return i;
    }

    public JSONObject data(){
        crawlerPage.getRequest().setUrl(API+"/scan/"+taskid+"/data");
        crawlerPage.getRequest().setHttpMethod(HttpMethod.GET);
        fetcher.run(crawlerPage);
        return crawlerPage.getResponse().getRespFormatJson();
    }

    public String getTaskid() {
        return taskid;
    }

    public String getTarget() {
        return target;
    }

}
