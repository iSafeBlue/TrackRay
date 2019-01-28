package com.trackray.base.controller;

import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;
import com.trackray.base.service.IpService;
import com.trackray.base.utils.ReUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
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
