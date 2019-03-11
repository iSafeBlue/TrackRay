package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Awvs;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Awvs内部扫描插件
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/25 15:14
 */
@Rule(enable = false)
@Plugin(value="awvsScan",title = "awvs扫描插件" , author = "blue" )
public class AwvsScan extends AbstractPlugin {
    private String target;
    private Task task;
    private String targetid;
    private String sessionid;
    private String scanId;

    private List<Awvs.Vulnerabilities> vulnQueue = new ArrayList<>();

    @Autowired
    private Awvs awvs;

    @Override
    public boolean check(Map param) {

        try {
            target = param.get("target").toString();
            task = (Task)param.get("task");
            Map<String, Object> temp = task.getResult().getItems().get(target).getTemp();
            if (temp.containsKey("target_id") && awvs.ok){
                targetid = temp.get("target_id").toString();
                sessionid = temp.get("session_id").toString();
                scanId = temp.get("scan_id").toString();
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;

    }

    @Override
    public Object start() {
        while (true) {
            if (this.monitor())
                break;

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                break;
            }

        }
        return null;
    }

    private boolean monitor() {
        Awvs.Scan scan = awvs.scan(scanId);
        if (scan!=null){

            List<Awvs.Vulnerabilities> vulns = awvs.vulns(sessionid, scanId);
            SysLog.info("深度扫描漏洞数 "+vulns.size());
            if (!vulns.isEmpty()){
                for (Awvs.Vulnerabilities vuln : vulns) {

                    if (!vulnQueue.contains(vuln)){
                        vulnQueue.add(vuln);

                        Awvs.Vulndetail vulndetail = awvs.vuln(scanId, sessionid, vuln.getVulnId());
                        vuln.setVuln(vulndetail);

                        Vulnerable vulnerable = Vulnerable.builder().affectsUrl(vulndetail.getAffectsUrl())
                                .level(vulndetail.getSeverity())
                                .vulName(vulndetail.getVtName())
                                .affectsDetail(vulndetail.getAffectsDetail())
                                .request(vulndetail.getRequest())
                                .build();

                        task.getResult().getItems().get(target).getVulns().add(vulnerable);


                    }

                }

            }


            if (StringUtils.equalsAny(scan.getCurrentSession().getStatus() , Awvs.State.COMPLETED , Awvs.State.ABORTED , Awvs.State.FAILED))
            {
                vulnQueue.clear();

                return true;
            }

        }

        return false;
    }
}
