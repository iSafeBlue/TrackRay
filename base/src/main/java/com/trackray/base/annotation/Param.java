package com.trackray.base.annotation;

public @interface Param {
    String key () default "";
    String defaultValue() default "";
    String desc() default "";

}
