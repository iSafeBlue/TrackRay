package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Awvs;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
@Plugin(value="awvsScan",title = "awvs扫描插件" , author = "浅蓝" )
public class AwvsScan extends InnerPlugin {


    private String target;
    private String targetid;
    private String sessionid;
    private String scanId;


    private List<Awvs.Vulnerabilities> vulnQueue = new ArrayList<>();

    @Autowired
    private Awvs awvs;

    @Override
    public boolean check(Map param) {
        try {
            target = task.getTargetStr();
            Map<String, Object> temp = (Map<String, Object>) param.get("map");
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

                        int type = Vulnerable.Type.UNKNOWN.getType();
                        String vname = vulndetail.getVtName().toLowerCase();
                        if (vname.contains("xss") || vname.contains("Cross site scripting")){
                            type = Vulnerable.Type.XSS.getType();
                        }else if(vname.contains("csrf")){
                            type = Vulnerable.Type.CSRF.getType();
                        }else if(vname.contains("overflow")){
                            type = Vulnerable.Type.OVERFLOW.getType();
                        }else if (vname.contains("SQL")){
                            type = Vulnerable.Type.SQL_INJECTION.getType();
                        }

                        Vulnerable vulnerable = Vulnerable.builder()
                                .address(vulndetail.getAffectsUrl())
                                .level(vulndetail.getSeverity())
                                .title(vulndetail.getVtName())
                                .detail(vulndetail.getAffectsDetail())
                                .type(type)
                                .payload(vulndetail.getRequest())
                                .build();

                        //task.getResult().getItems().get(target).getVulns().add(vulnerable);
                        addVulnerable(vulnerable);

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

    @Override
    public void process() {

        while (true) {
            if (this.monitor())
                break;

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                break;
            }

        }

    }
}
