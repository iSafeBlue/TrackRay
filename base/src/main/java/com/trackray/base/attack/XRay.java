package com.trackray.base.attack;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.stream.PluginDataOutputStream;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/7 10:49
 */
@Plugin(value = "xray" , title = "XRay内部插件" , author = "浅蓝")
@Rule
public class XRay extends InnerPlugin<XRay> {

    public final static String VERSION = "0.18.2";

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

    @Value("${xray.console.log}")
    private boolean ouputConsole;

    private File outputDir = null;

    private boolean block = false;


    public static StringBuffer logBuffer = new StringBuffer();


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

    public File getOutputDir() {
        return new File(tempdir.concat("xray/"));
    }

    @Override
    public void process() {
        result = this;
        command.add(REAL_PATH);
        command.add("webscan");

        outputDir = new File(tempdir.concat("xray/"));

        if (!outputDir.exists())
            outputDir.mkdirs();

    }

    public  boolean isLocalPortUsing(int port){
        boolean flag = false;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
        }
        return flag;
    }
    /***
     * 测试主机Host的port端口是否被使用
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host,int port)  {
        boolean flag = false;
        Socket socket = null;
        try {

            InetAddress Address = InetAddress.getByName(host);
            socket = new Socket(Address,port);  //建立一个Socket连接
            flag = true;
        } catch (IOException e) {

        }finally {

        }
        return flag;
    }

        public XRay setCommand(String command){
        this.command.add(command);
        return this;
    }

    public XRay setListen(String ip , int port) {
        String host = (ip+":"+port);
        command.add("--listen "+host);
        return this;
    }
    public XRay setBasicCrawler(String url) {
        command.add("--basic-crawler "+url);
        return this;
    }
    public XRay setPlugins(String... plugins){
        command.add("--plugins "+String.join(",",plugins));
        return this;
    }
    public XRay setUrl(String url){
        url = url.replaceAll("\\&","%26");
        url = url.replaceAll("\"","\\\"");
        url = url.replaceAll("\\|","%7C");

        command.add("--url "+ "\"" + url + "\"");
        return this;
    }

    public XRay outputJSON(String filename){
        output("json",filename);
        return this;
    }

    public XRay outputTEXT(String filename){
        output("text",filename);
        return this;
    }
    public XRay outputHTML(String filename){
        output("html",filename);
        return this;
    }

    private void output(String type , String filename){
        setOutfileFlag = true;
        File output = new File(tempdir.concat("xray/"));
        outfile = new File(output, filename);
        command.add("--"+type+"-output "+outfile.getAbsolutePath());
    }

    public File getOutfile() {
        return outfile;
    }

    public void setOutfile(File outfile) {
        this.outfile = outfile;
    }

    public DefaultExecutor run(){
        DefaultExecutor executor = new DefaultExecutor();

        if (REAL_PATH_PARENT.exists()&&REAL_PATH_PARENT.isFile()){
            executor.setWorkingDirectory(REAL_PATH_PARENT.getParentFile());
        }
        if (!setOutfileFlag)
            outputTEXT(UUID.randomUUID().toString());
        try {
            String joinCmd = String.join(" ", command.toArray(new String[]{}));
            CommandLine commandLine = CommandLine.parse(joinCmd);
            final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

            if (!ouputConsole){
                PluginDataOutputStream pluginDataOutputStream = new PluginDataOutputStream(new ByteArrayOutputStream(), logBuffer);

                PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(pluginDataOutputStream);

                //PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(new ByteArrayOutputStream());
                executor.setStreamHandler(pumpStreamHandler);
            }



            executor.setWatchdog(watchdog);

            if (block){
                executor.execute(commandLine);
            }else {
                executor.execute(commandLine,resultHandler);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return executor;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
