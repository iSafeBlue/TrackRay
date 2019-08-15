package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/7 10:49
 */
@Plugin(value = "xrayInner" , title = "XRay内部插件" , author = "浅蓝")
@Rule
public class XRayInner extends InnerPlugin<XRayInner> {

    public final static String VERSION = "0.9.1";

    public final static String LINUX_PATH = BASE + "/xray/linux/xray_linux_amd64";
    public final static String WIN_PATH = BASE + "/xray/windows/xray_windows_amd64.exe";

    public final static String REAL_PATH = Constant.TRACKRAY_SYSTEMOS == Constant.WINDOWS?WIN_PATH:LINUX_PATH;

    public static String[] PLUGINS = "crlf_injection,redirect,baseline,ssrf,jsonp,brute_force,upload,phantasm,xxe,path_traversal,dirscan,cmd_injection,sqldet".split(",");

    public final static File REAL_PATH_PARENT = new File(REAL_PATH);

    private List<String> command = new ArrayList<>();
    private File outfile ;

    private boolean setOutfileFlag = false;

    @Value("${temp.dir}")
    private String tempdir;

    @Override
    public boolean check(Map param) {
        String content = "";
        if (Constant.TRACKRAY_SYSTEMOS == Constant.WINDOWS){
            Shell block = shell().block(true);
            if (REAL_PATH_PARENT.exists()&&REAL_PATH_PARENT.isFile()){
                block.workdir(REAL_PATH_PARENT.getParentFile());
            }
            try {
                block.exec(REAL_PATH +" version");
            } catch (IOException e) {
                throw new RuntimeException("执行XRAY版本命令时异常");
            }
            content = block.readAll();
        }else{
            Shell chomod = shell().block(true);
            try {
                chomod.exec( "chmod +x " + LINUX_PATH);
            } catch (IOException e) {
                throw new RuntimeException("赋予XRAY可执行权限时异常");
            }

            Shell block = shell().block(true);
            if (REAL_PATH_PARENT.exists()&&REAL_PATH_PARENT.isFile()){
                block.workdir(REAL_PATH_PARENT.getParentFile());
            }
            try {
                block.exec(REAL_PATH + " version");
            } catch (IOException e) {
                throw new RuntimeException("执行XRAY版本命令时异常");
            }
            content = block.readAll();
        }
        if (StringUtils.containsAny(content,"Version",VERSION)){
            return true;
        }
        return false;
    }



    @Override
    public void process() {
        result = this;
        command.add(REAL_PATH);
        command.add("webscan");

        File file = new File(tempdir.concat("xray/"));
        if (!file.exists())
            file.mkdirs();


    }
    public XRayInner setCommand(String command){
        this.command.add(command);
        return this;
    }

    public XRayInner setListen(String ip ,int port) {
        String host = (ip+":"+port);
        command.add("--listen "+host);
        return this;
    }
    public XRayInner setPlugins(String... plugins){
        command.add("--plugins "+String.join(",",plugins));
        return this;
    }
    public XRayInner setUrl(String url){
        url = url.replaceAll("\\&","%26");
        url = url.replaceAll("\"","\\\"");
        url = url.replaceAll("\\|","%7C");

        command.add("--url "+ "\"" + url + "\"");
        return this;
    }

    public XRayInner outputJSON(String filename){
        output("json",filename);
        return this;
    }

    public XRayInner outputTEXT(String filename){
        output("text",filename);
        return this;
    }
    private void output(String type , String filename){
        setOutfileFlag = true;
        File file = new File(tempdir.concat("xray/"));
        outfile = new File(file, filename);
        command.add("--"+type+"-output "+outfile.getAbsolutePath());
    }


    public XRayResult run(boolean block){
        Shell shell = shell().block(block);
        if (REAL_PATH_PARENT.exists()&&REAL_PATH_PARENT.isFile()){
            shell.workdir(REAL_PATH_PARENT.getParentFile());
        }
        if (!setOutfileFlag)
            outputTEXT(UUID.randomUUID().toString());
        try {
            shell.exec(String.join(" ",command.toArray(new String[]{})));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        String content = shell.readAll();
        XRayResult xRayResult = new XRayResult() {
            @Override
            public File outputFile() {
                return outfile;
            }

            @Override
            public String outputText() {
                return content;
            }
        };
        return xRayResult;
    }


    public interface XRayResult{
        public File outputFile();
        public String outputText();
    }

}
