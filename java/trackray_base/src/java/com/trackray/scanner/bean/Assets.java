package com.trackray.scanner.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {
    //子域名  <Domain , IP>
    private Map<String,String> childDomain = new HashMap<>();
    //域名持有者的其他网站
    private List<String> holdSite = new ArrayList<String>();
    //同服务器的其他网站
    private List<String> serverSite = new ArrayList<String>();
    //备案者所持有的其他域名 <Domain , Ttile>
    private Map<String,String> icpSite = new HashMap<>();

    public Map<String, String> getChildDomain() {
        return childDomain;
    }

    public void setChildDomain(Map<String, String> childDomain) {
        this.childDomain = childDomain;
    }

    public List<String> getHoldSite() {
        return holdSite;
    }

    public void setHoldSite(List<String> holdSite) {
        this.holdSite = holdSite;
    }

    public List<String> getServerSite() {
        return serverSite;
    }

    public void setServerSite(List<String> serverSite) {
        this.serverSite = serverSite;
    }

    public Map<String, String> getIcpSite() {
        return icpSite;
    }

    public void setIcpSite(Map<String, String> icpSite) {
        this.icpSite = icpSite;
    }
}
