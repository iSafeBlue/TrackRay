package com.trackray.base.burpsuite.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trackray.base.burpsuite.pojo.configuration.BurpConfig;
import com.trackray.base.burpsuite.pojo.history.BurpProxyHistory;
import com.trackray.base.handle.Shell;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;
import org.apache.commons.exec.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/9 19:37
 */
@Component
public class BurpSuite {

    public static String burpFile = "burpsuite_pro_v2.0.11beta.jar";

    public static String burpLoaderFile = "burp-loader-keygen.jar";

    public static String burpApiFile = "burp-rest-api-2.0.1.jar";

    public static boolean isActive = false;
    private String serverAddress = "127.0.0.1";
    private int serverPort = 8090;
    private int burpPort = 8080;
    private boolean headlessMode = true;
    private boolean needLoader = true;
    private File workDir = null;

    public class BurpSuiteInner{
        public BurpSuite back(){
            return BurpSuite.this;
        }
    }
    public class BurpSuiteOption extends BurpSuiteInner{
        public BurpSuiteOption serverPort(int port){
            serverPort = port;
            return this;
        }
        public BurpSuiteOption workDir(File file){
            workDir = file;
            return this;
        }
        public BurpSuiteOption serverAddress(String address){
            serverAddress = address;
            return this;
        }
        public BurpSuiteOption burpPort (int port){
            burpPort = port;
            return this;
        }
        public BurpSuiteOption headlessMode(boolean flag){
            headlessMode = flag;
            return this;
        }
        public BurpSuiteOption needLoader(boolean flag){
            needLoader = flag;
            return this;
        }
    }

    public BurpSuiteOption option(){
        return new BurpSuiteOption();
    }

    public boolean checkActive(){
        try {
            RequestBuilder get = Requests.get("http://" + serverAddress + ":" + serverPort + "/burp/versions");
            RawResponse send = get.send();
            int code = send.statusCode();
            if (code==200){
                isActive = true;
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
    @Value("${temp.dir}")
    private String tempdir;

    @Value("${burp.console.log}")
    private boolean ouputConsole;

    private DefaultExecutor defaultExecutor;

    public BurpSuite open(){
        File burp = new File(tempdir, "burp");
        if (!burp.exists()) {
            burp.mkdirs();
        }else {
            for (File file : burp.listFiles()) {
                try {
                    if (file.toString().endsWith(".jar")||file.toString().endsWith(".json")){
                        file.delete();
                    }
                }catch (Exception e){
                    continue;
                }
            }
        }

        List<String> command = new ArrayList<String>();
        command.add("java");
        if (needLoader){
            command.add("-Xbootclasspath/p:"+burpLoaderFile);
        }
        command.add("-Djava.io.tmpdir="+burp);
        command.add("-jar");
        command.add(burpApiFile);
        command.add("--server.port="+serverPort);
        command.add("--burp.jar="+burpFile);
        if (!headlessMode){
            command.add("--headless.mode="+headlessMode);
        }
        String[] commandArr = command.toArray(new String[]{});
        String commandStr = String.join(" ", commandArr);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(getWorkDir());
        CommandLine parse = CommandLine.parse(commandStr);
        final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        if (!ouputConsole){
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(new ByteArrayOutputStream());
            executor.setStreamHandler(pumpStreamHandler);
        }
        executor.setWatchdog(watchdog);
        try {
            this.defaultExecutor = executor;
            executor.execute(parse,resultHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @PreDestroy
    public boolean destory(){
        try {
            RequestBuilder get = Requests.get("http://" + serverAddress + ":" + serverPort + "/burp/stop");
            RawResponse send = get.send();
            int code = send.statusCode();
            isActive = false;
            return true;
        }catch (Exception e){
            defaultExecutor.getWatchdog().destroyProcess();
            return false;
        }
    }
    public class BurpSuiteConfig extends BurpSuiteInner{

        public BurpConfig getConfiguration(){
            RequestBuilder get = Requests.get("http://" + serverAddress + ":" + serverPort + "/burp/configuration");
            RawResponse send = get.send();
            BurpConfig burpConfig = JSON.parseObject(send.readToText(), BurpConfig.class);
            return burpConfig;
        }
    }
    public BurpSuiteConfig config(){
        return new BurpSuiteConfig();
    }

    public BurpProxyHistory getProxyHistory(){
        RequestBuilder get = Requests.get("http://" + serverAddress + ":" + serverPort + "/burp/proxy/history");
        RawResponse send = get.send();
        BurpProxyHistory burpProxyHistory = JSON.parseObject(send.readToText(), BurpProxyHistory.class);
        return burpProxyHistory;
    }

    public String version(){
        RequestBuilder get = Requests.get("http://" + serverAddress + ":" + serverPort + "/burp/versions");
        RawResponse send = get.send();
        JSONObject jsonObject = JSON.parseObject(send.readToText());
        return jsonObject.getString("burpVersion");
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getBurpPort() {
        return burpPort;
    }

    public void setBurpPort(int burpPort) {
        this.burpPort = burpPort;
    }

    public boolean isHeadlessMode() {
        return headlessMode;
    }

    public void setHeadlessMode(boolean headlessMode) {
        this.headlessMode = headlessMode;
    }

    public boolean isNeedLoader() {
        return needLoader;
    }

    public void setNeedLoader(boolean needLoader) {
        this.needLoader = needLoader;
    }

    public File getWorkDir() {
        return workDir;
    }

    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }
}
