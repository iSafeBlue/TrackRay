package com.trackray.base.httpclient;

import com.trackray.base.bean.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient 请求封装类
 */
@Deprecated
public class CrawlerPage implements Cloneable{

    private String base = "";

    private Task task;

    private Request request = new Request();

    private HttpProxy proxy;

    private Response response = new Response();

    private boolean redirect = true;


    private List<Map.Entry<String,Integer>> proxys = new ArrayList<>();

    private Map<String,Object> tempData = new HashMap<>();

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<Map.Entry<String, Integer>> getProxys() {
        return proxys;
    }

    public void setProxys(List<Map.Entry<String, Integer>> proxys) {
        this.proxys = proxys;
    }

    public Map<String, Object> getTempData() {
        return tempData;
    }

    public void setTempData(Map<String, Object> tempData) {
        this.tempData = tempData;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public HttpProxy getProxy() {
        return proxy;
    }

    public void setProxy(HttpProxy proxy) {
        this.proxy = proxy;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public CrawlerPage clone(){
        CrawlerPage p = null;
        try {
            p = (CrawlerPage) super.clone();
            p.setRequest(p.getRequest().clone());
            p.setResponse(p.getResponse().clone());
            if (p.getProxy()!=null)
                p.setProxy(p.getProxy().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return p;
    }
}


