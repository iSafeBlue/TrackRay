package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.SysLog;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.javaweb.core.net.HttpRequest;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(enable = false)
@Plugin(value = "simpleVulRule" ,title = "简单CMS漏洞检测规则" , author = "浅蓝")
public class SimpleVulRule extends InnerPlugin<List<Vulnerable>> {
    private String target;
    private List<Vulnerable> vulns = new ArrayList<>();

    private List<Payloader> doSwitch(FingerPrint finger) {
        List<Payloader> loaders = new ArrayList<>();
        switch (finger) {
            case $phpcms:

                loaders.add(
                        Payloader.builder()
                                .url("/api.php?op=get_menu&act=ajax_getlist&callback=aaaaa&parentid=0&key=authkey&cachefile=..\\..\\..\\phpsso_server\\caches\\caches_admin\\caches_data\\applist&path=admin")
                                .custom(
                                        new Custom() {
                                            @Override
                                            public boolean fun( HttpResponse response) throws Exception {
                                                return response.body().contains("aaaaa")?true:false;//如果响应体包含aaaaa则存在漏洞
                                            }
                                        }
                                ).vuln(Vulnerable.builder().level(Vulnerable.Level.HIGH.getLevel())
                                .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                                .title("phpcms authkey泄漏")
                                .build())
                                .build()
                );

                loaders.add(
                        Payloader.builder()
                                .url("/api.php?op=get_menu&act=ajax_getlist&callback=aaaaa&parentid=0&key=authkey&cachefile=..\\..\\..\\phpsso_server\\caches\\caches_admin\\caches_data\\applist&path=admin")
                                .method(HttpRequest.Method.GET)
                                .containsStr("aaaaa")
                                .vuln(
                                        Vulnerable.builder()
                                                .level(Vulnerable.Level.HIGH.getLevel())
                                                .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                                                .title("phpcms authkey泄漏")
                                                .build()) //另一种实现方式
                                .build()
                );

                break;
            case $Discuz:
                break;
            default:
                /**
                 * 以上都没有匹配时执行
                 */
                break;
        }
        /****  通用规则 *****/
        loaders.add(
                Payloader.builder()
                        .url("/crossdomain.xml")
                        .containsStr("domain=\"*\"")
                        .vuln(
                                Vulnerable.
                                        builder().
                                        level(Vulnerable.Level.LOW.getLevel())
                                        .type(Vulnerable.Type.CONFIG_ERROR.getType())
                                        .title("crossdomain.xml 配置不当").build()
                        ).build()
        );

        loaders.add(
                Payloader.builder().url("/WebResource.axd?d=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1")
                        .containsStr("Microsoft .NET Framework")
                        .vuln(Vulnerable.builder()
                                .level(Vulnerable.Level.LOW.getLevel())
                                .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                                .payload("/WebResource.axd?d=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1")
                                .title("ASP.NET Padding Oracle信息泄露漏洞").build()).build()
        );

        /*************************JBOSS*************************/
        loaders.add(
                Payloader.builder().url("/examples/servlets/index.html")
                        .containsStr("Servlet Examples")
                        .vuln(Vulnerable.builder()
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .type(Vulnerable.Type.UNAUTHORIZED_ACCESS.getType())
                                .title("Tomcat样例信息泄露漏洞").build()).build()
        );

        /*******************************************************/


        /*************************JBOSS*************************/
        loaders.add(
                Payloader.builder().url("/jmx-console/")
                        .containsStr("JMX Agent")
                        .vuln(Vulnerable.builder()
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .type(Vulnerable.Type.UNAUTHORIZED_ACCESS.getType())
                                .title("JBOSS /jmx-console/ 未授权访问").build()).build()
        );
        loaders.add(
                Payloader.builder().url("/web-console/ServerInfo.jsp")
                        .containsStr("JBoss Management")
                        .vuln(Vulnerable.builder()
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .type(Vulnerable.Type.UNAUTHORIZED_ACCESS.getType())
                                .title("JBOSS /web-console/ 未授权访问").build()).build()
        );
        loaders.add(
                Payloader.builder().url("/invoker/readonly/")
                        .containsStr("ObjectInputStream")
                        .vuln(Vulnerable.builder()
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .type(Vulnerable.Type.DESERIALIZE.getType())
                                .title("JBOSSAS 5.x/6.x 反序列化命令执行漏洞 ").build()).build()
        );

        /*******************************************************/


        loaders.add(
                Payloader.builder()
                        .url("/robots.txt")
                        .method(HttpRequest.Method.GET)
                        .custom(
                                new Custom() {
                                    @Override
                                    public boolean fun(HttpResponse response) throws Exception {
                                        String body = response.body();
                                        if (body==null)
                                            return false;

                                        if (StringUtils.containsAny(body," for ","config","admin","install"))
                                            return true;
                                        return false;
                                    }
                                }
                        )
                        .vuln(
                                Vulnerable.builder()
                                        .level(Vulnerable.Level.INFO.getLevel())
                                        .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                                        .title("robots.txt 存在敏感路径")
                                        .build())
                        .build()
        );


        return loaders;
    }

    @Override
    public void before() {
        target = this.task.getTargetStr();
    }

    @Override
    public void process() {
        result = start();
    }

    @Override
    public List<Vulnerable> start() {
        if (null!=(task)){
            FingerPrint finger = task.getResult().getSystemInfo().getFinger();
            if (finger!=null){
                test(doSwitch(finger));
            }
        }
        return vulns;
    }

    @Override
    public void after(Object... args) {

        if (result!=null){
            for (Vulnerable vulnerable : result) {
                addVulnerable(vulnerable);
            }
        }
    }

    private void test(List<Payloader> payloaders) {
        SysLog.info("符合要求的有"+payloaders.size()+"个简单的漏洞规则");
        for (Payloader payloader : payloaders) {

            HttpURLRequest req = payloader.getRequest();
            try {
                HttpResponse resp = null;
                if (req == null) {
                    req = new HttpURLRequest()
                            .url(payloader.url)
                            .method(payloader.method);
                    if (StringUtils.isNotBlank(payloader.data)) {
                        req.data(payloader.data);
                    }

                    if (payloader.header != null) {
                        req.header(payloader.header);
                    }

                    if (payloader.method == HttpRequest.Method.GET)
                        resp = req.get();
                    else
                        resp = req.post();

                }else{
                    resp = req.request();
                }


                if (payloader.getCustom()!=null){
                    try {
                        if (payloader.custom.fun( resp )){
                            addVul(payloader);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }else{


                    if (StringUtils.isNotBlank(payloader.containsStr)){
                        if (StringUtils.contains(resp.body() , payloader.containsStr)){
                            addVul(payloader);
                        }

                    }else if(StringUtils.isNotBlank(payloader.matche)){
                        if (resp.body().matches(payloader.containsStr)){
                            addVul(payloader);
                        }
                    }
                }


            }catch (Exception e){}


        }
    }
    private void addVul(Payloader payloader){
        payloader.vuln.setAddress(payloader.url);
        payloader.vuln.setPayload(payloader.data);
        vulns.add(payloader.vuln);
    }

    interface Custom{
        public boolean fun(HttpResponse response) throws Exception;
    }

    @Data
    @Builder
    static class Payloader{
        private HttpURLRequest request;
        private String url="";
        private HttpRequest.Method method = HttpRequest.Method.GET;
        private String data="";
        private Map<String,String > header;
        private String containsStr="";  //文字包含匹配
        private String matche = ""; //正则表达式匹配
        private Vulnerable vuln;
        private Custom custom;

    }
}
