package com.trackray.base.attack;

import com.trackray.base.bean.*;
import com.trackray.base.enums.*;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.PageUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class Crawler {

    private String[] suffix = {".css",".js",".jpg",".jpeg",".gif",".png",".bmp",".html",".htm",".swf",".ico",".ttf",".woff",".svg",".cur",".woff2"};
    private CrawlerPage crawlerPage;
    private Fetcher fetcher = new Fetcher();
    private ArrayList<ArrayList<String>> urlWaiting1 = new ArrayList<ArrayList<String>>();      //等待被处理的url，声明二维的原因是想第一维储存搜索到了树的第几层，第二维存储了这一层所有的链接
    private ArrayList<String> urlWaiting2=new ArrayList<String>();
    private ArrayList<String> urlProcessed = new ArrayList<String>();   //处理过的url
    private ArrayList<String> urlError = new ArrayList<String>();       //错误的url
    public ArrayList<String> hasVul = new ArrayList<String>();       //包含漏洞的文件
    static int deep=4;                                                  //设置深度为2
    private String regex = "";
    private int numFindUrl = 0;     //find the number of url
    static int height=deep-1;       //这是为了后面的循环方便
    public List<SQLMap> sqlmaps = new ArrayList<>();
    private String target;
    private int error = 0;
    public int ERROR_MAX =  10;
    public int SPIDER_MAX;
    public int success;
    private boolean useProxy = false;
    private Map<String, CrawlerPlugin> plugins;
    private WebApplicationContext webApplicationContext;


    public Crawler(CrawlerPage crawlerPage, WebApplicationContext appContext) {
        webApplicationContext = appContext;
        crawlerPage.setRedirect(true);
        this.crawlerPage = crawlerPage;
        deep = crawlerPage.getTask().getSpiderDeep();
        Map<String, Integer> proxyMap = crawlerPage.getTask().getProxyMap();
        if (!proxyMap.isEmpty()){
            useProxy=true;
            PageUtils.copyTaskProxy(crawlerPage.getTask(),crawlerPage);
        }

        plugins = webApplicationContext.getBeansOfType(CrawlerPlugin.class);
        {
            String reg = ".+";
            String exp = "";
            for (String s : suffix) {
                exp = exp + ("\\"+s+"(\\?.*)?|");
            }
            exp = exp.substring(0,exp.length()-1);
            exp = "("+exp+")";
            this.regex = reg.concat(exp);
        }
    }

    /**
     * start crawling
     */
    public void begin() {            //开始抓取


            for(int i=0;i<=height;i++)       //一层一层的处理
        {
            try {
                ArrayList<String> urlWaiting_temp=new ArrayList<String>();
                urlWaiting_temp=urlWaiting1.get(i);
                while(!urlWaiting_temp.isEmpty()) {   //如果这一层非空就处理
                    if (success >= SPIDER_MAX){
                        break;
                    }
                    try {
                        processURL(urlWaiting_temp.remove(0), i); //处理url函数，后面有定义
                    }catch (RuntimeException e){
                        error++;
                        if (error >= ERROR_MAX){
                            break;
                        }else{
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                System.out.println(e);
                break;
            }
        }
        //抓取结束后的一些日志，后面有log函数的定义
        log("finish crawling");
        log("the number of urls that were found:" + numFindUrl);
        log("the number of urls that were processed:" + urlProcessed.size());
        log("the number of urls that were xss:" + hasVul.size());
        log("the number of urls that resulted in an error:" + urlError.size());
    }

    public void processURL(String strUrl,int height) {  //处理url，传height的参的目的是记录当前处理的url是第几层的
        URL url = null;
        try {
            url = new URL(strUrl);
            crawlerPage.getRequest().setUrl(strUrl);
            log("Processing: " + strUrl);
            fetcher.run(crawlerPage);
            processPlugin(crawlerPage);
            int code = crawlerPage.getResponse().getStatus().getStatusCode();
            if (code ==302 || code == 301){
                for (Header header : crawlerPage.getResponse().getStatus().getHeaders()) {
                    if (header.getName().contains("Location")){
                        if (header.getValue().contains("://") && !header.getValue().matches("^(http|https)://(\\S+\\.)*"+url.getHost()+"/.*")){
                            break;
                        }else{
                            fetcher.run(crawlerPage);
                            break;
                        }
                    }
                }

            }

            Reader r = new StringReader(PageUtils.getContent(crawlerPage));
            /*if (true){
                processFinger(crawlerPage);
            }
            findInfo(crawlerPage);

            if (strUrl.matches(".*\\?.*=.*"))
            {
                processXSS(crawlerPage);
            }
            */

            // parse the URL
            HTMLEditorKit.Parser parse = new HTMLParse().getParser();
            parse.parse(r, new Parser(url,height), true); //解析url


        } catch (IOException e) {
            urlError.add(strUrl.toString());
            log("Error: " + strUrl);
            return;
        }
        urlProcessed.add(strUrl);  //添加url为已经处理
        log("Complete: " + strUrl);
        success++;
    }

    private void processPlugin(CrawlerPage crawlerPage) {
        for (Map.Entry<String, CrawlerPlugin> entry : plugins.entrySet()) {
            CrawlerPlugin plugin = entry.getValue();
            plugin.target = crawlerPage.getRequest().getUrl();
            plugin.crawlerPage = (crawlerPage.clone());

            plugin.crawler = this;
            plugin.executor();
        }
    }

    private void findInfo(CrawlerPage crawlerPage) {
        ResponseStatus status = crawlerPage.getResponse().getStatus();
        if (status.getContentTypeString().contains("htm"))
        {
            String content = status.getContent();
            if (StringUtils.isNotBlank(content))
            {
                String tel = ReUtils.getTel(content);
                String mail = ReUtils.getMail(content);
                if (null!=tel){
                    List<String> tel1 = crawlerPage.getTask().getResult()
                            .getSenseInfo().getTel();
                    if (!tel1.contains(tel))
                        tel1.add(tel);
                }

                if (null!=mail)
                {
                    List<String> email = crawlerPage.getTask().getResult().getSenseInfo().getEmail();
                    if (!email.contains(mail)){
                        email.add(mail);
                        if (mail.contains("@qq.com")){
                            crawlerPage.getTask().getResult().getSenseInfo().getNum().add(RegexUtil.extractStr(mail,"\\d*qq"));
                        }
                    }
                }
            }
        }
    }

    private void processSQLI(String url) {
        SQLMap sqlMap = new SQLMap(url);
        if (sqlMap.start()){
            log("start sql injection scan sqlmap taskid="+sqlMap.getTaskid());
            sqlmaps.add(sqlMap);
        }
    }

    private void processXSS(CrawlerPage crawlerPage) {
        String url = crawlerPage.getRequest().getUrl();
        log("start process XSS "+url);
        URL u = PageUtils.getURL(url);
        String urlstring = this.urltoString(u);
        for (String s : hasVul) {
            if (s.contains(urlstring)){
                return;
            }
        }
        /*if (hasVul.contains(urlstring)){
            return;
        }*/
        hasVul.add(url);
        processSQLI(url);
        if (url.matches(Constant.Vuln.FILE_READ_VULN_REGEX)){
            Vulnerable build = Vulnerable.builder().request(url).
                    description("该链接可能会存在文件读取/包含等漏洞").
                    level(VulnLevel.DANGER.getLevel()).vulType(VulnType.FILE_OPERATION.getType()).build();
            crawlerPage.getTask().getResult().getItems().get(crawlerPage.getBase()).getVulns().add(build);
        }
        Map<String, String> param = PageUtils.getParam(u);
        Set<String> keys = param.keySet();
        Object[] karr = keys.toArray();
        for (String payload : Payload.xssPayload) {
            for (int i=0;i<keys.size();i++){
                String key = (String) karr[i];
                StringBuffer path = new StringBuffer(urlstring.concat("?"));
                for (String k : keys) {
                    String v = param.get(k);
                    if (k.equals(key)) {
                        path.append(k + ("="));
                        path.append(v + (payload));
                        path.append("&");
                    }else{
                        path.append(k + ("="));
                        path.append(v );
                        path.append("&");
                    }
                }

                path.delete(path.lastIndexOf("&"),path.length());
                crawlerPage.getRequest().setUrl(path.toString());
                crawlerPage.getRequest().setHttpMethod(HttpMethod.GET);
                try {
                    fetcher.run(crawlerPage);
                }catch (Exception e){
                    log("ERROR "+url);
                    continue;
                }
                String content = PageUtils.getContent(crawlerPage);
                if (content.contains(payload)){
                    //存在漏洞
                    log("xss vulnerable "+path.toString());
                    Vulnerable build = Vulnerable.builder().description("通过爬虫检测出来该链接存在XSS漏洞 param=" + key)
                            .affectsUrl(path.toString()).vulName(VulnType.XSS.getName()).vulType(VulnType.XSS.getType()).level(VulnLevel.DANGER.getLevel()).build();
                    crawlerPage.getTask().getResult().getItems().get(crawlerPage.getBase()).getVulns()
                            .add(build);
                    return;

                }
            }

        }

    }

    private String urltoString(URL u) {
        int len = u.getProtocol().length() + 1;
        if (u.getAuthority() != null && u.getAuthority().length() > 0)
            len += 2 + u.getAuthority().length();
        if (u.getPath() != null) {
            len += u.getPath().length();
        }
        if (u.getQuery() != null) {
            len += 1 + u.getQuery().length();
        }
        if (u.getRef() != null)
            len += 1 + u.getRef().length();

        StringBuffer result = new StringBuffer(len);
        result.append(u.getProtocol());
        result.append(":");
        if (u.getAuthority() != null && u.getAuthority().length() > 0) {
            result.append("//");
            result.append(u.getAuthority());
        }
        if (u.getPath() != null) {
            result.append(u.getPath());
        }
        return result.toString();
    }

    private void processFinger(CrawlerPage crawlerPage) {
        Task task = crawlerPage.getTask();
        String content = crawlerPage.getResponse().getStatus().getContent();
        Language language = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getLanguage();
        WEBServer webServer = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getWebServer();
        if(language==null || language==Language.OTHER  || webServer==null || webServer == WEBServer.OTHER){
                Header[] responseHeader = crawlerPage.getResponse().getStatus().getHeaders();
                PageUtils.fingerServer(responseHeader,crawlerPage.getBase(),task);
        }
        language = task.getResult().getItems().get(crawlerPage.getBase()).getSystemInfo().getLanguage();
        if(language==null || language==Language.OTHER ) {
            String url = crawlerPage.getRequest().getUrl();
            URL u = PageUtils.getURL(url);
            url = u.getPath();
            PageUtils.fingerLang(url,crawlerPage.getBase(),task);
        }

        /*for (Map.Entry<String, String> entry : Constant.Vuln.FINGERS_REGEX.entrySet()) {
            String regex = entry.getKey();
            String finger = entry.getValue();
            if (content.matches(regex)){
                task.getResult().getSystemInfo().getFinger().put(finger,crawlerPage.getRequest().getUrl());
                break;
            }
        }*/

    }


    public void addURL(String url,int height) { //添加url时考虑到树的深度和urlWaiting1是二维的特性，代码可能写的有一些冗余了
        String u = this.urltoString(PageUtils.getURL(url));
        for(int i=0;i<urlWaiting1.size();i++)
        {
            /*ArrayList<String> list = urlWaiting1.get(i);
            for (String urlstr : list) {
                if (urlstr.contains(u)){
                    return;
                }
            }*/
            if(urlWaiting1.get(i).contains(url))
            {
                return;
            }
        }
        if (urlError.contains(url)) {

            return;
        }
        /*if (urlProcessed.contains(u)) {
            return;
        }*/
        if (urlProcessed.contains(url)) {
            return;
        }
        log("Adding to workload: " + url);
        if(height==0) {
            urlWaiting2.add(url);
            urlWaiting1.add(height, urlWaiting2);
            numFindUrl++;
        }
        else
        {

            if(urlWaiting1.size()<=height)   //判断如果是新的一层的url（也就是height增加了，就为urlWaiting1的索引加1。
            {
                ArrayList<String> urlTemp=new ArrayList<String>();
                urlTemp.add(url);
                urlWaiting1.add(height, urlTemp);
                numFindUrl++;
            }
            else {                       //如果这个url到来之前这一层已经有url了，那就先把那一层所有的url拿出来，把它放进去，再全放回urlWaiting1里。
                urlWaiting2 = urlWaiting1.remove(height);
                urlWaiting2.add(url);
                urlWaiting1.add(height, urlWaiting2);
                numFindUrl++;
            }
        }

    }

    public void log(String entry) {
        System.out.println((new Date()) + ":" + entry);
    }

    //重设getParser为public
    protected class HTMLParse extends HTMLEditorKit {
        public Parser getParser() {
            return super.getParser();
        }
    }

    protected class Parser extends HTMLEditorKit.ParserCallback {
        protected URL base;
        protected int height;
        public Parser(URL base,int height) {
            this.base = base;
            this.height=height;

        }
        //一个主要的回调函数，这里值关心了href标签
        public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {


            String href = (String) a.getAttribute(HTML.Attribute.HREF);

            if ((href == null) && (t == HTML.Tag.FRAME))
                href = (String) a.getAttribute(HTML.Attribute.SRC);

            if (href == null) {

                return;

            }
            int i = href.indexOf('#');
            if (i != -1)
                href = href.substring(0, i);

            if (href.toLowerCase().startsWith("mailto:"))
                return;
            if (href.toLowerCase().startsWith("tel:"))
                return;
            if (href.toLowerCase().startsWith("javascript:"))
                return;
            String root = "";

            if (crawlerPage.getTask().getTarget().type == Constant.IP_TYPE){
                root = ReUtils.getIp(crawlerPage.getTask().getTargetStr());
            }else if (crawlerPage.getTask().getTarget().type == Constant.URL_TYPE){
                root = ReUtils.getDomain(crawlerPage.getTask().getTargetStr());
            }else{
                return;
            }



            if (href.matches(regex))
                return;

            if (href.startsWith("http")){
                if (!href.matches("^(http|https)://(\\S+\\.)*"+root+"/.*")){
                    return;
                }else{
                    addURL(href,height+1);
                    return;
                }
            }

            handleLink(base, href,height+1);
            //log(base+href);
        }

        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            handleSimpleTag(t, a, pos); // handle the same way

        }


    }
    public void handleLink(URL base, String str,int height) {
        try {
            URL url = new URL(base, str); //base为当前路径，str为相对路径，这样加一起组成绝对路径。
            addURL(url.toString(),height);


        } catch (MalformedURLException e) {
            log("Found malformed URL: " + str);
        }
    }

    public List<SQLMap> getSqlmaps() {
        return sqlmaps;
    }

    public void setSqlmaps(List<SQLMap> sqlmaps) {
        this.sqlmaps = sqlmaps;
    }
}

