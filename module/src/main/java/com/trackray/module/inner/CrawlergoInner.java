package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/29 17:20
 */
@Plugin(value = "crawlergo" ,title = "CrawlergoInner" , author = "浅蓝")
@Rule(auth = true)
public class CrawlergoInner extends InnerPlugin
{

    public File LINUX_FILE = new File(getPluginResourcePath(),"/crawlergo_linux_amd64/crawlergo");

    public File WIN_FILE = new File(getPluginResourcePath(),"/crawlergo_windows_amd64/crawlergo.exe");

    public File REAL_PATH = Constant.TRACKRAY_SYSTEMOS == Constant.WINDOWS?WIN_FILE:LINUX_FILE;

    public static final String VERSION = "0.2.0";

    @Value("${temp.dir}")
    private String tempdir;

    @Value("${crawlergo.console.log}")
    private boolean consoleOutput;

    private boolean sync = true;

    @Override
    public boolean check(Map param) {
        String content = "";
        if (Constant.TRACKRAY_SYSTEMOS == Constant.WINDOWS){
            Shell block = shell().block(true);
            if (WIN_FILE.exists()&&WIN_FILE.isFile()){
                block.workdir(WIN_FILE.getParentFile());
            }
            try {
                block.exec(WIN_FILE.getCanonicalPath() +" --version");
            } catch (IOException e) {
                throw new RuntimeException("执行CRAWLERGO版本命令时异常");
            }
            content = block.readAll();
        }else{

            Shell block = shell().block(true);
            if (LINUX_FILE.exists()&&LINUX_FILE.isFile()){
                block.workdir(LINUX_FILE.getParentFile());
            }
            try {
                block.exec(LINUX_FILE.getCanonicalPath() + " --version");
            } catch (IOException e) {
                Shell chomod = shell().block(true);
                try {
                    chomod.exec( "chmod +x " + LINUX_FILE.getCanonicalPath());
                } catch (IOException ex) {
                    throw new RuntimeException("赋予CRAWLERGO可执行权限时异常");
                }
                if (step==1) {
                    step++;
                    check(getParam());
                }
            }
            content = block.readAll();
        }
        if (StringUtils.containsAny(content,VERSION)){
            return true;
        }
        return false;
    }

    @Override
    public void process() {
    }

    @Value("${crawlergo.chrome.path}")
    private String chromiumPath = "";
    @Value("${crawlergo.max.tab}")
    private int maxCrawledCount = 10;
    @Value("${crawlergo.push.proxy}")
    private String pushProxy = "";
    @Value("${crawlergo.filter.mode}")
    private String filterMode = "";

    private List<String> urls = new ArrayList<>();
    private DefaultExecutor defaultExecutor;
    private OutputStream outputStream;
    private InputStream inputStream;


    public GlobalOption option(){
        return new GlobalOption();
    }

    public CrawlergoInner addUrl(String ... url){
        for (String s : url) {
            urls.add(s);
        }
        return this;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public DefaultExecutor getDefaultExecutor() {
        return defaultExecutor;
    }

    public void setDefaultExecutor(DefaultExecutor defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    public CommandLine buildCommandLine(){
        CommandLine commandLine = CommandLine.parse(REAL_PATH.toString());
        if (new File(chromiumPath).exists()) {
            commandLine.addArgument("-c");
            commandLine.addArgument("\"" + chromiumPath + "\"");
        }

        if (StringUtils.isNotEmpty(pushProxy)){
            commandLine.addArgument("--push-to-proxy");
            commandLine.addArgument(pushProxy);
        }

        if (StringUtils.isNotEmpty(filterMode)){
            commandLine.addArgument("--filter-mode");
            commandLine.addArgument(filterMode);
        }

        commandLine.addArgument("-t");
        commandLine.addArgument(String.valueOf(maxCrawledCount));

        for (String url : urls) {
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                continue;
            }
            url = url.replaceAll("\\\\","");
            url = url.replaceAll("$","");
            url = url.replaceAll("`","");
            url = url.replaceAll("\\|","");
            url = url.replaceAll("\"","");
            url = url.replaceAll("\r","");
            url = url.replaceAll("\n","");
            commandLine.addArgument("\""+url+"\"");
        }

        return commandLine;
    }

    public DefaultExecutor run(CommandLine commandLine){
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(REAL_PATH.getParentFile());
        final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
        if (!consoleOutput){
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(new ByteArrayOutputStream());
            executor.setStreamHandler(pumpStreamHandler);
        }else if (outputStream!=null){
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(pumpStreamHandler);
        }
        executor.setWatchdog(watchdog);
        defaultExecutor = executor;
        try {
            if (sync){
                executor.execute(commandLine,new DefaultExecuteResultHandler());
            }else {
                executor.execute(commandLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return executor;
    }

    public class GlobalOption {

        public GlobalOption chromiumPath(String chromiumPath){
            CrawlergoInner.this.chromiumPath = chromiumPath;
            return this;
        }

        public GlobalOption maxCrawledCount(int maxCrawledCount){
            CrawlergoInner.this.maxCrawledCount = maxCrawledCount;
            return this;
        }

        public GlobalOption pushProxy(String pushProxy){
            CrawlergoInner.this.pushProxy = pushProxy;
            return this;
        }
        public GlobalOption filterMode(String filterMode){
            CrawlergoInner.this.filterMode = filterMode;
            return this;
        }

        public CrawlergoInner back(){
            return CrawlergoInner.this;
        }
    }
}
