package com.trackray.scanner.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum WEBServer {
    RESIN("Resin"),
    IIS8(".content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}"),
    IIS6("go.microsoft.com/fwlink/?linkid="),
    IIS7("Internet Information Services 7"),
    NGINX("nginx/"),
    TOMCAT("Apache Tomcat"),
    APACHE("The requested URL"+"|"+"Apache/"),
    OTHER;
    private String text;
    private List<String> keywords;
    WEBServer() {
        keywords = new ArrayList<>(0);
    }

    WEBServer(String text) {
        this.text = text;
        if (!text.isEmpty() && text.contains("|")){
            keywords = Arrays.asList(text.split("\\|"));
        }else{
            ArrayList<String> arr = new ArrayList<>();
            arr.add(text);
            keywords  = arr;
        }
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
