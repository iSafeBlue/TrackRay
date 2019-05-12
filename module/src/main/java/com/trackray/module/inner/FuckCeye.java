package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.plugin.InnerPlugin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/10 12:56
 */
@Plugin(title = "ceye.io 插件" ,desc = "ceye.io DNSlog 回显插件，可以查询http和dns日志",author = "浅蓝")
public class FuckCeye extends InnerPlugin<Boolean> {
    @Value("${ceye.io.token}")
    public String token;
    @Value("${ceye.io.identifier}")
    public String identifier;

    private String template = "http://api.ceye.io/v1/records?token=%s&type=%s&filter=%s";

    public String formatURL(String type , String keyword){
        return String.format(template,token,type,keyword);
    }
    public JSONObject get(String url){
        try {
            return JSONObject.fromObject(requests.url(url).get().body());
        } catch (MalformedURLException e) {
            return new JSONObject();
        }
    }
    public boolean check(){
        return check(this.param);
    }
    @Override
    public boolean check(Map param) {
        if (StringUtils.isAnyBlank(token,identifier))
            return false;
        String url = formatURL("dns", "");
        JSONObject obj = get(url);
        return !obj.isNullObject() && obj.has("meta") && obj.getJSONObject("meta").getInt("code") ==200;
    }
    public static final String NAME = "name";
    public static final String REMOTE_ADDR= "remote_addr";
    public static final String CREATED_AT = "created_at";

    public JSONObject searchDNS(String keyword){
        return get(formatURL("dns",keyword));
    }

    public JSONObject searchHTTP(String keyword){
        return get(formatURL("http",keyword));
    }

    public int searchCount(String keyword){
        int i = 0;
        i+= searchDNSCount(keyword);
        i+= searchHTTPCount(keyword);
        return i;
    }
    public int searchDNSCount(String keyword){
        JSONObject dns = searchDNS(keyword);
        return dns.getJSONArray("data").size();
    }
    public int searchHTTPCount(String keyword){
        JSONObject http = searchHTTP(keyword);
        return http.getJSONArray("data").size();
    }

    @Override
    public void process() {
        result = true;
    }

}
