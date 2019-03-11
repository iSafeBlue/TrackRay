package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.Payload;
import com.trackray.base.attack.SQLMap;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.enums.VulnLevel;
import com.trackray.base.enums.VulnType;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.PageUtils;
import com.trackray.base.utils.SysLog;

import java.net.URL;
import java.util.Map;
import java.util.Set;

@Plugin(title = "基于爬虫的XSS & SQLI检测插件" , author = "blue")
public class SQLinjectAndXss extends CrawlerPlugin {
    @Override
    public boolean check() {
        if (crawlerPage.getRequest().getUrl().matches(".*\\?.*=.*"))
        {
            String url = crawlerPage.getRequest().getUrl();
            URL u = PageUtils.getURL(url);
            String urlstring = this.urltoString(u);
            for (String s : crawler.hasVul) {
                if (s.contains(urlstring)){
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public void process() {
        String url = crawlerPage.getRequest().getUrl();
        URL u = PageUtils.getURL(url);
        String urlstring = this.urltoString(u);

        /*if (hasVul.contains(urlstring)){
            return;
        }*/
        crawler.hasVul.add(url);

        SQLMap sqlMap = new SQLMap(url);//sql注入检测
        if (sqlMap.start()){
            log("start sql injection scan sqlmap taskid="+sqlMap.getTaskid());
            crawler.sqlmaps.add(sqlMap);
        }

        if (url.matches(Constant.Vuln.FILE_READ_VULN_REGEX)){
            Vulnerable build = Vulnerable.builder().vulName(VulnType.FILE_OPERATION.getName())
                    .affectsUrl(url).description("该链接可能会存在文件读取/包含等漏洞")
                    .level(VulnLevel.DANGER.getLevel()).vulType(VulnType.FILE_OPERATION.getType())
                    .build();
            addVul(build);
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
                    Vulnerable build = Vulnerable.builder().affectsDetail("通过爬虫检测出来该链接存在XSS漏洞 param=" + key)
                            .affectsUrl(path.toString())
                            .vulType(VulnType.XSS.getType())
                            .level(VulnLevel.DANGER.getLevel()).build();
                    addVul(build);
                    return;

                }
            }

        }

    }

    public void log(String log){
        SysLog.info(log);
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
}
