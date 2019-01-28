package com.trackray.base.plugin;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.handle.Shell;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 插件抽象类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class AbstractPlugin<E> implements Callable<AbstractPlugin<E>> {

    public enum Type{
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
    protected final static String BASE = Constant.RESOURCES_PATH;

    private Shell shell = new Shell();
    protected E result;
    public Fetcher fetcher = new Fetcher();
    public CrawlerPage crawlerPage = new CrawlerPage();
    public Map<String,Object> param;
    public String errorMsg = "未通过校验";

    public int step = 1;

    public abstract boolean check(Map<String,Object> param);

    public void before(){}

    public Object after(Object... args){return null;}

    public abstract E start();

    @Override
    public AbstractPlugin<E> call() {
        return executor();
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public E result() {
        return result;
    }

    public AbstractPlugin<E> executor() {
        boolean flag = true;
        try {
            flag = check(param);
        }catch (Exception e){
            flag = false;
        }
        if (flag){
            before();
            result = start();
            after();
        }
        return this;
    }

    private final void exec(){}
    public final Shell shell(){
        return shell;
    }

    public Rule currentRule(){
        Rule rule = this.getClass().getAnnotation(Rule.class);
        return rule;
    }
    public Param[] currentParams(){
        return this.currentRule().params();
    }

    public Plugin currentPlugin(){
        Plugin plugin = this.getClass().getAnnotation(Plugin.class);
        return plugin;
    }

}
