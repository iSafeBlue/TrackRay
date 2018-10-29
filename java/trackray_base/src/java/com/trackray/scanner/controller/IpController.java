package com.trackray.scanner.controller;

import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;
import com.trackray.scanner.service.IpService;
import com.trackray.scanner.utils.ReUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("ipController")
public class IpController
{
    @Autowired
    private IpService ipService;

    public ResultCode scan(Task task) {
        String targetStr = task.getTargetStr();
        task.getResult().getIpInfo().setIp(ReUtils.getIp(targetStr));
        return ipService.scan(task);
    }

}
