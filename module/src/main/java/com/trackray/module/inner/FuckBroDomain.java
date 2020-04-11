package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.RegexUtil;
import com.trackray.base.utils.SysLog;
import org.apache.http.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/24 17:01
 */
@Plugin(title = "兄弟域名扫描插件" , author = "浅蓝")
public class FuckBroDomain extends InnerPlugin {
    @Override
    public void process() {
        String host = DomainUtils.getHost(task.getTargetStr());

        List<String> domains = chinazReverse(host);//反查域名资产
        task.getResult().getAssets().setHoldSite(domains);//存储域名资产

        Map<String, String> icpMap = aizhanIcp(host);//备案者的其他域名资产

        task.getResult().getAssets().setIcpSite(icpMap);//存储备案域名资产

        List<String> sameSite = chinazSame(task.getResult().getHostInfo().getRealIP());//同服网站

        task.getResult().getAssets().setServerSite(sameSite);//存储同服网站资产
    }

    public List<String> chinazSame(String ip){
        List<String> result = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<String, String>();
        int total = 5;
        for (int page=1;page<=total;page++) {
            String url = "http://s.tool.chinaz.com/same".concat("?s="+ip+"&page="+page);
            Document doc = null;
            try {
                URL u = new URL(url);
                doc = Jsoup.parse(u, 10000);
            } catch (Exception e) {
                continue;
            }
            if (doc !=null && doc.toString().contains("ReLists")) {
                String pagestr = RegexUtil.extractStr(doc.toString(), "共(\\d)+页");
                int i = 1;
                try {
                    Integer.parseInt(pagestr);
                }catch (Exception e){
                    i=1;
                }
                if (page == 1) {
                    if (i <= 5 && i >= 1){
                        total = i;
                    }
                }
                Elements reLists = doc.select("li.ReLists");
                for (Element list : reLists) {
                    String domain = list.select("div.overhid a").text();
                    map.put(domain,"");
                }
            }else {
                continue;
            }

        }

        for (String key : map.keySet()) {
            result.add(key);
        }
        SysLog.info("同服网站扫描结束");
        return result;
    }


    public Map<String,String> aizhanIcp(String domain){
        HashMap<String, String> map = new HashMap<>();
        HttpClient httpClient = new HttpClient();
        String url = "https://icp.aizhan.com/%s/";
        try {
            ResponseStatus responseStatus = httpClient.get(String.format(url, domain));
            String html = responseStatus.getContent();
            if (!html.contains("未找到") && html.contains("该单位备案网站") && html.contains("缓存于"))
            {
                Document doc = Jsoup.parse(html);

                Elements trs = doc.select("div#company .table-s1 tbody tr");
                for (Element tr : trs) {
                    String title = tr.select("td").get(1).text();
                    String dom = tr.select("td").get(2).text();
                    map.put(dom,title);
                }
            }
        } catch (Exception e) {
            task.getExceptions().add(e);
        }
        SysLog.info("ICP反查结束");
        return map;
    }


    public List<String> chinazReverse(String domain){
        ArrayList<String> arr = new ArrayList<>();
        HttpClient httpClient = new HttpClient();
        Document parse = null;
        try {
            ResponseStatus responseStatus = httpClient.get("http://whois.chinaz.com/" + domain);
            Document doc = Jsoup.parse(responseStatus.getContent());
            if (doc.toString().contains("邮箱反查")){
                String href = doc.select("a:contains(邮箱反查)").attr("href");

                ResponseStatus resp = httpClient.get("http://whois.chinaz.com/" + href);
                parse = Jsoup.parse(resp.getContent());
            }
        } catch (Exception e) {
            task.getExceptions().add(e);
            return arr;
        }

        if (parse!=null)
        {
            Elements items = parse.select(".Wholist .domain");

            for (Element item : items) {
                String text = item.select("div.listOther a").text();
                arr.add(text);
            }

        }
        SysLog.info("邮箱反查结束");
        return arr;
    }

}
