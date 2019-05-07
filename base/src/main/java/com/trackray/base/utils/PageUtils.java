package com.trackray.base.utils;

import com.trackray.base.bean.Task;
import com.trackray.base.enums.Language;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.httpclient.CrawlerPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class PageUtils {

    public static String getContent(CrawlerPage crawlerPage)
    {
        final String contentString = crawlerPage.getResponse().getStatus().getContentString();
        if (StringUtils.isNotBlank(contentString)){
            return contentString;
        }
        return crawlerPage.getResponse().getStatus().getContent();
    }
    public static String getContent(CrawlerPage crawlerPage , String charset)
    {
        return crawlerPage.getResponse().getStatus().getContent(charset);
    }
    public static URL getURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Map<String,String> getParam(URL url){
        Map<String, String> map = new HashMap<>();
        String query = url.getQuery();
        if (StringUtils.isNotBlank(query))
        {
            if (query.contains("&")){
                String[] params = query.split("&");
                for (String param : params) {
                    String[] kv = param.split("=");
                    map.put(kv[0],kv[1]);
                }
            }else if(query.contains("=")){
                String[] kv = query.split("=");
                map.put(kv[0],kv[1]);
            }
        }
        return map;
    }


    public static void copyTaskProxy(Task task , CrawlerPage crawlerPage){
        if (!task.getProxyMap().isEmpty())
        {
            for (Map.Entry<String, Integer> entry : task.getProxyMap().entrySet()) {
                crawlerPage.getProxys().add(entry);
            }
        }
    }


    public static void fingerServer(Header[] responseHeader,String target, Task task) {
        if (responseHeader!=null && responseHeader.length > 0) {
            for (Header header : responseHeader) {
                if (header.getName().contains("Server")) {
                    String server = header.getValue().toLowerCase();
                    for (WEBServer webServer : WEBServer.values()) {
                        if (!webServer.getKeywords().isEmpty())
                        {
                            for (String k : webServer.getKeywords()) {
                                if (server.contains(k.toLowerCase())){
                                    task.getResult().getSystemInfo().setWebServer(webServer);
                                }
                            }

                        }
                    }
                    if (task.getResult().getSystemInfo().getWebServer() == null){
                        task.getResult().getSystemInfo().setWebServer(WEBServer.OTHER);
                    }
                }
                if (header.getName().contains("X-Powered-By")) {
                    String script = header.getValue().toLowerCase();
                    for (Language l : Language.values()) {
                        if (script.contains(l.name().toLowerCase()))
                            task.getResult().getSystemInfo().setLanguage(l);
                    }

                }
                if (header.getValue().contains("PHPSESSID") || header.getValue().contains(".php")) {
                    task.getResult().getSystemInfo().setLanguage(Language.PHP);
                }
                if (header.getValue().contains("JSESSIONID") || header.getValue().contains(".jsp")) {
                    task.getResult().getSystemInfo().setLanguage(Language.JAVA);
                }
                if (header.getValue().contains("ASP.NET") || header.getValue().contains(".asp")) {
                    task.getResult().getSystemInfo().setLanguage(Language.NET);
                }
            }

        }
    }

    public static void fingerLang(String str ,String target , Task task){
        if (str.contains(".php")){
            task.getResult().getSystemInfo().setLanguage(Language.PHP);
            SysLog.info("已识别到脚本语言为PHP");
        }else
        if (str.contains(".jsp") || str.contains(".action") || str.contains(".do")){
            task.getResult().getSystemInfo().setLanguage(Language.JAVA);
            SysLog.info("已识别到脚本语言为JAVA");
        }else
        if (str.contains(".asp")){
            task.getResult().getSystemInfo().setLanguage(Language.NET);
            SysLog.info("已识别到脚本语言为.NET");
        }else{
            task.getResult().getSystemInfo().setLanguage(Language.OTHER);
            SysLog.info("未识别出脚本语言");
        }

    }

}
