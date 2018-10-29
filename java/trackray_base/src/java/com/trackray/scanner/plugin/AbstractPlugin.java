package com.trackray.scanner.plugin;

import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.handle.Shell;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
    public Map<String,String> param;

    public int step = 1;

    public abstract boolean check(Map<String,String> param);

    public void before(){}

    public Object after(Object... args){return null;}

    public abstract E start();

    @Override
    public AbstractPlugin<E> call() {
        return executor();
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public E result() {
        return result;
    }

    public AbstractPlugin<E> executor() {
        if (check(param)){
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

}
