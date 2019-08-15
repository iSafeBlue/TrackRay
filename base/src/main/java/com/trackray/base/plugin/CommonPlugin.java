package com.trackray.base.plugin;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 普通插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class CommonPlugin<E> extends AbstractPlugin<E> {

    public enum Type{
        /**
         * 非交互式插件响应类型
         */
        JSON("application/json"),
        XML("application/xml"),
        HTML("text/html"),
        TEXT("text/plain");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    public enum Charset{
        /**
         * 非交互式插件响应编码
         */
        UTF8("charset=utf-8"),
        GBK("charset=gbk"),
        GB2312("charset=gb2312"),
        NULL("");

        private String value;

        Charset(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public HttpServletRequest request;
    public HttpServletResponse response;

    public boolean isNone(String o){
        return StringUtils.isBlank(o);
    }

    public boolean isNull(Object o){
        return o==null;
    }

    public void write(String content){
        if (response!=null ){
            try {
                response.setContentType("text/pain;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.print(content);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

}
