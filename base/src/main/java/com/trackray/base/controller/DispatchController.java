package com.trackray.base.controller;

import com.trackray.base.attack.FuzzDomain;
import com.trackray.base.attack.SQLMap;
import com.trackray.base.bean.Constant;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;
import com.trackray.base.exploit.AbstractExploit;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Controller("dispatchController")
public class DispatchController {

    @Autowired
    private DomainController domainController;
    @Autowired
    private IpController ipController;
    @Autowired
    private CrawlerController crawlerController;
    @Autowired
    private WebApplicationContext webApplicationConnect;
    public void init(Task task){


        String targetStr = task.getTargetStr();
        Task.Target target = new Task.Target();
        if(ReUtils.isIp(targetStr)){
            target.ip = targetStr;
            target.type = Constant.IP_TYPE;
        }else{
            target.url = targetStr;
            target.type = Constant.URL_TYPE;
        }
        task.setTarget(target);
        /*String md5URL = MD5.getMD5Code(task.getTargetStr());
        task.setTaskMD5(md5URL);*/
        SysLog.info("任务初始化 type="+target.type);
    }
    public ResultCode dispatch(Task task){
        if (task.getTarget().type == Constant.IP_TYPE){
            SysLog.info("目标是IP类型，准备扫描中...");
            return ipController.scan(task);
        }
        if (task.getTarget().type == Constant.URL_TYPE){
            SysLog.info("目标是域名类型，准备扫描中...");
            if (task.getRule().childdomain && task.getTarget().type == Constant.URL_TYPE){
                SysLog.info("正在扫描子域名...");
                FuzzDomain.fuzz(task);
            }
            return domainController.scan(task);
        }
        return ResultCode.ERROR;
    }


    public void crawler(Task task, String target , ThreadPoolExecutor exec) {
            CrawlerPage crawler = new CrawlerPage();
            crawler.setTask(task);
            //目前只对目标站进行爬虫
            crawler.getRequest().setUrl(target);
            crawler.setBase(target);

            Runnable runable = new Runnable() {
                @Override
                public void run() {
                    crawlerController.crawler(crawler);
                    Runnable sqlmap =(Runnable) crawler.getTempData().remove("sqlmap");

                    exec.execute(sqlmap);
                }
            };
            exec.execute(runable);


    }
    public WebApplicationContext getAppContext(){
        return webApplicationConnect;
    }
    public void attack(Task task, ExecutorService exec) {
        WebApplicationContext context = getAppContext();
        Map<String, AbstractExploit> beans = context.getBeansOfType(AbstractExploit.class);
        AbstractPlugin simpleVul = (AbstractPlugin) context.getBean("simpleVulRule");
        SysLog.info("开始漏洞检测");

        for (String targeturl : task.getTargets()) {

            simpleVul.setParam(new HashMap<String,Object>(){{put("target",targeturl);put("task",task);}});
            exec.submit(simpleVul);

            for (Map.Entry<String, AbstractExploit> entry : beans.entrySet()) {
                AbstractExploit exp = entry.getValue();
                exp.setTask(task);
                exp.setTarget(targeturl);
                String bean = entry.getKey();
                SysLog.info(bean+" exploit executeing...");
                exec.execute(exp);
            }
        }


    }
}
