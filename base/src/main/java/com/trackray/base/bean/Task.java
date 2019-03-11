package com.trackray.base.bean;

import com.trackray.base.utils.ReUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Task implements Cloneable{

    private String name = "";

    private int threadPool = 30;

    private int timeout = 2000;

    private int spiderDeep = 2;

    private int maxSpider = 1000;

    private String targetStr= "";

    private Target target;

    private String taskMD5= "";

    private String cookie= "";

    private Set<String> targets = new HashSet<>();

    private Map<String,Integer> proxyMap = new HashMap<>();

    private Rule rule = new Rule();

    private Result result = new Result();

    private ExecutorService executor;


    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public Set<String> getTargets() {
        return targets;
    }

    public void setTargets(Set<String> targets) {
        this.targets = targets;
    }

    public Map<String, Integer> getProxyMap() {
        return proxyMap;
    }

    public void setProxyMap(Map<String, Integer> proxyMap) {
        this.proxyMap = proxyMap;
    }

    public int getMaxSpider() {
        return maxSpider;
    }

    public void setMaxSpider(int maxSpider) {
        this.maxSpider = maxSpider;
    }



    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getSpiderDeep() {
        return spiderDeep;
    }

    public void setSpiderDeep(int spiderDeep) {
        this.spiderDeep = spiderDeep;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(int threadPool) {
        this.threadPool = threadPool;
    }

    public String getTargetStr() {
        return targetStr;
    }

    public void setTargetStr(String targetStr) {
        this.targetStr = targetStr;
    }

    public String getTaskMD5() {
        return taskMD5;
    }

    public void setTaskMD5(String taskMD5) {
        this.taskMD5 = taskMD5;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Target getTarget() {
        if (target==null){
            target = new Target();
            if(ReUtils.isIp(targetStr)){
                target.ip = targetStr;
                target.type = Constant.IP_TYPE;
            }else{
                target.url = targetStr;
                target.type = Constant.URL_TYPE;
            }
            return target;
        }
        return target;
    }

    public void setTarget(Target target){
        this.target = target;
    }

    public static class Target{
        public String url;
        public String ip;
        public int type;
    }

    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}


