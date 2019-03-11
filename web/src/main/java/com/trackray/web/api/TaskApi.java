package com.trackray.web.api;

import com.trackray.web.query.TaskQuery;
import com.trackray.web.query.VulnQuery;
import com.trackray.web.repository.TaskRepository;
import com.trackray.web.service.TaskService;
import com.trackray.base.bean.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/task")
@RestController
public class TaskApi {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

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


    /*@RequestMapping(value = "vuln/put" )
    public ResultCode putVuln(@Valid VulnQuery query){
        ResultCode code = taskService.putVuln(query);
        return code;
    }*/

    @RequestMapping(value = "/find")
    public ResultCode findAll(){
        return ResultCode.SUCCESS(taskRepository.findAll());
    }

}
