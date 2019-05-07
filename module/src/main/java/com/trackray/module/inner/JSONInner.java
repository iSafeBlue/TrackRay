package com.trackray.module.inner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.bean.Vulnerable;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.plugin.InnerPlugin;
import com.trackray.base.utils.SysLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/4/30 12:36
 */
@Plugin(value = "jSONInner" ,title = "JSON 内部调用漏洞插件" ,author = "浅蓝")
public class JSONInner extends InnerPlugin {
    @Autowired
    private JSONPlugin jsonPlugin;

    @Override
    public void process() {

        String target = this.task.getTargetStr();

        HashMap<String, String> map = jsonPlugin.fuckJsonList();
        SysLog.info("共有"+map.size()+"个JSON漏洞插件");

        for (String k : map.keySet()) {

            try {
                String code = FileUtils.readFileToString(new File(JSONPlugin.jsonPath.concat(k)) , "utf-8");

                JSONVul jsonVul = new ObjectMapper().readValue(code, JSONVul.class);

                boolean executeResult = jsonPlugin.execute(target, jsonVul);

                if (executeResult){
                    Request request = jsonVul.getRequest();
                    Meta meta = jsonVul.getMeta();

                    Vulnerable vulnerable = Vulnerable.builder()
                            .level(meta.getLevel())
                            .type(Vulnerable.Type.UNKNOWN.getType())
                            .payload(request.getPostData())
                            .address(target.concat(request.getPath()))
                            .title(meta.getName())
                            .detail(meta.getRemarks())
                            .vulnId(new ArrayList<>())
                            .references(new ArrayList<>())
                            .build();

                    if (StringUtils.isNotEmpty(meta.getReferences().getCve())){
                        vulnerable.getVulnId().add(meta.getReferences().getCve());
                    }

                    if (StringUtils.isNotEmpty(meta.getReferences().getUrl())){
                        vulnerable.getReferences().add(meta.getReferences().getUrl());
                    }

                    addVulnerable(vulnerable);

                }

            } catch (IOException e) {
                task.getExceptions().add(e);
                continue;
            }

        }

    }
}
