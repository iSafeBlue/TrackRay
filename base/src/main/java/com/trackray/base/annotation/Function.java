package com.trackray.base.annotation;

import java.lang.annotation.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Function {
    String value() default "";
    String desc() default "";
}
