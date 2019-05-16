package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.MVCPlugin;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/15 10:26
 */
@Plugin(title = "SQLMAP 手册" , value = "sqlmaper", author = "浅蓝")
@Rule
public class SqlMapHelper extends MVCPlugin {
    @Override
    public void index() {
        model.setViewName("index");
    }

    @Function
    public void help1(){
        model.setViewName("help1");
    }

    @Function
    public void help2(){
        model.setViewName("help2");
    }
}
