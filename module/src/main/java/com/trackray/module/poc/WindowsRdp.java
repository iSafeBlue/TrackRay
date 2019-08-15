package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.SystemOS;
import com.trackray.base.plugin.AbstractPOC;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/16 17:49
 */
@Plugin(title = "Windows RDP 漏洞检测插件")
public class WindowsRdp extends AbstractPOC {
    @Override
    public void attack(Task task) {

        rdpDOSms12020();

    }

    /**
     * Windows XP Service Pack 3
     Windows XP Professional x64 Edition Service Pack 2
     Windows Server 2003 Service Pack 2
     Windows Server 2003 x64 Edition Service Pack 2
     Windows Server 2003 SP2（用于基于 Itanium 的系统）
     Windows Vista Service Pack 2
     Windows Vista x64 Edition Service Pack 2
     Windows Server 2008（用于 32 位系统）Service Pack 2*
     Windows Server 2008（用于基于 x64 的系统）Service Pack 2*
     Windows Server 2008（用于基于 Itanium 的系统）Service Pack 2
     Windows 7（用于 32 位系统）和 Windows 7（用于 32 位系统）Service Pack 1
     Windows 7（用于基于 x64 的系统）和 Windows 7（用于基于 x64 的系统）Service Pack 1
     Windows Server 2008 R2（用于基于 x64 的系统）和 Windows Server 2008 R2（用于基于 x64 的系统）Service Pack 1*
     Windows Server 2008 R2（用于基于 Itanium 的系统）和 Windows Server 2008 R2（用于基于 Itanium 的系统）Service Pack 1
     */
    private void rdpDOSms12020() {
        String realIP = getTask().getResult().getHostInfo().getRealIP();
        SystemEntity systemOS = getTask().getResult().getSystemInfo().getSystemOS();
        if (systemOS!=null && systemOS.getOs() == SystemOS.WINDOWS){
            String version = systemOS.getVersion();
            if (StringUtils.containsAny(version,
                    "Windows XP",
                    "Windows Server 2003",
                    "Windows Vista",
                    "Windows Server 2008",
                    "Windows 7"))
            {
                Vulnerable vulnerable = Vulnerable.builder()
                        .level(Vulnerable.Level.MIDDLE.getLevel())
                        .level(Vulnerable.Type.CODE_EXECUTION.getType())
                        .title("该端口可能存在 MS12-020 拒绝服务漏洞")
                        .references(Arrays.asList("https://docs.microsoft.com/zh-cn/security-updates/Securitybulletins/2012/ms12-020"))
                        .address(realIP + ":" + port).build();
                this.addVulnerable(vulnerable);



            }

        }


    }

    private int port = 3389;
    @Override
    public boolean check(Result result) {
        for (PortEntity entity : result.getSystemInfo().getPorts()) {
            if (entity.getPort() == 3389 || entity.getService().contains("ms-wbt-server")){
                port = entity.getPort();
                return true;
            }
        }
        return false;
    }
}
