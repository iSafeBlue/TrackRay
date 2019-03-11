package com.trackray.base.controller;

import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;
import com.trackray.base.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Controller("domainController")
public class DomainController
{
    @Autowired
    private DomainService domainService;

    public ResultCode scan(Task task) {
        return this.domainService.scan(task);
    }
}
