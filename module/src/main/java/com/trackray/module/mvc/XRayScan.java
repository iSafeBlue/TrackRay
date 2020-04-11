package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.XRay;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.XRayInner;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/7 10:47
 */
@Plugin(value = "xrayScan" , title = "XRay扫描器" , author = "浅蓝" )
@Rule(auth = true)
public class XRayScan extends MVCPlugin {

    @Autowired
    private XRay xRay;

    @Autowired
    private ConcurrentHashMap<String,Object> sysVariable;


    @Override
    public void index() {
        model.setViewName("index");
    }

    @Function
    public void doDel(){
        String file = param.get("file").toString();
        if (file.matches("[A-Za-z0-9\\._-]{1,100}")
                && !file.contains("/")
                && !file.contains("\\")){
            File report = new File(xRay.getOutputDir(), file);
            report.delete();
            write("删除成功");
        }else {
            write("文件名不合规");
        }

    }

    @Function
    public void doView(){
        String file = param.get("file").toString();
        if (file.matches("[A-Za-z0-9\\._-]{1,100}")
                && !file.contains("/")
                && !file.contains("\\")){
            File report = new File(xRay.getOutputDir(), file);
            if (report.exists()){
                try {
                    String content = FileUtils.readFileToString(report,"UTF-8");
                    model.addObject("msg", content);
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    model.addObject("msg", e.getMessage());
                }

            }else {
                model.addObject("msg", "文件不存在");
            }
        }else {
            model.addObject("msg", "文件名不合规");
        }
        model.setViewName("../common/default");
    }

    @Function
    public void passive(){
        File outputDir = xRay.getOutputDir();
        if (outputDir.exists()&&outputDir.isDirectory()){
            HashMap<String, String> map = new HashMap<>();
            for (File file : outputDir.listFiles()) {
                String name = file.getName();
                if (name.toLowerCase().endsWith(".html")){
                    Date date = new Date(file.lastModified());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = formatter.format(date);
                    map.put(name,dateString);
                }
            }
            String xray_report = sysVariable.getOrDefault("XRAY_REPORT", "").toString().concat(".html");
            model.addObject("current_report",xray_report);
            model.addObject("files",map);
        }
        model.setViewName("passive");
    }

    @Function
    public void doCrawler(){
        XRay xray = this.xRay.executor().result();
        String address = param.get("address").toString();
        if (address.startsWith("http://")||address.startsWith("https://")){
            xray.setBasicCrawler(address);
            String filename = "crawler_" + System.currentTimeMillis() + ".html";
            xray.outputHTML(filename);
            xray.run();
            write("正在扫描，请稍后查看报告文件:"+filename);
        }else {
            write("地址不合规");
        }
    }

    @Function
    public void crawler(){
        model.setViewName("crawler");
    }

    @Function
    public void scan(){

        XRay xray = this.xRay.executor().result();
        String url = param.get("url").toString();
        String plugins = param.get("plugins").toString();
        if (plugins.equals("all")){
            xray.setPlugins(XRayInner.PLUGINS);
        }else{
            xray.setPlugins(plugins.split(","));
        }
        xray.setUrl(url);
        xray.setBlock(true);
        DefaultExecutor run = xray.run();
        File file = xray.getOutfile();
        String content = "";
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
