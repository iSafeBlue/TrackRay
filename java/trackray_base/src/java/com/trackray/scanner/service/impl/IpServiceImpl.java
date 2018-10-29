package com.trackray.scanner.service.impl;

import com.trackray.scanner.bean.*;
import com.trackray.scanner.httpclient.CrawlerPage;
import com.trackray.scanner.httpclient.Fetcher;
import com.trackray.scanner.httpclient.ResponseStatus;
import com.trackray.scanner.service.IpService;
import com.trackray.scanner.service.SystemService;
import com.trackray.scanner.utils.ApiUtils;
import com.trackray.scanner.utils.DomainUtils;
import com.trackray.scanner.utils.ReUtils;
import com.trackray.scanner.utils.SysLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.util.Base64;
import java.util.List;

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
