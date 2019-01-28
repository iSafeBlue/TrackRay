package com.trackray.web.service;

import com.trackray.web.query.TaskQuery;
import com.trackray.web.query.VulnQuery;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;

public interface TaskService {
    ResultCode createTask(TaskQuery query);

    boolean startTask(String query);

    double taskProgress(String md5);

    boolean taskDestroy(String task);

    ResultCode getTaskData(String task);

    ResultCode checkTask(String task);

    ResultCode putVuln(VulnQuery query);

    int saveData(Task task, int status);

}
