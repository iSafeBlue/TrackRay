package com.trackray.module.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.burpsuite.pojo.BurpSuite;
import com.trackray.base.burpsuite.pojo.configuration.BurpConfig;
import com.trackray.base.burpsuite.pojo.configuration.ProxyServer;
import com.trackray.base.burpsuite.pojo.configuration.SocksProxy;
import com.trackray.base.burpsuite.pojo.configuration.UpstreamProxy;
import com.trackray.base.burpsuite.pojo.history.BurpProxyHistory;
import com.trackray.base.burpsuite.pojo.history.Messages;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpRequest;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/9 20:54
 */
@Plugin(value = "burpsuite",title = "BurpSuite")
@Rule(auth = true)
public class BurpSuiteApp extends MVCPlugin {

    @Autowired
    private BurpSuite burpSuite;

    @Override
    public void index() {
        if (burpSuite.checkActive()){

            String version = burpSuite.version();
            int burpPort = burpSuite.getBurpPort();
            int serverPort = burpSuite.getServerPort();
            model.addObject("version",version);
            model.addObject("burpPort",burpPort);
            model.addObject("burpApi","http://"+burpSuite.getServerAddress()+":"+serverPort);
            model.setViewName("index");
        }else{
            model.setViewName("open");
        }
    }

    @Function
    public void option(){
        BurpConfig configuration = burpSuite.config().getConfiguration();
        String json = StrUtils.formatJson(JSON.toJSONString(configuration));
        model.addObject("config",json);

        SocksProxy socksProxy = configuration.getProjectOptions().getConnections().getSocksProxy();
        model.addObject("socksProxy",socksProxy);

        UpstreamProxy upstreamProxy = configuration.getProjectOptions().getConnections().getUpstreamProxy();
        model.addObject("upstreamProxy",upstreamProxy.getServers());

        model.setViewName("option");
    }

    @Function
    public void doSocks(){
        SocksProxy socksProxy = JSON.parseObject(JSON.toJSONString(param), SocksProxy.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(socksProxy);

            json = "{\n" +
                    "  \"project_options\": {\n" +
                    "    \"connections\": {" +
                    "\"socks_proxy\":"+json+"\n" +
                    "}    }\n" +
                    "  }\n" +
                    "}";

            int serverPort = burpSuite.getServerPort();
            String url = "http://" + burpSuite.getServerAddress() + ":" + serverPort+"/burp/configuration";

            HttpResponse post = requests.url(url).data(json).method(HttpRequest.Method.PUT).request();
            int statusCode = post.getStatusCode();
            if (statusCode==200){
                write("{\"msg\":\"修改成功\"}");
                return;
            }

        } catch (Exception e) {
            write("{\"msg\":\""+e.getMessage()+"\"}");
            return;
        }
        write("{\"msg\":\"修改失败\"}");

    }

