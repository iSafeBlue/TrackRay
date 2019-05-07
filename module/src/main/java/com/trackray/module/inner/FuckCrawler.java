package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.PageUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.SysLog;
import org.apache.http.Header;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/1 22:25
 */
@Plugin(title = "网页爬虫插件" , author = "浅蓝")
public class FuckCrawler extends InnerPlugin {

    /**
     * TODO:懒得写爬虫，暂时用简单的，后期考虑使用webdriver做爬虫。
     */

    @Autowired
    private WebApplicationContext webApplicationConnect;
    private Map<String, CrawlerPlugin> plugins;

    public static String[] suffix = {".css",".js",".jpg",".jpeg",".gif",".png",".bmp",".html",".htm",".swf",".ico",".ttf",".woff",".svg",".cur",".woff2"};
    private int deep = 4;                                                  //设置深度为2
    private int height = deep-1;       //这是为了后面的循环方便
    private int numFindUrl = 0;     //find the number of url
    public int ERROR_MAX =  10;
    public int SPIDER_MAX = 50;
    public int success;
    private int error = 0;

    private ArrayList<ArrayList<String>> urlWaiting1 = new ArrayList<ArrayList<String>>();      //等待被处理的url，声明二维的原因是想第一维储存搜索到了树的第几层，第二维存储了这一层所有的链接
    private ArrayList<String> urlWaiting2=new ArrayList<String>();
    private ArrayList<String> urlProcessed = new ArrayList<String>();   //处理过的url
    private ArrayList<String> urlError = new ArrayList<String>();       //错误的url

    @Override
    public void before() {
        //deep = super.task.getSpiderDeep();
        SPIDER_MAX = super.task.getMaxSpider();

        plugins = webApplicationConnect.getBeansOfType(CrawlerPlugin.class);
        for (CrawlerPlugin plugin : plugins.values()) {
            plugin.setTask(this.task);
        }


        this.addURL(this.task.getTargetStr(),0);

    }

    @Override
    public void process() {

        for(int i=0;i<height;i++)       //一层一层的处理
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
                //task.getExceptions().add(e);
                break;
            }
        }

    }

    public void processURL(String strUrl,int height) {  //处理url，传height的参的目的是记录当前处理的url是第几层的
        if (!strUrl.contains(task.getResult().getHostInfo().getHost()))
            return;
        SysLog.info("crawler:"+strUrl);
        URL url = null;
        try {
            url = new URL(strUrl);
            HttpURLRequest httpRequest = requests.url(url);
            HttpResponse httpResponse = httpRequest.get();
            processPlugin(httpResponse);
            int code = httpResponse.getStatusCode();
            /*if (code ==302 || code == 301){
                for (Map.Entry<String, List<String>> header : httpResponse.getHeader().entrySet()) {
                    if (header.getKey().contains("Location")){
                        if (header.getValue().contains("://") && !header.getValue().get(0).matches("^(http|https)://(\\S+\\.)*"+url.getHost()+"/.*")){
                            break;
                        }else{
                            fetcher.run(crawlerPage);
                            break;
                        }
                    }
                }

            }*/

            Reader r = new StringReader(httpResponse.body());

            HTMLEditorKit.Parser parse = new HTMLParse().getParser();

            parse.parse(r, new Parser(url,height), true); //解析url

        } catch (IOException e) {
            urlError.add(strUrl.toString());
            task.getExceptions().add(e);
            return;
        }
        urlProcessed.add(strUrl);  //添加url为已经处理
        success++;
    }

    private void processPlugin(HttpResponse response) {
        if (response!=null){
            for (Map.Entry<String, CrawlerPlugin> entry : plugins.entrySet()) {
                CrawlerPlugin plugin = entry.getValue();
                plugin.target = response.getUrl();
                plugin.response = response;
                this.task.getExecutor().submit(plugin);
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
        if (!url.contains(task.getResult().getHostInfo().getHost()))
            return;
        /*if (urlProcessed.contains(u)) {
            return;
        }*/
        if (urlProcessed.contains(url)) {
            return;
        }
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

            if (task.getTarget().type == Constant.IP_TYPE){
                root = task.getResult().getHostInfo().getRealIP();
            }else if (task.getTarget().type == Constant.URL_TYPE){
                root = task.getResult().getHostInfo().getRootDomain();
            }else{
                return;
            }

            try {
                for (String s : suffix) {
                    if (new URL(href).getFile().endsWith(s))
                        return;
                }
                /*if (href.matches(regex))
                    return;*/
            } catch (MalformedURLException e) {

            }

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
            task.getExceptions().add(e);
        }
    }


}
