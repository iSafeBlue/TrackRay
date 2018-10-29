package com.trackray.rest.query;

import com.trackray.scanner.bean.Rule;

public class TaskQuery {

    private String name = "";
    private String target = "";
    private int thread = 0;
    private int spiderMax = 0 ;
    private int spiderDeep = 0;
    private int timeMax = 0;
    private String cookie = "";
    private String proxy = "";
    private Rule rule = new Rule();

    public int getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(int timeMax) {
        this.timeMax = timeMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getSpiderMax() {
        return spiderMax;
    }

    public void setSpiderMax(int spiderMax) {
        this.spiderMax = spiderMax;
    }

    public int getSpiderDeep() {
        return spiderDeep;
    }

    public void setSpiderDeep(int spiderDeep) {
        this.spiderDeep = spiderDeep;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
