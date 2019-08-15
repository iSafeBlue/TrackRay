package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Payload;
import com.trackray.base.enums.Language;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;

@Rule(enable = false)
@Plugin(value="fuzzDir",title = "敏感文件扫描" , author = "浅蓝" )
public class FuzzDir extends InnerPlugin {

    @Override
    public void process() {
        start();
    }

    @Override
    public Object start() {
        //TODO:处理的比较简单 后期再完善
        for (String dir : Payload.dirPayload) {
            if (StringUtils.isNotBlank(dir)){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String path = dir;
                        Language lang = task!=null?task.getResult().getSystemInfo().getLanguage():null;
                        if (path.contains("%EXT%")) {
                            path = path.replaceAll("%EXT%", lang != null && lang != Language.OTHER ? lang.getSuffix() : "php");
                        }
                        String url = task.getTargetStr().concat(path);
                        try {
                            HttpResponse response = new HttpURLRequest().url(url).get();
                            int code = response.getStatusCode();
                            String body = response.body();
                            if (code != 404) {
                                if (task != null) {
                                    if (StringUtils.containsAny(body.toLowerCase(),
                                            "not found","error" ,
                                            "exception" ,"不存在","无法找到",
                                            "safedog","找不到","防火墙"))
                                        return;
                                    task.getResult().getSystemInfo().getDirs().put(url, code);
                                }
                            }

                        } catch (MalformedURLException e) {
                            SysLog.error(e.getMessage());
                        }
                        HashSet<String> page403 = new HashSet<>();
                        for (Map.Entry<String, Integer> entry : task.getResult().getSystemInfo().getDirs().entrySet()) {
                            if (entry.getValue()==403)
                                page403.add(entry.getKey());
                        }
                        int dirsize = task.getResult().getSystemInfo().getDirs().size();
                        if (dirsize > 20  && (dirsize/2) <= page403.size()){

                            //如果403页面大于目录数的一半，则清除这些 无用的403页面
                            for (String page : page403) {
                                task.getResult().getSystemInfo().getDirs().remove(page);
                            }

                        }
                    }
                };

                task.getExecutor().submit(runnable);
            }
        }
        return null;
    }

}
