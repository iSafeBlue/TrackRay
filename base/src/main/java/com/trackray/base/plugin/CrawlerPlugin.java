package com.trackray.base.plugin;

import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.httpclient.CrawlerPage;
import com.trackray.base.httpclient.Fetcher;
import com.trackray.base.store.VulnDTO;
import com.trackray.base.store.VulnRepository;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

/**
 * 爬虫插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public abstract class CrawlerPlugin implements Runnable {

    public CrawlerPage crawlerPage;
    public Fetcher fetcher = new Fetcher();
    public HttpURLRequest requests = new HttpURLRequest();
    public HttpResponse response;
    public URL target;
    protected Task task;
    public abstract boolean check();
    public abstract void process();

    public static final Logger log = LoggerFactory.getLogger(CrawlerPlugin.class);

    @Autowired
    private VulnRepository vulnRepository;

    public void executor(){
        if (check()){
            process();
        }
    }

    @Override
    public void run() {
        executor();
    }
    public void addVulnerable(Vulnerable vulnerable){
        VulnDTO vuln = new VulnDTO();
        vuln.setAddress(vulnerable.getAddress());
        vuln.setLevel(vulnerable.getLevel());
        if (StringUtils.isBlank(vulnerable.getPayload())){
            vuln.setPayload(vulnerable.getAddress());
        }
        vuln.setMessage(vulnerable.getDetail());
        vuln.setPayload(vulnerable.getPayload());
        if (vulnerable.getReferences()!=null)
            vuln.setReference((String[]) vulnerable.getReferences().toArray());
        if (vulnerable.getRisk()!=null)
            vuln.setRisk((String[]) vulnerable.getRisk().toArray());
        if (vulnerable.getVulnId()!=null)
            vuln.setVulnId((String[]) vulnerable.getVulnId().toArray());
        vuln.setRepair(vulnerable.getRepair());
        vuln.setTitle(vulnerable.getTitle());
        vuln.setTaskMd5(this.task.getTaskMD5());
        vuln.setType(vulnerable.getType());
        try {
            VulnDTO save = vulnRepository.save(vuln);
            if (save!=null)
                log.info("漏洞已添加到数据库");
        }catch (Exception e){
            log.error("漏洞添加到数据库异常 data:"+vuln.toString());
            this.task.getExceptions().add(e);
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
