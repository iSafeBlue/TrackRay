package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
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
import org.javaweb.core.net.HttpURLRequest;
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
@Plugin(title = "FuckIPinfo",value = "fuckIPinfo")
public class FuckIPinfo extends InnerPlugin {
    @Override
    public void process() {
        String target = task.getTargetStr();

        HostInfo host = task.getResult().getHostInfo();

        String ip = ExtractUtils.extract(target, ExtractUtils.IP);

        String rootDomain = ExtractUtils.extract(target, ExtractUtils.DOMAIN);

        String targetHost = DomainUtils.getHost(target);

        host.setHost(targetHost);

        try {
            Document titleParse = Jsoup.parse(new URL(target), 1500);
            String title = titleParse.select("title").text();
            if (StringUtils.isNotEmpty(title))
                task.getResult().getAdditional().put("网站标题",title);
        } catch (Exception e) {
        }

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
            fuckRealIP(task);

        }

    }

    private void fuckRealIP(Task task) {
        try {
            String url = "https://fofa.so/result?qbase64=";
            String keyword = "domain=\"%s\" ";
            HttpURLRequest req = requests;
            String text = Base64.getEncoder().encodeToString((String.format(keyword, task.getResult().getHostInfo().getDomain())).getBytes());
            HttpResponse response = req.url(url.concat(text)).get();
            String body = response.body();
            if (StringUtils.containsAny(body,"获得 0 条匹配结果","Retry")){
                return;
            }
            Document parse = response.parse();
            Elements a = parse.select(".list_mod .fa-map-marker +a");
            if (!a.isEmpty()){
                String realIP = a.text().trim();
                String ip = task.getResult().getHostInfo().getIp();
                if (!ip.equals(realIP)){
                    task.getResult().getHostInfo().setRealIP(realIP);
                    task.getResult().getAdditional().put("FOFA提取到疑似真实IP" , realIP);
                }
            }


        }catch (Exception e){}
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
        page.getRequest().setUrl("https://tools.ipip.net/ping.php?v=4&a=println&host="+domain+"&area[]=china");
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
