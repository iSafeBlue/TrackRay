package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.HostInfo;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.SysLog;
import org.javaweb.core.net.HttpRequest;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/24 16:43
 */
@Plugin(title = "域名基本信息扫描插件插件")
public class FuckWhois extends InnerPlugin{
    @Override
    public void process() {

        String host = DomainUtils.getHost(task.getTargetStr());

        String whois = searchWhois(host);

        chinazWhois(host);

        HostInfo hostInfo = this.task.getResult().getHostInfo();
        hostInfo.setWhois(whois);

    }

    private void chinazWhois(String host) {
        try {
            String body = requests.url("http://whois.chinaz.com/".concat(host)).get().body();

            Document doc = Jsoup.parse(body);

            Elements registerCOM = doc.select(".WhLeList-left:contains(注册商)+div span");
            Elements register = doc.select(".WhLeList-left:contains(联系人)+div span");
            Elements email = doc.select(".WhLeList-left:contains(联系邮箱)+div span");
            Elements dns = doc.select(".WhLeList-left:contains(DNS)+div");

            if (check(registerCOM)){
                String text = registerCOM.text();
                task.getResult().getAdditional().put("域名商",text);
            }

            if (check(register)){
                String text = register.text();
                task.getResult().getAdditional().put("域名注册者",text);
            }

            if (check(email)){
                String text = email.text();
                task.getResult().getAdditional().put("域名注册者邮箱",text);
                task.getResult().getSenseInfo().getEmail().add(text);
            }

            if (check(dns)){
                String[] dnss = dns.text().split(" ");
                if (dnss.length>0){
                    for (String s : dnss) {
                        task.getResult().getHostInfo().getDns().add(s);
                    }

                }

            }
            
        } catch (MalformedURLException e) {
            task.getExceptions().add(e);
        }

    }

    private boolean check(Elements elements){
        return elements!=null && !elements.isEmpty();
    }

    public String searchWhois(String domain){
        HashMap<String, String> map = new HashMap<>();
        map.put("sld",domain);
        try {
            HttpResponse res = requests.url("https://tool.lu/whois/ajax.html").data(map).method(HttpRequest.Method.POST).request();


            if (res!=null && res.getStatusCode() == 200 && !res.body().isEmpty()) {
                SysLog.info("已扫描到WHOIS");
                return res.body();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return "";
    }
}
