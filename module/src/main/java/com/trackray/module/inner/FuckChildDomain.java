package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.attack.Payload;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.SysLog;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 子域名扫描插件类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/30 10:21
 */
@Plugin(title = "子域名扫描内部插件" , author = "blue")
public class FuckChildDomain extends InnerPlugin {

    @Override
    public void process() {

        String domain = task.getResult().getHostInfo().getRootDomain();

        for (String prefix : Payload.domainPayload) {
            if (StringUtils.isNotBlank(prefix)) {
                try {
                    if (!fuckDomain(domain,prefix))
                        break;
                }catch (Exception e){
                    continue;
                }
            }
        }

    }

    private boolean fuckDomain(String root, String prefix) {
        if (task.getResult().getAssets().getChildDomain().size() >= 30){
            return false;
        }
        String domain = String.format("%s.%s", prefix, root);
        String ip =null;
        try {
            ip = InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException e) {
            return true;
        }
        if (ReUtils.isIp(ip) && !"127.0.0.1".equals(ip)){
            SysLog.info("扫描到子域名 "+domain);
            task.getResult().getAssets().getChildDomain().put(domain,ip);
            return true;
        }
        return false;
    }
}
