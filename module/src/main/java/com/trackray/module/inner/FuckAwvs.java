package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.Awvs;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/30 11:17
 */
@Plugin(title = "AWVS 扫描插件" ,author = "浅蓝")
public class FuckAwvs extends InnerPlugin{

    @Autowired
    private Awvs awvs;

    @Override
    public void process() {

        String target = task.getTargetStr();

        if (this.awvs!=null && awvs.ok){
            String targetId = this.awvs.createTarget(target);

            this.awvs.startScan(targetId);
            try {
                out:for (int i = 1; i <=5 ; i++) {
                    Thread.sleep(5000); //给awvs一点时间初始化任务

                    List<Awvs.Scan> scans = awvs.scans();

                    in:for (Awvs.Scan scan : scans) {
                        String tid = scan.getTargetId();
                        if(targetId.equals(tid)){
                            String scanId = scan.getScanId();

                            String scanSessionId = scan.getCurrentSession().getScanSessionId();

                            SysLog.info(String.format("Task=%s TargetID=%s scanID=%s sessionID=%s",task.getTaskMD5(),targetId,scanId,scanSessionId));


                            if(StringUtils.isAnyEmpty(scanId,scanSessionId)){
                                //如果scanid或sessionid有任意一个为空 ，则需要进行重试
                                SysLog.warn("第"+i+"次重试");

                                Thread.sleep(10000);

                                continue out;//跳过外层循环
                            }else{

                                SysLog.info("深度任务已初始化完成，即将开始深度扫描");

                                Map<String, Object> temp = new HashMap<>();
                                temp.put("target_id",targetId);
                                temp.put("scan_id",scanId);
                                temp.put("session_id",scanSessionId);

                                result = temp;

                                break out;//结束外层循环
                            }


                        }
                    }

                }
            } catch (InterruptedException e) {
                SysLog.error(e);
            }

        }

    }
}
