package com.trackray.scanner.controller;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.attack.FuzzDomain;
import com.trackray.scanner.attack.SQLMap;
import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.exploit.AbstractExploit;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.HttpClient;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.utils.MD5;
import com.trackray.scanner.utils.ReUtils;
import com.trackray.scanner.utils.SysLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.concurrent.*;

@Controller("dispatchController")
public class DispatchController {

    @Autowired
    private DomainController domainController;
    @Autowired
    private IpController ipController;
    @Autowired
    private CrawlerController crawlerController;

    public void init(Task task){
        {
            if (new HttpClient().getContent(SQLMap.API).equals("error"))
            {
                SysLog.info("SQLMAP服务未启动");
            }
        }

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
            return ipController.scan(task);
        }
        if (task.getTarget().type == Constant.URL_TYPE){
            if (task.getRule().childdomain && task.getTarget().type == Constant.URL_TYPE){
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

    public void attack(Task task, ExecutorService exec) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        Map<String, AbstractExploit> beans = context.getBeansOfType(AbstractExploit.class);
        for (Map.Entry<String, AbstractExploit> entry : beans.entrySet()) {
            AbstractExploit exp = null;
            try {
                exp = entry.getValue().getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            exp.setTask(task);
            String bean = entry.getKey();
            SysLog.info(bean+" start executeing...");
            exec.execute(exp);
        }

    }
}
