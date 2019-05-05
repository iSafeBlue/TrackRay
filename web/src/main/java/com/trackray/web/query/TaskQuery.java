package com.trackray.web.query;

import com.trackray.base.bean.Rule;
import lombok.Data;

@Data
public class TaskQuery {

    private String name = "";   //任务名


    private String target = ""; //任务目标 可以是 url 可以是域名 可以是主机名

    private int thread = 0;     //最大线程数

    private int spiderMax = 0 ; //最大爬虫页数

    private int spiderDeep = 0; //最大爬虫深度

    private int timeMax = 0;    //任务最长时间 单位：分钟

    private String cookie = ""; //cookie

    private String headers = "";//额外请求头

    private String proxy = "";  //代理池

    private Rule rule = new Rule(); //扫描开关

}
