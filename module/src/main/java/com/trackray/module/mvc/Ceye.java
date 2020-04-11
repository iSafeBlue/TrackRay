package com.trackray.module.mvc;

import com.trackray.base.annotation.Option;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.base.store.SettingDTO;
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
@Rule(auth = true , options =
        {
                @Option(name = "ceye域名", formName = "domain", defaultValue = "yourdomain.ceye.io" , description = "填写ceye分配的域名"),
                @Option(name = "secret", formName = "secret", defaultValue = ""),
                @Option(name = "配置文件优先", formName = "frist", defaultValue = "true" , description = "[false|true]是否优先使用application.properties的配置")
        })
public class Ceye extends MVCPlugin {
    @Autowired
    private FuckCeye fuckCeye;

    @Override
    public void index() {

        List<SettingDTO> ceyeSetting = settingRepository.findAllByPluginKey("ceye");
        SettingDTO domainSetting = ceyeSetting.get(0);
        SettingDTO secretSetting = ceyeSetting.get(1);
        SettingDTO fristSetting = ceyeSetting.get(2);
        if (!Boolean.parseBoolean(fristSetting.getFormValue())){
            fuckCeye.token = secretSetting.getFormValue();
            fuckCeye.identifier = domainSetting.getFormValue();
        }

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
