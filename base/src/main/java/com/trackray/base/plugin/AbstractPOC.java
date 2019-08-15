package com.trackray.base.plugin;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.*;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.store.VulnDTO;
import com.trackray.base.store.VulnRepository;
import org.javaweb.core.net.HttpURLRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * poc抽象类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class AbstractPOC implements Runnable{
    @Deprecated
    protected static Fetcher fetcher = new Fetcher();
    @Deprecated
    protected CrawlerPage crawlerPage = new CrawlerPage();

    @Autowired
    private VulnRepository vulnRepository;

    protected HttpURLRequest requests = new HttpURLRequest();    //请求类

    private String target;

    private static final Logger log = LoggerFactory.getLogger(AbstractPOC.class);

    private Task task;

    public void before(){
        //...
    }

    public abstract void attack(Task task);

    public abstract boolean check(Result result);

    @Override

    public void run() {
        executor();
    }

    public void executor(){
        //PageUtils.copyTaskProxy(task,crawlerPage);
        if (check(task.getResult())){
            String title = this.currentPlugin().title();
            log.info(String.format("[%s] 漏洞扫描插件通过检测，正在执行。",title));
            before();

            attack(task);

            after();

            log.info(String.format("[%s] 漏洞扫描插件执行结束。",title));
        }
    }

    public void addVulnerable(Vulnerable vulnerable){
        VulnDTO vuln = new VulnDTO();
        vuln.setAddress(vulnerable.getAddress());
        vuln.setLevel(vulnerable.getLevel());
        //vuln.setMessage(vulnerable.);

        if (vulnerable.getReferences()!=null)
            vuln.setReference( vulnerable.getReferences().toArray(new String[]{}));
        if (vulnerable.getRisk()!=null)
            vuln.setRisk( vulnerable.getRisk().toArray(new String[]{}));
        if (vulnerable.getVulnId()!=null)
            vuln.setVulnId(vulnerable.getVulnId().toArray(new String[]{}));

        vuln.setPayload(vulnerable.getPayload());
        vuln.setRepair(vulnerable.getRepair());
        vuln.setTitle(vulnerable.getTitle());
        vuln.setTaskMd5(this.task.getTaskMD5());
        vuln.setType(vulnerable.getType());

        try {
            VulnDTO save = vulnRepository.save(vuln);
            if (save!=null)
                log.info(vuln.getTitle()+ " 漏洞已添加到数据库");
        }catch (Exception e){
            log.error(vuln.getTitle()+ "漏洞添加到数据库异常");
            this.task.getExceptions().add(e);
        }

    }

    private void after() {

    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
