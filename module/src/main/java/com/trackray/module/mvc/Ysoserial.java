package com.trackray.module.mvc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/14 12:00
 */
@Plugin(title = "ysoserial payload生成" , value = "ysoserial" ,author = "浅蓝")
@Rule
public class Ysoserial extends MVCPlugin {
    @Override
    public void index() {

        model.setViewName("index");
    }
}
