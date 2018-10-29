package com.trackray.scanner.utils;

import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.enums.HttpMethod;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import com.trackray.scanner.httpclient.HttpClient;
import com.trackray.scanner.httpclient.ResponseStatus;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ApiUtils {
    private static final String CENSYS_API = "https://censys.io/api/";
    private static final String TOOLLU_WHOIS_API = "https://tool.lu/whois/ajax.html";
    private static final String AIZHAN_API = "https://whois.aizhan.com/%s/";
    private static final String CHINAZ_SAME_API = "http://s.tool.chinaz.com/same";

    private static final CrawlerPage page = new CrawlerPage();
    private static final Fetcher f = new Fetcher();
    public static List<JSONObject> censys(String query){
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

        CrawlerPage page = new CrawlerPage();
        page.getRequest().setUrl(CENSYS_API.concat("v1/search/ipv4"));
        String sign = Base64.getEncoder().encodeToString((Constant.CENSYS_APPID+":"+Constant.CENSYS_SECRET).getBytes());
        String auth = "Basic ".concat(sign);
        page.getRequest().addHttpHeader("Authorization",auth);
        page.getRequest().setHttpMethod(HttpMethod.POST);
        JSONObject obj = new JSONObject();
        obj.put("query",query);
        obj.put("fields","[\"ip\",\"protocols\"]");
        page.getRequest().setParamStr(obj.toString());
        page.getRequest().setTimeout(60000);
        try {
            new Fetcher().run(page);
        }catch (Exception e){
            System.out.println("超时");
            return arr;
        }
        ResponseStatus status = page.getResponse().getStatus();
        if (status != null){
            if(status.getStatusCode() == 200){
                if (StringUtils.isNotBlank(status.getContent())){
                    JSONObject resp = page.getResponse().getRespFormatJson();
                    if (resp.optString("status").equals("ok")){
                        JSONArray results = resp.optJSONArray("results");
                        for (Object result : results) {
                            try {
                                JSONObject res = (JSONObject) result;
                                arr.add(res);
                            }catch (Exception e){
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return arr;
    }


    public static String tooluWhois(String domain){
        HttpClient client = new HttpClient();
        HashMap<String, Object> map = new HashMap<>();
        map.put("sld",domain);
        ResponseStatus post = client.post(TOOLLU_WHOIS_API,map);
        if (post.getStatusCode() == 200 && !post.getContent().isEmpty()) {
            SysLog.info("已扫描到WHOIS");
            return post.getContent();
        }
        return "";
    }
    public static List<String> chinazReverse(String domain){
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
        } catch (HttpException e) {
            e.printStackTrace();
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
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
    public static List<String> aizhanReverseRegisted(String domain){
        ArrayList<String> arr = new ArrayList<>();
        String url = String.format(AIZHAN_API, domain);
        CrawlerPage page = new CrawlerPage();
        page.getRequest().setUrl(url);
        Fetcher fetcher = new Fetcher();
        try {

        fetcher.run(page);
        if (page.getResponse().getStatus().getStatusCode() == 200 && page.getResponse().getStatus().getContent().contains("反查邮箱")){
            String content = page.getResponse().getStatus().getContent();
                String md5 = RegexUtil.extractStr(content, "([a-fA-F0-9]{32})");
                String param = "?domain="+domain+"&cc="+md5+"&rn=1531734119&update=true";
                page.getRequest().setUrl("https://whois.aizhan.com/api/whois"+param);
                fetcher.run(page);
            String content1 = page.getResponse().getStatus().getContent();
            JSONObject json = page.getResponse().getRespFormatJson();
                String email = json.optJSONObject("regrinfo").optString("email");

            if (ReUtils.isMail(email)) {
                String emailurl = "https://whois.aizhan.com/reverse-whois?q="+email+"&t=email";
                page.getRequest().setUrl("https://whois.aizhan.com".concat(emailurl));
                fetcher.run(page);
                Document respFormatHtml = page.getResponse().getRespFormatHtml();
                if (page.getResponse().getStatus().getContent().contains("url domain")) {
                    Document parse = page.getResponse().getRespFormatHtml();
                    Elements elements = parse.select("tr.url.domain a");
                    for (Element element : elements) {
                        String text = element.text();
                        arr.add(text);
                    }
                }
            }
        }
        }catch (Exception e){
            return arr;
        }

        return arr;
    }

    public static Map<String,String> aizhanIcp(String domain){
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
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SysLog.info("ICP反查结束");
        return map;
    }

    public static List<String> chinazSame(String ip){
        List<String> result = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<String, String>();
        int total = 5;
        for (int page=1;page<=total;page++) {
            String url = CHINAZ_SAME_API.concat("?s="+ip+"&page="+page);
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

    public static void main(String[] args) throws IOException {

        List<String> strings = chinazSame("116.114.20.53");

        System.out.println(strings.size());

        System.out.println(strings);
    }

    public static String initSQLMap(String api) {

        page.getRequest().setUrl(api+"/task/new");
        f.run(page);
        JSONObject resp = page.getResponse().getRespFormatJson();
        return resp.optString("taskid");
    }
}
