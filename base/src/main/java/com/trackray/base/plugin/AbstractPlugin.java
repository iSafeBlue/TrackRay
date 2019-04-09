package com.trackray.base.plugin;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.HackKit;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import org.javaweb.core.net.HttpURLRequest;
import org.springframework.beans.factory.annotation.Autowired;

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

    protected final static String BASE = Constant.RESOURCES_PATH;   // resource外部资源文件的绝对路径

    protected E result;                     // 插件返回对象

    @Deprecated
    public Fetcher fetcher = new Fetcher(); // 执行请求类 已过时
    @Deprecated
    public CrawlerPage crawlerPage = new CrawlerPage(); //普通请求类 已过时

    protected HttpURLRequest requests = new HttpURLRequest();    //请求类

    public Map<String,Object> param;                            // 从前端传来的参数
    public String errorMsg = "未通过校验";                       // 错误响应信息

    @Autowired
    private HackKit hackKit ; //工具包

    public int step = 1;

    public abstract boolean check(Map<String,Object> param);    // 检测方法

    public void before(){}                                      // 执行插件主代码前执行

    public Object after(Object... args){return null;}           // 执行插件主代码后执行

    public abstract E start();                                  // 插件代码实现方法

    @Override
    public AbstractPlugin<E> call() {
        return executor();
    }      // callable

    public void setParam(Map param) {
        this.param = param;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public E result() {
        return result;
    }

    /**
     * 执行方法
     * @return
     */
    public AbstractPlugin<E> executor() {
        boolean flag = true;
        try {
            flag = check(param); // 判断参数是否合法
        }catch (Exception e){
            flag = false;
        }
        if (flag){  //合法则执行插件代码
            before();
            result = start();
            after();
        }
        return this;
    }

    //private final void exec(){}

    /**
     * 系统shell对象
     * @return
     */
    public final Shell shell(){
        return new Shell();
    }

    /**
     * 获取当前规则注解
     * @return
     */
    public Rule currentRule(){
        Rule rule = this.getClass().getAnnotation(Rule.class);
        return rule;
    }

    /**
     * 获取当前插件参数注解
     * @return
     */
    public Param[] currentParams(){
        return this.currentRule().params();
    }


    /**
     * 获取当前插件注解
     * @return
     */
    public Plugin currentPlugin(){
        Plugin plugin = this.getClass().getAnnotation(Plugin.class);
        return plugin;
    }

}
