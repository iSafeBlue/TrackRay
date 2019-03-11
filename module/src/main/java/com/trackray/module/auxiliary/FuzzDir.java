package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.Language;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Rule(enable = false)
@Plugin(value="fuzzDir",title = "敏感文件扫描" , author = "blue" )
public class FuzzDir extends CommonPlugin {
    private Task task;
    private String target;
    private ExecutorService executor;
    @Override
    public boolean check(Map param) {
        if (param.containsKey("task") )
            task = (Task) param.get("task");
        target = param.get("target").toString();

        if (task!=null)
            executor = task.getExecutor();
        else
            executor = Executors.newFixedThreadPool(10);
        return true;
    }

    @Override
    public Object start() {


        for (String dir : Payload.dirPayload) {
            if (StringUtils.isNotBlank(dir)){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String path = dir;
                        Language lang = task!=null?task.getResult().getItems().get(target).getSystemInfo().getLanguage():null;
                        if (path.contains("%EXT%")) {
                            path = path.replaceAll("%EXT%", lang != null && lang != Language.OTHER ? lang.getSuffix() : "php");
                        }
                        String url = target.concat(path);
                        CrawlerPage clone = crawlerPage.clone();
                        clone.getRequest().setUrl(url);
                        fetcher.run(clone);
                        String contentString = crawlerPage.getResponse().getStatus().getContentString();
                        int code = clone.getResponse().getStatus().getStatusCode();
                        if (code != 404) {
                            if (task != null) {
                                if (StringUtils.containsAny(contentString.toLowerCase(),
                                        "not found","error" ,
                                                "exception" ,"不存在","无法找到",
                                                "safedog","找不到","防火墙"))
                                    return;
                                task.getResult().getItems().get(target).getSystemInfo().getDirs().put(url, code);
                            }
                            SysLog.info(url + " " + code);
                        }
                    }
                };
                executor.execute(runnable);
            }
        }
        return new Object();
    }
}
