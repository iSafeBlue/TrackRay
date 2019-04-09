package com.trackray.base.annotation;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public @interface Param {
    String key () default "";
    String defaultValue() default "";
    String desc() default "";

}
