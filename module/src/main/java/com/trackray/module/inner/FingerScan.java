package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.FingerBean;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.enums.WEBServer;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Rule(enable = false)
@Plugin(value="fingerScan",title = "指纹扫描" , author = "浅蓝" )
public class FingerScan extends InnerPlugin<FingerPrint> {
    private String target;

    @Override
    public void before() {
        this.target = this.task.getTargetStr();
    }

    @Override
    public void process() {
        result = start();
    }

    @Override
    public FingerPrint start() {

        //通过404判断webSERVER
        try {
            String page404 = requests.url(task.getTargetStr().concat("/asdasdsad")).get().body();

            for (WEBServer web : WEBServer.values()) {
                if (!web.getKeywords().isEmpty())
                {
                    if (StringUtils.containsAny(page404,web.getKeywords().toArray(new String[]{}))){
                        task.getResult().getSystemInfo().setWebServer(web);
                    }
                }
            }

        } catch (MalformedURLException e) {
        }


        FingerPrint[] fingers = FingerPrint.values();
        for (FingerPrint finger : fingers) {
            if (finger==FingerPrint.unknown)
                continue;
            FingerBean[] beans = finger.getFingers();
            for (FingerBean bean : beans) {
                try {
                    String url = target.concat(bean.getUrl());
                    log.info(String.format("指纹名[%s] url[%s]" , finger.getName() , bean.getUrl()));
                    HttpResponse response = requests.url(url).get();
                    int statusCode = response.getStatusCode();
                    String content = response.body();
                    if (bean.isMatch()){

                        if (    statusCode!=404
                                &&
                                (content.contains(bean.getMatch())
                                        ||
                                        content.matches(bean.getMatch())
                                )){
                            return scaned(finger);
                        }

                    }else{

                        String md5 = bean.getMd5();
                        if (matchMd5(response,md5)){
                            return scaned(finger);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return FingerPrint.unknown;
    }

    private FingerPrint scaned(FingerPrint finger) {
        if (task!=null)
            task.getResult().getSystemInfo().setFinger(finger);
        return finger;
    }

    @Value("${temp.dir}")
    private String temp;
    private boolean matchMd5(HttpResponse req, String match) {
        if (req.getStatusCode() == 404)
            return false;
        String uuid = UUID.randomUUID().toString();
        String body = req.body();
        File file = new File(temp.concat(uuid));
        try {
            FileUtils.writeStringToFile(file,body);
            String md5 = DigestUtils.md5Hex(new FileInputStream(file));
            return md5.equals(match);
        } catch (IOException e) {
            return false;
        }finally {
            if (file.exists())
                file.delete();
        }
    }
}