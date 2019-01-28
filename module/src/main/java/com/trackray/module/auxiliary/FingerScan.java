package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.FingerBean;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.CommonPlugin;

import java.util.HashMap;
import java.util.Map;

@Rule(enable = false)
@Plugin(value="fingerScan",title = "指纹扫描" , author = "blue" )
public class FingerScan extends CommonPlugin {
    private Task task;
    private String target;

    public static void main(String[] args) {
    }

    @Override
    public boolean check(Map param) {
        if (param.containsKey("task") )
            task = (Task) param.get("task");
        target = param.get("target").toString();
        return true;
    }

    @Override
    public FingerPrint start() {
        FingerPrint[] fingers = FingerPrint.values();
        for (FingerPrint finger : fingers) {
            if (finger==FingerPrint.unknown)
                continue;
            FingerBean[] beans = finger.getFingers();
            for (FingerBean bean : beans) {
                if (bean.isMatch()){
                    String url = target.concat(bean.getUrl());
                    crawlerPage.getRequest().setUrl(url);
                    fetcher.run(crawlerPage);

                    String content = crawlerPage.getResponse().getStatus().getContent();
                    if (    crawlerPage.getResponse().getStatus().getStatusCode()==200
                            &&
                            (content.contains(bean.getMatch())
                                            ||
                                    content.matches(bean.getMatch())
                            )){
                        return scaned(finger);
                    }
                }else{
                    //...md5
                }

            }
        }
        return FingerPrint.unknown;
    }

    private FingerPrint scaned(FingerPrint finger) {
        if (task!=null)
            task.getResult().getItems().get(target).getSystemInfo().setFinger(finger);
        return finger;
    }
}
