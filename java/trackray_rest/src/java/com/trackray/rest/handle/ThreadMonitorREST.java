package com.trackray.rest.handle;

import com.trackray.rest.service.TaskService;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.handle.ThreadMonitor;
import com.trackray.scanner.utils.SysLog;
import org.springframework.beans.factory.annotation.Autowired;

public class ThreadMonitorREST extends ThreadMonitor {

    @Autowired
    private TaskService taskService;

    @Override
    public void save(Task task , int status) {
        if (taskService.saveData(task,status)!=1)
            SysLog.dbWarn("存储数据失败");
    }
}
