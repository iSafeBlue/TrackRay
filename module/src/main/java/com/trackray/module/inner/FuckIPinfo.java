package com.trackray.module.inner;

import com.trackray.base.bean.HostInfo;
import com.trackray.base.bean.Task;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ExtractUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.RegexUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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

            fuckOhterIP(task);
            fuckIPLocation(task);
            fuckIPHistory(task);
            //TODO:真实IP

        }

    }

    public static void main(String[] args) {

        try {
            Document parse = Jsoup.parse(new URL("http://site.ip138.com/www.ixsec.org/"), 1500);
            Elements p = parse.select(".panel:contains(历史解析记录) > p");
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void fuckIPHistory(Task task) {
        try {
            HttpResponse resp = requests.url("http://site.ip138.com/" + task.getResult().getHostInfo().getDomain() + "/")
                    .get();
            if (resp!=null && resp.getStatusCode() == 200){

                Document parse = resp.parse();

                Elements p = parse.select(".panel:contains(历史解析记录) > p");
                if (p!=null && p.size()>0){
                    HashMap<String, String> historyIP = new HashMap<>();
                    for (Element element : p) {
                        String date = element.select("span").text();
                        String ip = element.select("a").text();
                        if (StringUtils.isNotEmpty(ip)){
                            historyIP.put(ip , date);
                        }
                    }
                    if (!historyIP.isEmpty())
                        task.getResult().getAdditional().put("域名历史解析记录",historyIP);
                }


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private void fuckIPLocation(Task task) {
        String domain = task.getResult().getHostInfo().getDomain();
        task.getExecutor().submit(
                new Runnable() {
                    @Override
                    public void run() {

                        try {
                            HttpResponse resp = requests.url("http://ip.tool.chinaz.com/" + domain).get();
                            Document parse = resp.parse();
                            if (parse!=null ){
                                Elements select = parse.select("p.WhwtdWrap:contains(物理位置) +p > span");
                                if (select.size() > 0)
                                {
                                    String ip = select.get(1).text();
                                    String location = select.last().text();
                                    if (location!=null && location.toLowerCase().contains("cdn"))
                                        task.getResult().getHostInfo().setCdn(true);
                                    task.getResult().getAdditional().put("IP物理位置",ip + " --- " + location);
                                }
                            }
                        } catch (MalformedURLException e) {
                        }
                    }
                }
        );
    }

    private void fuckOhterIP(Task task) {
        task.getExecutor().submit(
                new Runnable() {
                    @Override
                    public void run() {

                        Set<String> ips = fuckOhterIP(task.getResult().getHostInfo().getDomain());
                        if (!ips.isEmpty()){
                            task.getResult().getHostInfo().getOtherIP().addAll(ips);
                            task.getResult().getHostInfo().setCdn(ips.size()>1);
                        }
                    }
                }
        );


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
