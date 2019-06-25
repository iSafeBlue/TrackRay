package com.trackray.base.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public enum WEBServer {
    RESIN("Resin"),
    IIS8("class=\"content-container\"><fieldset>"),
    IIS6("go.microsoft.com/fwlink/?linkid="),
    IIS7("Internet Information Services 7","background-color:#555555;}","<h3>您要查找的资源可能已被删除","<div id=\"header\"><h1>服务器错误</h1></div>"),
    NGINX("nginx/"),
    TOMCAT("Apache Tomcat"),
    APACHE("The requested URL","Apache/"),
    WEBLOGIC("Hypertext Transfer Protocol","From RFC 2068","unavailable and has no forwarding address"),
    OTHER;
    private List<String> keywords = new ArrayList<>();
    WEBServer() {
    }

    WEBServer(String ... text) {
        for (String s : text) {
            keywords.add(s);
        }
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
