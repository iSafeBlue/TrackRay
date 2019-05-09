package com.trackray.module.inner;

import com.trackray.base.bean.HostInfo;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ExtractUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.RegexUtil;
import net.sf.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/24 16:37
 */
public class FuckIPinfo extends InnerPlugin {
    @Override
    public void process() {
        String target = task.getTargetStr();

        HostInfo host = task.getResult().getHostInfo();

        String ip = ExtractUtils.extract(target, ExtractUtils.IP);

        String rootDomain = ExtractUtils.extract(target, ExtractUtils.DOMAIN);

        String targetHost = DomainUtils.getHost(target);

        host.setHost(targetHost);

        if (ip != null) {
            host.setIp(ip);
            host.setRealIP(ip);

        }else if (rootDomain != null){
            String domain = targetHost;
            ip = DomainUtils.getHostAddress(domain);
            host.setRootDomain(rootDomain); //根域名
            host.setDomain(domain); //当前域名
            host.setIp(ip); //域名的IP
            host.setRealIP(ip);//真实IP

            //TODO:真实IP

            task.getExecutor().submit(
                    new Runnable() {
                        @Override
                        public void run() {
                            Set<String> ips = fuckOhterIP(domain);
                            if (!ips.isEmpty()){
                                host.getOtherIP().addAll(ips);
                                host.setCdn(true);
                            }
                        }
                    }
            );
        }

    }

    private Set<String> fuckOhterIP(String domain) {
        Set<String> r = new HashSet<>();
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

        for (Element ping : pings) {
            if (!ping.html().contains("call_ping"))
                continue;
            String text = ping.html();
            String json = RegexUtil.extractStr(text, "parent\\.call_ping\\((.*)\\);");
            JSONObject obj = JSONObject.fromObject(json);
            String pingIP = (obj.getString("ip"));
            if (ExtractUtils.matche(pingIP,"(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}"))
                r.add(pingIP);
        }
        return r;
    }


}
