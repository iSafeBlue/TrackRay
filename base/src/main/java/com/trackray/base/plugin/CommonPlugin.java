package com.trackray.base.plugin;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 普通插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class CommonPlugin<E> extends AbstractPlugin<E> {


    public HttpServletResponse response;

    public boolean isNone(String o){
        return StringUtils.isBlank(o);
    }

    public boolean isNull(Object o){
        return o==null;
    }

}
