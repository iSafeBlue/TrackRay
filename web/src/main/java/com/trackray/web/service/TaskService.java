package com.trackray.web.service;

import com.trackray.web.query.TaskQuery;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;

import javax.servlet.http.HttpSession;

public interface TaskService {
    ResultCode createTask(TaskQuery query , HttpSession session);

    boolean startTask(String query);

    double taskProgress(String md5);

    boolean taskDestroy(String task, HttpSession session);

    ResultCode getTaskData(String task);

    ResultCode checkTask(String task);

    int saveData(Task task, int status);

    void processTask(String task);

    boolean taskDelete(String task);
}
