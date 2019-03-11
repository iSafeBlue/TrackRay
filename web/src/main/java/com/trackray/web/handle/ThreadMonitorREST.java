package com.trackray.web.handle;

import com.trackray.web.service.TaskService;
import com.trackray.base.bean.Task;
import com.trackray.base.handle.ThreadMonitor;
import com.trackray.base.utils.SysLog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 任务监控
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class ThreadMonitorREST extends ThreadMonitor {

    @Autowired
    private TaskService taskService;

    @Override
    public void save(Task task , int status) {
        if (taskService.saveData(task,status)!=1)
            SysLog.error("存储数据失败");
    }
}
