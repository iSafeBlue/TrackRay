package com.trackray.module.plugin;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.attack.FuzzDomain;
import com.trackray.scanner.attack.Nmap;
import com.trackray.scanner.attack.Payload;
import com.trackray.scanner.bean.ResultItem;
import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.plugin.WebSocketPlugin;
import com.trackray.scanner.utils.DomainUtils;
import com.trackray.scanner.utils.ReUtils;
import com.trackray.scanner.utils.RegexUtil;
import com.trackray.scanner.utils.SysLog;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(title = "资产扫描" , author = "blue")
@Rule(param = {"domain","port","thread"} , websocket = true)
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
            send("thread不可以大于5");
            return false;
        }
        return true;
    }

    @Override
    public Object start() {
        String root = ReUtils.getDomain(domain);
        send("扫描初始化中，你的目标是："+root);
        for (String prefix : Payload.domainPayload) {
            if (StringUtils.isNotBlank(prefix)) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        Asset asset = new Asset();
                        String domain = String.format("%s.%s", prefix, root);
                        String ip =null;
                        try {
                            ip = InetAddress.getByName(domain).getHostAddress();
                        } catch (UnknownHostException e) {
                            return;
                        }
                        asset.setDomain(domain);
                        if (ReUtils.isIp(ip) && !ip.matches("(127\\.|192\\.|10\\.|172\\.)")){
                            CrawlerPage page = new CrawlerPage();
                            page.getRequest().setUrl("https://tools.ipip.net/ping.php?v=4&a=send&host="+domain+"&area[]=china");
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
                                String res = Nmap.exec("-p1-30000", "--open", formatIP);
                                asset.setSegment(res);
                            }
                        }
                        send(asset.toString());
                        assets.add(asset);
                    }
                });
            }
        }

        while (true){
            if (service.isTerminated() || service.isShutdown()){
                send("扫描结束");
                send("=======无CDN======");
                for (Asset asset : assets) {
                    if (!asset.isCdn()){
                        send(asset.toString());
                    }
                }
                send("=======有CDN======");
                for (Asset asset : assets) {
                    if (asset.isCdn()){
                        send(asset.toString());
                    }
                }

                break;
            }
        }
        return "";
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
