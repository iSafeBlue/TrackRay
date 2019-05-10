package com.trackray.web.api;

import com.trackray.web.query.TaskQuery;
import com.trackray.web.query.VulnQuery;
import com.trackray.web.repository.TaskRepository;
import com.trackray.web.service.TaskService;
import com.trackray.base.bean.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/task")
@RestController
public class TaskApi {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;



    @RequestMapping(value = "process" , method = RequestMethod.POST )
    public ResultCode process(String task){
        taskService.processTask(task);
        return ResultCode.SUCCESS;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResultCode create(TaskQuery query , HttpSession session){
        ResultCode task = taskService.createTask(query , session);
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

    @RequestMapping(value = "destroy"  )
    public ResultCode destroy(String task , HttpSession session){
        boolean flag = taskService.taskDestroy(task , session);
        if (flag)
            return ResultCode.SUCCESS;
        else
            return ResultCode.ERROR;
    }

    @RequestMapping(value = "delete")
    public ResultCode destroy(String task ){
        boolean flag = taskService.taskDelete(task);
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
