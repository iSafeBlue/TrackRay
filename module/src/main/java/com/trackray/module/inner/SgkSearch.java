package com.trackray.module.inner;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.HackKit;
import com.trackray.base.plugin.InnerPlugin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpRequest;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/6/24 10:49
 */
@Plugin(title = "信息泄露查询" , author = "浅蓝" )
@Rule(
        params = {
                @Param(key = "email" )
        }
)
public class SgkSearch extends InnerPlugin<Map<String,String>> {

    public static final String EMAIL = "email";

    @Override
    public void process() {

        String email = this.getParam().get(EMAIL).toString();

        HashMap<String, String> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(email)){

            Map<String,String> v1 = this.monitorFirefox(email);

            resultMap.putAll(v1);

        }

        this.result = resultMap;

    }
    private Map<String, String> haveibeenPwned(String email) {
        String url = String.format("https://haveibeenpwned.com/unifiedsearch/%s" , email);
        HashMap<String, String> map = new HashMap<>();
        try {
            String body = requests.url(url).get().body();
            JSONObject json = JSONObject.fromObject(body);
            JSONArray breaches = json.getJSONArray("Breaches");
            for (int i = 0; i < breaches.size(); i++) {
                JSONObject object = breaches.getJSONObject(i);

                String name = object.getString("Name");
                String breachDate = object.getString("BreachDate");
                map.put(name,breachDate);
            }


        } catch (Exception e) {
            log.error(e.toString());
        }


        return map;
    }

    private Map<String, String> monitorFirefox(String email) {
        String url = "https://monitor.firefox.com/";
        HashMap<String, String> map = new HashMap<>();
        try {
            HttpResponse res = requests.url(url).get();
            Map<String, String> cookies = res.getCookies();
            String cookie = "";
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookie += ( entry.getKey() + "=" + entry.getValue() + ";" );
            }

            Document parse = res.parse();

            if (parse!=null){
                String csrf = parse.select("#scan-user-email input").first().val();
                String emailHash = hackKit.enDecrypt.sha1.SHA1(email).toUpperCase();
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("_csrf",csrf);
                requestMap.put("pageToken","");
                requestMap.put("scannedEmailId","1");
                requestMap.put("email",email);
                requestMap.put("emailHash", emailHash);
                HttpResponse response = requests.url(url + "scan")
                        .contentType("application/x-www-form-urlencoded")
                        .data(requestMap)
                        .cookie(cookie)
                        .method(HttpRequest.Method.POST).request();

                Document result = response.parse();

                Elements flx = result.select(".breach-info-wrapper > .flx");

                if (!flx.isEmpty()){

                    for (Element element : flx) {
                        String title = element.select(".breach-title").text();
                        String time = element.select("span:contains(时间)+span").text();
                        map.put(title,time);
                    }


                }

            }

        } catch (Exception e) {
            log.error(e.toString());
        }

        return map;
    }


}
