package com.trackray.module.poc;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.HostInfo;
import com.trackray.base.bean.Result;
import com.trackray.base.bean.Task;
import com.trackray.base.plugin.AbstractPOC;
import net.dongliu.requests.Requests;

import java.util.HashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/13 15:40
 */
@Plugin(title = "fuckTheWebsiteEverything" , author = "浅蓝")
public class FuckTheWebsiteEverything extends AbstractPOC {
    @Override
    public void attack(Task task) {

        HostInfo hostInfo = task.getResult().getHostInfo();
        for (String dns : hostInfo.getDns()) {

            dns = dns.toUpperCase();

            if (dns.contains("CLOUDFLARE"))
                hostInfo.setCdn(true);


        }

        for (String ip : hostInfo.getOtherIP()) {
            try {
                String text = Requests.get("http://" + ip).send().readToText();
                if (text.contains("Cloudflare")){
                    hostInfo.setCdn(true);
                }

                if (text.contains(task.getResult().getAdditional().get("网站标题").toString())){
                    task.getResult().getAdditional().put("通过多地PING的IP提取出疑似真实IP",ip);
                }
            }catch (Exception e){
                continue;
            }

        }

        if (task.getResult().getAdditional().containsKey("域名历史解析记录")){
            HashMap<String,String> map = (HashMap<String, String>) task.getResult().getAdditional().get("域名历史解析记录");
            for (String ip : map.keySet()) {
                try {
                    String text = Requests.get("http://" + ip).send().readToText();
                    if (text.contains(task.getResult().getAdditional().get("网站标题").toString())){
                        task.getResult().getAdditional().put("通过历史解析记录提取出疑似真实IP",ip);
                    }
                }catch (Exception e){
                    continue;
                }
            }

        }


    }




    @Override
    public boolean check(Result result) {
        return true;
    }
}
