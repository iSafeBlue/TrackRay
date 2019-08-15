package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.XRayInner;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/7 10:47
 */
@Plugin(value = "xrayScan" , title = "XRay扫描器" , author = "浅蓝" )
@Rule(auth = true)
public class XRayScan extends MVCPlugin {

    @Autowired
    private XRayInner xRayInner;

    @Override
    public void index() {
        model.setViewName("index");
    }

    @Function
    public void scan(){
        XRayInner xray = xRayInner.executor().result();
        String url = param.get("url").toString();
        String plugins = param.get("plugins").toString();
        if (plugins.equals("all")){
            xray.setPlugins(XRayInner.PLUGINS);
        }else{
            xray.setPlugins(plugins.split(","));
        }
        xray.setUrl(url);
        XRayInner.XRayResult run = xray.run(true);
        File file = run.outputFile();
        String content = run.outputText();
        if (file.exists() && file.length()>0){
            try {
                content = FileUtils.readFileToString(file,"UTF-8");
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        try {
            response.getWriter().print(content);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }



}
