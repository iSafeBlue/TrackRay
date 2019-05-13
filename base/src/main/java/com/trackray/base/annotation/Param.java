package com.trackray.base.annotation;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public @interface Param {
    String key () default "";           // 参数键
    String defaultValue() default "";   // 参数默认值
    String desc() default "";           // 参数说明

}
