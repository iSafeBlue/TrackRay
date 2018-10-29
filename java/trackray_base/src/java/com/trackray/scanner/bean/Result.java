package com.trackray.scanner.bean;

import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

    private IPInfo ipInfo = new IPInfo();//IP和域名的一些基本信息
    private Assets assets = new Assets();//目标的所有资产
    private SenseInfo senseInfo = new SenseInfo();//敏感信息

    private Map<String,ResultItem> items = new HashMap<>();

    public Map<String, ResultItem> getItems() {
        return items;
    }

    public void setItems(Map<String, ResultItem> items) {
        this.items = items;
    }

    public SenseInfo getSenseInfo() {
        return senseInfo;
    }

    public void setSenseInfo(SenseInfo senseInfo) {
        this.senseInfo = senseInfo;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public IPInfo getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(IPInfo ipInfo) {
        this.ipInfo = ipInfo;
    }

}
