package com.trackray.base.bean;

import com.trackray.base.utils.ReUtils;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ExecutorService;

@Data
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

    private List<Exception> exceptions = new ArrayList<>();

    public Target getTarget() {
        if (target==null){
            target = new Target();
            if(targetStr.matches("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}")){
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


