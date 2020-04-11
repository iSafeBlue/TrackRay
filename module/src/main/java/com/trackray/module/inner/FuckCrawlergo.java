package com.trackray.module.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.XRay;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.burpsuite.pojo.BurpSuite;
import com.trackray.base.burpsuite.pojo.configuration.ProxyServer;
import com.trackray.base.plugin.InnerPlugin;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.javaweb.core.net.HttpRequest;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/31 14:42
 */
@Plugin(value = "fuckCrawlergo", title = "crawlergo爬虫扫描",author = "浅蓝")
public class FuckCrawlergo extends InnerPlugin{

    @Autowired
    private CrawlergoInner crawlergoInner;

    @Autowired
    private ConcurrentHashMap<String,Object> sysVariable;

    @Autowired
    private XRay xRay;

    @Autowired
    private BurpSuite burpSuite;

    @Value("${xray.remote.host}")
    private String host;

    @Value("${xray.remote.port}")
    private String port;

    @Override
    public void before() {

        ProxyServer proxyServer = new ProxyServer();
        proxyServer.setDestination_host("*");
        proxyServer.setEnabled(true);
        proxyServer.setProxy_host(host);
        proxyServer.setProxy_port(Integer.parseInt(port));

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
                log.info("crawlergo自动为burpsuite设置上游xray被动扫描代理成功");
                return;
            }
        } catch (Exception e) {
            log.error("crawlergo自动为burpsuite设置上游xray被动扫描代理失败",e);
            return;
        }

    }

    @Override
    public void process() {
        crawlergoInner.executor();
        String targetStr = task.getTargetStr();
        crawlergoInner.setSync(false);
        crawlergoInner.addUrl(targetStr);
        CommandLine commandLine = crawlergoInner.buildCommandLine();
        crawlergoInner.run(commandLine);
        String xray_report = sysVariable.getOrDefault("XRAY_REPORT", "").toString();
        if (!xray_report.isEmpty()){
            File reportFile = new File(xRay.getOutputDir(), xray_report + ".json");
            if (reportFile.exists()){
                try {
                    String jsonStr = FileUtils.readFileToString(reportFile, "UTF-8");
                    JSONArray arr = JSON.parseArray(jsonStr);
                    if (arr!=null && !arr.isEmpty())
                    {
                        for (int i = 0; i <arr.size() ; i++) {
                            try {
                                JSONObject obj = arr.getJSONObject(i);
                                JSONObject detail = obj.getJSONObject("detail");
                                JSONObject target = obj.getJSONObject("target");
                                String url = target.getString("url");
                                String host = detail.getString("host");
                                String payload = detail.getString("payload");
                                String request = detail.getString("request");
                                if (request.length()>=65534)
                                    request = request.substring(0,65534);
                                String response = detail.getString("response");
                                if (response.length()>=65534)
                                    response = response.substring(0,65534);

                                if (targetStr.toLowerCase().contains(host.toLowerCase())){
                                    Vulnerable vulnerable = Vulnerable.builder().title("CRAWLERGO x XRAY漏扫结果")
                                            .payload(payload)
                                            .request(request)
                                            .response(response)
                                            .address(url).build();
                                    addVulnerable(vulnerable);

                                }
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                                continue;
                            }
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }
}
