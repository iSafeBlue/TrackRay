package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.Language;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.SysLog;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Plugin(title = "系统指纹信息探测",author = "浅蓝")
public class FingerProbe extends CrawlerPlugin {
    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void process() {
        String content = response.body();
        Language language = task.getResult().getSystemInfo().getLanguage();
        WEBServer webServer = task.getResult().getSystemInfo().getWebServer();
        if(language==null || language==Language.OTHER  || webServer==null || webServer == WEBServer.OTHER){

            for (Map.Entry<String, List<String>> header : response.getHeader().entrySet()) {
                String value = header.getValue().get(0);
                String valueLower = value.toLowerCase();
                String key = header.getKey();
                String keyLower = key.toLowerCase();
                if (keyLower.contains("server")) {
                    String server = value;

                    for (WEBServer web : WEBServer.values()) {
                        if (!web.getKeywords().isEmpty())
                        {
                            for (String k : web.getKeywords()) {
                                if (valueLower.contains(k.toLowerCase())){
                                    task.getResult().getSystemInfo().setWebServer(web);
                                }
                            }

                        }
                    }
                    if (task.getResult().getSystemInfo().getWebServer() == null){
                        task.getResult().getSystemInfo().setWebServer(WEBServer.OTHER);
                    }
                }
                if (keyLower.contains("X-Powered-By".toLowerCase())) {
                    String script = value.toLowerCase();
                    for (Language l : Language.values()) {
                        if (script.contains(l.name().toLowerCase()))
                            task.getResult().getSystemInfo().setLanguage(l);
                    }

                }
                if (valueLower.contains("PHPSESSID".toLowerCase()) || value.contains("php")) {
                    task.getResult().getSystemInfo().setLanguage(Language.PHP);
                }
                if (valueLower.contains("JSESSIONID".toLowerCase()) || value.contains("jsp")) {
                    task.getResult().getSystemInfo().setLanguage(Language.JAVA);
                }
                if (valueLower.contains("ASP.NET".toLowerCase()) || value.contains("asp")) {
                    task.getResult().getSystemInfo().setLanguage(Language.NET);
                }
                
            }


        }

        //TODO:根据404页面判断WEB服务器

        language = task.getResult().getSystemInfo().getLanguage();
        if(language==null || language==Language.OTHER ) {
            String url = target.getPath().toLowerCase();

            if (url.contains(".php")){
                task.getResult().getSystemInfo().setLanguage(Language.PHP);
                SysLog.info("已识别到脚本语言为PHP");
            }else
            if (url.contains(".jsp") || url.contains(".action") || url.contains(".do")){
                task.getResult().getSystemInfo().setLanguage(Language.JAVA);
                SysLog.info("已识别到脚本语言为JAVA");
            }else
            if (url.contains(".asp")){
                task.getResult().getSystemInfo().setLanguage(Language.NET);
                SysLog.info("已识别到脚本语言为.NET");
            }else{
                task.getResult().getSystemInfo().setLanguage(Language.OTHER);
                SysLog.info("未识别出脚本语言");
            }

        }
    }
}
