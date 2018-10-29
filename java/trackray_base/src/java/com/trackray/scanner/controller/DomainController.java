package com.trackray.scanner.controller;

import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("domainController")
public class DomainController
{
    @Autowired
    private DomainService domainService;

    public ResultCode scan(Task task) {
        return this.domainService.scan(task);
    }
}
