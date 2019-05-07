package com.trackray.module.plugin.ios;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/2 17:34
 */
@Rule
@Plugin(value = "mvcTest",
        title = "mvc类插件测试",
        author = "浅蓝")
public class mvcTest extends MVCPlugin{

    @Function("test.html")
    public void test(){
        model.addObject("msg","这是一个测试插件");
        model.setViewName("test");
    }


    @Override
    public void index() {
        model.addObject("msg","index page");
        model.setViewName("index");
    }
}
