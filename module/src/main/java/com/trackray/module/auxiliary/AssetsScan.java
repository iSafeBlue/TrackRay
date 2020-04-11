package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Payload;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.plugin.WebSocketPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ExtractUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.RegexUtil;
import com.trackray.module.inner.Nmap;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Plugin(title = "资产扫描" , author = "浅蓝")
@Rule(enable = false , params = {
                @Param(key = "domain",defaultValue = "baidu.com" , desc = "目标域名"),
                @Param(key = "port",defaultValue = "true" , desc = "是否扫描端口"),
                @Param(key = "thread",defaultValue = "3" , desc = "线程数"),}
                , websocket = true)
public class AssetsScan extends WebSocketPlugin{

    private String domain;
    private boolean isScanPort;
    private List<Asset> assets  = new ArrayList<>();
    private ExecutorService service;
    @Override
    public boolean check(Map param) {
        domain = param.get("domain").toString();
        String port = param.get("port").toString();
        isScanPort = Boolean.parseBoolean(port);
        int thread = Integer.parseInt(param.get("thread").toString());
        if (thread >0 && thread<6)
        {
            service = Executors.newFixedThreadPool(thread);
        }else{
            println("thread不可以大于5");
            return false;
        }
        return true;
    }



    @Override
    public Object start() {
        String root = ExtractUtils.extract(domain , ExtractUtils.DOMAIN);
        println("扫描初始化中，你的目标是："+root);
        for (String prefix : Payload.domainPayload) {
            if (StringUtils.isNotBlank(prefix)) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        Asset asset = new Asset();
                        String domain = String.format("%s.%s", prefix, root);
                        String ip =DomainUtils.getHostAddress(domain);
                        asset.setDomain(domain);
                        if (ReUtils.isIp(ip) && !ip.matches("(127\\.|192\\.|10\\.|172\\.)")){
                            CrawlerPage page = new CrawlerPage();
                            page.getRequest().setUrl("https://tools.ipip.net/ping.php?v=4&a=println&host="+domain+"&area[]=china");
                            page.getRequest().setHttpMethod(HttpMethod.POST);
                            page.getRequest().setTimeout(40000);
                            page.getRequest().addHttpHeader("Connection","keep-alive");
                            page.getRequest().addHttpHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                            page.getRequest().addHttpHeader("Referer","https://tools.ipip.net/ping.php");
                            fetcher.run(page);
                            Document html = page.getResponse().getRespFormatHtml();
                            Elements pings = html.getElementsByTag("script");

                            asset.getIplist().add(ip);
                            for (Element ping : pings) {
                                if (!ping.html().contains("call_ping"))
                                    continue;
                                String text = ping.html();
                                String json = RegexUtil.extractStr(text, "parent\\.call_ping\\((.*)\\);");
                                JSONObject obj = JSONObject.fromObject(json);
                                if (ReUtils.isIp(obj.getString("ip"))){
                                    String pingIP = (obj.getString("ip"));
                                    asset.getIplist().add(pingIP);
                                }
                            }

                            if (asset.getIplist().size()>1){
                                asset.setCdn(true);
                            }

                            if (!asset.isCdn() && isScanPort)
                            {
                                String[] iparr = ip.split("\\.");
                                String formatIP = String.format("%s.%s.%s.%s", iparr[0], iparr[1], iparr[2], "0/24");
                                Nmap nmap = new Nmap();
                                nmap.setParam(new HashMap<String,String>(){{put("target",formatIP);}});
                                asset.setSegment(nmap.executor().result().toString());
                            }

                            println(asset.toString());
                            assets.add(asset);
                        }

                    }
                });
            }
        }

        while (service.isTerminated() || service.isShutdown()){
            return assets;
        }

        return "end";
    }

    @Override
    public void onClose() {
        service.shutdownNow();
    }

    class Asset{
        private String domain;
        private boolean cdn;
        private HashSet<String> iplist = new HashSet<>();
        private String segment;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public boolean isCdn() {
            return cdn;
        }

        public void setCdn(boolean cdn) {
            this.cdn = cdn;
        }

        public HashSet<String> getIplist() {
            return iplist;
        }

        public void setIplist(HashSet<String> iplist) {
            this.iplist = iplist;
        }

        public String getSegment() {
            return segment;
        }

        public void setSegment(String segment) {
            this.segment = segment;
        }

        @Override
        public String toString() {
            return "Asset{" +
                    "domain='" + domain + '\'' +
                    ", cdn=" + cdn +
                    ", iplist=" + iplist +
                    ", segment='" + segment + '\'' +
                    '}';
        }
    }

}
