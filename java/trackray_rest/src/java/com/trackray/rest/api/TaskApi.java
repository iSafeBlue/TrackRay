package com.trackray.rest.api;

import com.trackray.rest.query.Query;
import com.trackray.rest.query.TaskQuery;
import com.trackray.rest.query.VulnQuery;
import com.trackray.rest.service.TaskService;
import com.trackray.rest.service.VulnService;
import com.trackray.scanner.attack.FuzzDir;
import com.trackray.scanner.attack.FuzzDomain;
import com.trackray.scanner.bean.Constant;
import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.controller.DispatchController;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@RequestMapping("/task")
@RestController
public class TaskApi {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "start" , method = RequestMethod.POST )
    public ResultCode start(String task){
        ResultCode code = taskService.checkTask(task);
        if (code.getCode() == 200) {
            new Thread() {
                @Override
                public void run() {
                    boolean flag = taskService.startTask(task);
                }
            }.start();
        }
        return code;
    }
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResultCode create(TaskQuery query){
        ResultCode task = taskService.createTask(query);
        return task;
    }

    @RequestMapping(value = "progress"  , method = RequestMethod.POST)
    public ResultCode progress(String task){
        double taskRate = taskService.taskProgress(task);
        if (taskRate > 0)
            return ResultCode.getInstance(200,"成功",taskRate);
        else
            return ResultCode.ERROR;
    }

    @RequestMapping(value = "destroy"  , method = RequestMethod.POST)
    public ResultCode destroy(String task){
        boolean flag = taskService.taskDestroy(task);
        if (flag)
            return ResultCode.SUCCESS;
        else
            return ResultCode.ERROR;
    }

    @RequestMapping(value = "data" , method = RequestMethod.POST)
    public ResultCode data(String task){
        return taskService.getTaskData(task);
    }


    @RequestMapping(value = "vuln/put" )
    public ResultCode putVuln(@Valid VulnQuery query){
        ResultCode code = taskService.putVuln(query);
        return code;
    }

}
