package com.trackray.scanner.annotation;


import com.trackray.scanner.plugin.AbstractPlugin;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rule {
    boolean enable() default true;
    boolean websocket() default false;
    boolean sync() default false;
    String[] param() default {};
    AbstractPlugin.Type type() default AbstractPlugin.Type.JSON;
    AbstractPlugin.Charset charset() default AbstractPlugin.Charset.UTF8;
    String[] headers() default {};
    String filename() default "";
}
