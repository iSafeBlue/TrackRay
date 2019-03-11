package com.trackray.base.service.impl;

import com.trackray.base.bean.IPInfo;
import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;
import com.trackray.base.service.DomainService;
import com.trackray.base.service.IpService;
import com.trackray.base.service.SystemService;
import com.trackray.base.utils.ApiUtils;
import com.trackray.base.utils.DomainUtils;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Service("domainService")
public class DomainServiceImpl implements DomainService {

    @Autowired
    private SystemService systemService;
    @Autowired
    private IpService ipService;
    @Override
    public ResultCode scan(Task task) {
        if(this.hasCDN(task)){
            SysLog.info("该网站已识别到CDN");
        }else{
            String host = DomainUtils.getHost(task.getTargetStr());
            String domain = ReUtils.getDomain(task.getTargetStr());
            IPInfo ipinfo = DomainUtils.get(host);
            task.getResult().setIpInfo(ipinfo);//存储ipinfo
            if (null != ipinfo){
                if (task.getRule().sense) {
                    SysLog.info("正在查询WHOIS信息...");
                    String whois = ApiUtils.tooluWhois(host);
                    ipinfo.setWhois(whois);

                    SysLog.info("正在反查域名资产");
                    List<String> domains = ApiUtils.chinazReverse(domain);//反查域名资产

                    task.getResult().getAssets().setHoldSite(domains);//存储域名资产

                    SysLog.info("正在扫描备案者持有域名");
                    Map<String, String> icpMap = ApiUtils.aizhanIcp(domain);//备案者的其他域名资产


                    task.getResult().getAssets().setIcpSite(icpMap);//存储备案域名资产

                    SysLog.info("正在扫描同服网站");
                    List<String> sameSite = ApiUtils.chinazSame(ReUtils.isIp(ipinfo.getIp()) ? ipinfo.getIp() : host);//同服网站

                    task.getResult().getAssets().setServerSite(sameSite);//存储同服网站资产
                }
            }else{
                return ResultCode.getInstance(500,"目标URL域名不规范");
            }
        }
        ipService.scan(task);//当识别出IP后进行ip扫描

        return systemService.scan(task);
    }

    private boolean hasCDN(Task task) {
        return false;
    }
}
