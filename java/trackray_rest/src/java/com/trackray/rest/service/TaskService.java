package com.trackray.rest.service;

import com.trackray.rest.query.TaskQuery;
import com.trackray.rest.query.VulnQuery;
import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;

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
