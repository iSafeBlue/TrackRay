package com.trackray.base.utils;

import com.trackray.base.bean.Constant;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;
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

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
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


}
