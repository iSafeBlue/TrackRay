package com.trackray.base.annotation;

public @interface Option {

    String name();
    String formName();
    String description() default "";
    String defaultValue() ;


}
