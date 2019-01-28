package com.trackray.module.crawler;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.plugin.CrawlerPlugin;
import com.trackray.base.utils.ReUtils;
import com.trackray.base.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Plugin(title = "敏感信息探测",author = "blue")
public class InfoProbe extends CrawlerPlugin {
    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void process() {
        ResponseStatus status = crawlerPage.getResponse().getStatus();
        if (status.getContentTypeString().contains("htm"))
        {
            String content = status.getContent();
            if (StringUtils.isNotBlank(content))
            {
                String tel = ReUtils.getTel(content);
                String mail = ReUtils.getMail(content);
                if (null!=tel){
                    List<String> tel1 = crawlerPage.getTask().getResult()
                            .getSenseInfo().getTel();
                    if (!tel1.contains(tel))
                        tel1.add(tel);
                }

                if (null!=mail)
                {
                    List<String> email = crawlerPage.getTask().getResult().getSenseInfo().getEmail();
                    if (!email.contains(mail)){
                        email.add(mail);
                        if (mail.contains("@qq.com")){
                            crawlerPage.getTask().getResult().getSenseInfo().getNum().add(RegexUtil.extractStr(mail,"\\d*qq"));
                        }
                    }
                }
            }
        }
    }
}
