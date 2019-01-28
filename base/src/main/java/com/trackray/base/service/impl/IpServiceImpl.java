package com.trackray.base.service.impl;

import com.trackray.base.bean.*;
import com.trackray.base.service.IpService;
import com.trackray.base.service.SystemService;
import com.trackray.base.utils.ApiUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.SysLog;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Service("ipService")
public class IpServiceImpl implements IpService {

    @Autowired
    private SystemService systemService;

    @Override
    public ResultCode scan(Task task) {
        String ip = task.getResult().getIpInfo().getIp();
        if (ip!=null && ReUtils.isIp(ip)){
            String[] iparr = ip.split("\\.");
            String formatIP = String.format("%s.%s.%s.%s", iparr[0], iparr[1], iparr[2], "0/24");
            SysLog.info("目标是IP地址，正在调用CENSYS接口扫描C段");
            if (task.getRule().sense)
                this.censys(formatIP , task);
        }else{
            return ResultCode.getInstance(500,"目标地址非正常IP");
        }
        if (task.getTarget().type == Constant.URL_TYPE)
        {
            return ResultCode.SUCCESS;
        }
        return systemService.scan(task);
    }

    /**
     * @author b1u3r
     * @param formatIP
     * @param task
     * @description 调用censys接口查询C段服务
     */
    private void censys(String formatIP, Task task) {
        List<JSONObject> censys = ApiUtils.censys(formatIP);
        task.getResult().getIpInfo().setcList(censys);
    }



}
