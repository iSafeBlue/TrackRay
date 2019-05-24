package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/20 15:13
 */
@Plugin(title = "SQL报错信息插件" , author = "浅蓝")
public class SQLErrorCrawler  extends CrawlerPlugin{
    @Override
    public boolean check() {
        if (target.toString().matches(".*\\?.*=.*"))
        {
            return true;
        }
        return false;
    }

    public static String[] flags = {"Microsoft OLE","附近有语法错误","SqlException","your SQL syntax","SQLException","ORA-","语句的语法错误","后的引号不完整"};

    public static String[] payloads = {"'","\\","\""};

    @Override
    public void process() {

        String url = target.toString();
        URL u = target;
        String urlstring = this.urltoString(u);

        if (url.matches(Constant.Vuln.FILE_READ_VULN_REGEX)) {
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
        for (String payload : payloads) {
            for (int i = 0; i < keys.size(); i++) {
                String key = (String) karr[i];
                StringBuffer path = new StringBuffer(urlstring.concat("?"));
                for (String k : keys) {
                    String v = param.get(k);
                    if (k.equals(key)) {
                        path.append(k + ("="));
                        path.append(v + (payload));
                        path.append("&");
                    } else {
                        path.append(k + ("="));
                        path.append(v);
                        path.append("&");
                    }
                }

                path.delete(path.lastIndexOf("&"), path.length());
                String content = "";
                try {
                    requests.url(path.toString());
                    content = requests.get().body();

                    for (String flag : flags) {
                        if (StringUtils.contains(content,flag)){

                            addVulnerable(
                                    Vulnerable.builder()
                                            .type(Vulnerable.Type.SQL_INJECTION.getType())
                                            .level(Vulnerable.Level.MIDDLE.getLevel())
                                            .title("SQL 报错信息")
                                            .address(path.toString())
                                            .payload(path.toString())
                                            .build()
                            );

                        }
                    }


                } catch (MalformedURLException e) {

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

}
