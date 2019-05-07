package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.PageUtils;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

@Plugin(title = "基于爬虫的 XSS 检测插件" , author = "浅蓝")
public class XssByCrawler extends CrawlerPlugin {
    @Override
    public boolean check() {
        if (target.toString().matches(".*\\?.*=.*"))
        {
            /*URL u = PageUtils.getURL(url);
            String urlstring = this.urltoString(u);
            for (String s : crawler.hasVul) {
                if (s.contains(urlstring)){
                    return false;
                }
            }*/


            return true;
        }
        return false;
    }

    @Override
    public void process() {
        String url = target.toString();
        URL u = target;
        String urlstring = this.urltoString(u);

        if (url.matches(Constant.Vuln.FILE_READ_VULN_REGEX)){
            Vulnerable build = Vulnerable.builder()
                    .title(Vulnerable.Type.FILE_OPERATION.getName())
                    .address(url)
                    .detail("该链接可能会存在文件读取/包含等漏洞")
                    .level(Vulnerable.Level.HIGH.getLevel())
                    .type(Vulnerable.Type.FILE_OPERATION.getType())
                    .build();
            addVulnerable(build);
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
                String content = "";
                try {
                    requests.url(path.toString());
                    content = requests.get().body();
                } catch (MalformedURLException e) {

                }

                if (StringUtils.contains(content,payload)){
                    //存在漏洞
                    log("xss vulnerable "+path.toString());
                    Vulnerable build = Vulnerable.builder()
                            .detail("通过爬虫检测出来该链接存在XSS漏洞 param=" + key)
                            .address(path.toString())
                            .type(Vulnerable.Type.XSS.getType())
                            .title("反射型XSS漏洞")
                            .level(Vulnerable.Level.HIGH.getLevel()).build();
                    addVulnerable(build);
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