    @Function
    public void doProxy(){
        ProxyServer proxyServer = JSON.parseObject(JSON.toJSONString(param), ProxyServer.class);
        JSONObject servers = JSON.parseObject("{\n" +
                "  \"project_options\": {\n" +
                "    \"connections\": {\n" +
                "      \"upstream_proxy\": {\n" +
                "        \"servers\": [\n" +
                "        ],\n" +
                "            \"use_user_options\":false\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        JSONArray arr = servers.getJSONObject("project_options").getJSONObject("connections").getJSONObject("upstream_proxy").getJSONArray("servers");
        arr.add(proxyServer);
        String json = servers.toJSONString();
        int serverPort = burpSuite.getServerPort();
        String url = "http://" + burpSuite.getServerAddress() + ":" + serverPort+"/burp/configuration";
        try {
            HttpResponse post = requests.url(url).data(json).method(HttpRequest.Method.PUT).request();
            int statusCode = post.getStatusCode();
            if (statusCode==200){
                write("{\"msg\":\"修改成功\"}");
                return;
            }
        } catch (MalformedURLException e) {
            write("{\"msg\":\""+e.getMessage()+"\"}");
            return;
        }
        write("{\"msg\":\"修改失败\"}");

    }

    @Function
    public void history(){
        BurpProxyHistory proxyHistory = burpSuite.getProxyHistory();
        if (proxyHistory!=null){
            for (Messages messages : proxyHistory.getMessages()) {
                messages.setCode(messages.hashCode());
                String reqcode = "";
                try {
                    if (StringUtils.isNotEmpty(messages.getRequest())){
                        reqcode = new String(hackKit.enDecrypt.base64.decode(messages.getRequest()));
                    }
                } catch (Exception e) {
                    reqcode = new String(Base64.getDecoder().decode(messages.getRequest()));
                }

                String respcode = "";
                try {
                    if (StringUtils.isNotEmpty(messages.getResponse())){
                        respcode = new String(hackKit.enDecrypt.base64.decode(messages.getResponse()),"UTF-8");
                    }
                } catch (Exception e) {
                    respcode = new String(Base64.getDecoder().decode(messages.getResponse()));
                }
                messages.setRequest(HtmlUtils.htmlEscape(reqcode));
                messages.setResponse(HtmlUtils.htmlEscape(respcode));
            }
            model.addObject("history",proxyHistory.getMessages());
        }

        model.setViewName("history");
    }

    @Function
    public void repeater(){
        HashMap<String, String> headers = new HashMap<>();
        String request = param.get("request").toString();
        String host = param.get("host").toString();
        String protocol = param.get("protocol").toString();
        String port = param.get("port").toString();
        String header = request.substring(request.indexOf("\n")+1,request.indexOf("\n\n"));
        String frist = request.split("\n")[0];
        String method = frist.substring(0, frist.indexOf(" /")).trim();
        String url = frist.substring(frist.indexOf(" ") + 1, frist.indexOf("HTTP/")).trim();
        url = protocol + "://" + host + (StringUtils.equalsAny(port.trim(),"80","443")?"":(":"+port)) + url;
        if (StringUtils.isNotEmpty(header)){

            requests.getRequestHeader().clear();
            for (String s : header.split("\n")) {
                if (s.contains(":")){
                    String[] split = s.split(":");
                    String key = split[0].trim();
                    String value = s.substring(s.indexOf(":")+1).trim();
                    headers.put(key,value);
                }
            }
            requests.header(headers);
        }
        try {
            String data = request.substring(request.indexOf("\n\n") + 2);
            requests.data(data);
        }catch (Exception e){

        }
        try {
            requests.url(url);
            requests.method(HttpRequest.Method.valueOf(method));
            HttpResponse response = requests.request();
            String body = response.body();
            String resp = "";
            List<String> remove = response.getHeader().remove(null);
            if (remove!=null&&!remove.isEmpty()) {
                resp = remove.get(0)+"\n";
            }else{
                resp = "HTTP/1.1 "+response.getStatusCode()+" " + response.getStatusMessage() + "\n";
            }

            for (Map.Entry<String, List<String>> entry : response.getHeader().entrySet()) {
                if (entry.getValue().size()>1){
                    for (String s : entry.getValue()) {
                        resp += (entry.getKey() + ": " + s) + "\n";
                    }
                }else if (entry.getValue().size()==1){
                    resp += (entry.getKey() + ": " + entry.getValue().get(0)) + "\n";
                }

            }
            resp += "\n";
            resp += body;
            write(resp);
        } catch (MalformedURLException e) {
            write(e.toString());
        }

    }

    @Function
    public void open(){
        int serverPort = Integer.parseInt(param.get("serverPort").toString());
        boolean needLoader = Boolean.parseBoolean(param.getOrDefault("needLoader","false").toString());
        boolean headlessMode = Boolean.parseBoolean(param.getOrDefault("headlessMode","false").toString());
        JSONObject json = new JSONObject();

        BurpSuite burpSuite = this.burpSuite.option()
                .burpPort(serverPort)
                .headlessMode(headlessMode)
                .workDir(this.pluginResourcePath)
                .needLoader(needLoader).back();
        try {
            burpSuite.open();
            json.put("msg","正在启动Burpsuite代理服务器，请等待数秒后重新访问插件主页");
        }catch (Exception e){
            json.put("msg","启动失败："+e.getMessage());
        }
        write(json.toString());
    }

    @Function
    public void destory(){
        if (burpSuite.destory()){
            write("{\"msg\":\"关闭成功\"}");
        }else{
            write("{\"msg\":\"关闭失败\"}");
        }
    }
}
