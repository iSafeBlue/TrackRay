package com.trackray.base.plugin;

import com.trackray.base.annotation.Function;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.store.VulnDTO;
import com.trackray.base.store.VulnRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 内部插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/2 16:33
 */
public abstract class InnerPlugin<E> extends AbstractPlugin<E>{

    public static final Logger log = LoggerFactory.getLogger(InnerPlugin.class);

    protected Task task;

    @Autowired
    private VulnRepository vulnRepository;

    public void addVulnerable(Vulnerable vulnerable){
        VulnDTO vuln = new VulnDTO();
        vuln.setAddress(vulnerable.getAddress());
        vuln.setLevel(vulnerable.getLevel());
        //vuln.setMessage(vulnerable.);
        vuln.setPayload(vulnerable.getPayload());
        if (vulnerable.getReferences()!=null)
            vuln.setReference(vulnerable.getReferences().toArray(new String[]{}));
        if (vulnerable.getRisk()!=null)
            vuln.setRisk( vulnerable.getRisk().toArray(new String[]{}));
        if (vulnerable.getVulnId()!=null)
            vuln.setVulnId(vulnerable.getVulnId().toArray(new String[]{}));
        vuln.setRepair(vulnerable.getRepair());
        vuln.setTitle(vulnerable.getTitle());
        vuln.setTaskMd5(this.task.getTaskMD5());
        vuln.setType(vulnerable.getType());
        try {
            VulnDTO save = vulnRepository.save(vuln);
            if (save!=null)
                log.info(vuln.getTitle()+ " 漏洞已添加到数据库");
        }catch (Exception e){
            log.error(vuln.getTitle()+"漏洞添加到数据库异常");
            this.task.getExceptions().add(e);
            vuln.setPayload("");
            VulnDTO save = vulnRepository.save(vuln);
            if (save!=null)
                log.info(vuln.getTitle()+"漏洞已添加到数据库");


        }
    }


    public InnerPlugin<E> setTask(Task task) {
        this.task = task;
        return this;
    }

    public abstract void process();

    @Override
    public E start() {
        return null;
    }

    @Override
    public boolean check(Map<String, Object> param) {
        return true;
    }

    @Override
    public AbstractPlugin<E> executor() {
        try {
            before();

            if (check(param))
                process();

            after();
        }catch (Exception e){
            if (task!=null)
                this.task.getExceptions().add(e);
        }

        return this;
    }
}
