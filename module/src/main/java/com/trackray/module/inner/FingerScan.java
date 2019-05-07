package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.FingerBean;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.plugin.InnerPlugin;
import org.javaweb.core.net.HttpResponse;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

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
        FingerPrint[] fingers = FingerPrint.values();
        for (FingerPrint finger : fingers) {
            if (finger==FingerPrint.unknown)
                continue;
            FingerBean[] beans = finger.getFingers();
            for (FingerBean bean : beans) {
                if (bean.isMatch()){
                    String url = target.concat(bean.getUrl());
                    try {
                        HttpResponse response = requests.url(url).get();
                        int statusCode = response.getStatusCode();
                        String content = response.body();
                        if (    statusCode==200
                                &&
                                (content.contains(bean.getMatch())
                                        ||
                                        content.matches(bean.getMatch())
                                )){
                            return scaned(finger);
                        }
                    } catch (MalformedURLException e) {
                        continue;
                    }

                }else{
                    //TODO:...md5
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
}
