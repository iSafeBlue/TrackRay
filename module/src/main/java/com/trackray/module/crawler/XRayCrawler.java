package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.CheckUtils;
import com.trackray.module.inner.XRayInner;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/15 17:51
 */
@Plugin(title = "基于爬虫的XRay扫描插件" , author = "浅蓝")
public class XRayCrawler extends CrawlerPlugin {
    @Override
    public boolean check() {
        String query = this.target.getQuery();
        return StringUtils.isNotEmpty(query);
    }
    @Autowired
    private XRayInner xRayInner;
    @Override
    public void process() {

        XRayInner xray = xRayInner.executor().result();
        xray.setUrl(this.target.toString());
        xray.outputJSON(UUID.randomUUID().toString());
        xray.setPlugins("crlf_injection","redirect","ssrf","jsonp","brute_force","upload","xxe","path_traversal","dirscan","cmd_injection","sqldet");
        File file = xray.run(true).outputFile();
        if (file.exists()){
            try {
                String result = FileUtils.readFileToString(file, "UTF-8");
                if (CheckUtils.isJson(result)){
                    JSONArray arr = JSONArray.fromObject(result);
                    for (int i = 0; i <arr.size() ; i++) {
                        JSONObject vul = arr.getJSONObject(i);
                        //long create_time = vul.getLong("create_time");
                        String vuln_class = vul.getString("vuln_class");
                        JSONObject detail = vul.getJSONObject("detail");
                        String url = detail.getString("url");
                        String request = detail.getString("request");
                        String response = detail.getString("response");
                        String payload = detail.getString("payload");
                        if (response.length()>65535)
                            response = "响应数据超长";
                        Vulnerable.VulnerableBuilder builder = Vulnerable.builder();
                        builder.level(Vulnerable.Level.HIGH.getLevel());
                        builder.payload(request);
                        builder.address(url);
                        builder.detail(response);
                        builder.title(vuln_class);
                        Vulnerable build = builder.build();
                        this.addVulnerable(build);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }finally {
                file.delete();
            }
        }
    }
}
