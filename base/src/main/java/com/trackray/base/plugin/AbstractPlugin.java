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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 插件抽象类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class AbstractPlugin<E> implements Callable<AbstractPlugin<E>> {


    public final Logger log = LoggerFactory.getLogger(this.getClass());


    protected final static String BASE = Constant.RESOURCES_INCLUDE_PATH;   // resource外部资源文件的绝对路径

    protected E result;                     // 插件返回对象

    @Deprecated
    public Fetcher fetcher = new Fetcher(); // 执行请求类 已过时
    @Deprecated
    public CrawlerPage crawlerPage = new CrawlerPage(); //普通请求类 已过时

    protected HttpURLRequest requests = new HttpURLRequest();    //请求类

    public Map<String,Object> param = new HashMap<>();                            // 从前端传来的参数
    public String errorMsg = "未通过校验";                       // 错误响应信息

    @Autowired
    protected HackKit hackKit = new HackKit() ; //工具包

    public int step = 1;

    public abstract boolean check(Map<String,Object> param);    // 检测方法

    public void before(){}                                      // 执行插件主代码前执行

    public void after(){}           // 执行插件主代码后执行

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

    public AbstractPlugin<E> addParam(String key ,Object value){
        this.param.put(key , value);
        return this;
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
        String title = currentPlugin().title();
        try {
            flag = check(param); // 判断参数是否合法

            if (flag){  //合法则执行插件代码
                log.info(String.format("[%s] 该插件通过检测，正在执行。",title));
                before();
                result = start();
                after();
                log.info(String.format("[%s] 插件执行结束。",title));
            }
        }catch (Exception e){
            errorMsg = e.getMessage();
            log.error(String.format("[%s] 执行过程中发生异常。",title),e);
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
