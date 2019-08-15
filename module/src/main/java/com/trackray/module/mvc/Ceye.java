package com.trackray.module.mvc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.FuckCeye;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/8 18:08
 */
@Plugin(value = "ceye" ,title = "CEYE日志", author = "浅蓝")
@Rule(auth = true)
public class Ceye extends MVCPlugin {
    @Autowired
    private FuckCeye fuckCeye;

    @Override
    public void index() {
        JSONObject http = fuckCeye.searchHTTP("");
        if (http !=null && http.containsKey("data") && http.getJSONArray("data").isArray()){
            List data = http.getJSONArray("data");
            model.addObject("http",data);
        }
        JSONObject dns = fuckCeye.searchDNS("");
        if (dns !=null && dns.containsKey("data") && dns.getJSONArray("data").isArray()){
            List data = dns.getJSONArray("data");
            model.addObject("dns",data);
        }
        String identifier = fuckCeye.identifier;
        model.addObject("identifier",identifier);
        model.setViewName("index");
    }
}
