package com.trackray.base.controller;

import com.trackray.base.bean.Task;
import com.trackray.base.plugin.AbstractPOC;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.utils.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Controller("dispatchController")
public class DispatchController {

    @Autowired
    private WebApplicationContext webApplicationConnect;

    public WebApplicationContext getAppContext(){
        return webApplicationConnect;
    }
    @Deprecated
    public void attack(Task task, ExecutorService exec) {
        WebApplicationContext context = getAppContext();
        Map<String, AbstractPOC> beans = context.getBeansOfType(AbstractPOC.class);
        AbstractPlugin simpleVul = (AbstractPlugin) context.getBean("simpleVulRule");
        SysLog.info("开始漏洞检测");

        for (String targeturl : task.getTargets()) {

            simpleVul.setParam(new HashMap<String,Object>(){{put("target",targeturl);put("task",task);}});
            exec.submit(simpleVul);

            for (Map.Entry<String, AbstractPOC> entry : beans.entrySet()) {
                AbstractPOC exp = entry.getValue();
                exp.setTask(task);
                exp.setTarget(targeturl);
                String bean = entry.getKey();
                SysLog.info(bean+" exploit executeing...");
                exec.execute(exp);
            }
        }


    }
}
