package com.trackray.base.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope("prototype")
/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public @interface Plugin {
    String value() default "";  //插件在springioc容器的ID
    String title(); //插件标题
    String desc() default "";   //插件介绍
    String author() default "anonymous";    //插件作者
    String[] link() default ""; //插件相关连接
    long time() default 0L; //插件对应的时间戳
}
