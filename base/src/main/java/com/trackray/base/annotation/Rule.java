package com.trackray.base.annotation;


import com.trackray.base.plugin.AbstractPlugin;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public @interface Rule {
    boolean enable() default true;  //是否在外部启用插件
    boolean websocket() default false;  //是否为websocket插件
    boolean sync() default false;   //是否为异步任务
    Param[] params() default {}; // 插件
    //String[] param() default {};    //插件使用所需参数 如{"url","port"}
    //String[] defParam() default {}; //插件默认参数需与param下标一一对应 如{"http://localhost","3306"}
    //String[] descParam() default {};//插件参数对应的介绍 需与param下标对应 如{"目标地址","使用端口"}
    AbstractPlugin.Type type() default AbstractPlugin.Type.JSON;    //当使用commonplugin时返回给浏览器的文本类型
    AbstractPlugin.Charset charset() default AbstractPlugin.Charset.UTF8;   //使用commonplugin时返回给浏览器的文本编码
    String[] headers() default {};  //返回给浏览器时的response header
    String filename() default "";   //插件用于下载功能时返回的文件名
}
