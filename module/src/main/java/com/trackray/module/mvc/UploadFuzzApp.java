package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.MVCPlugin;
import com.trackray.module.inner.UploadFuzzInner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/12 15:20
 */
@Plugin(title = "上传漏洞Fuzz" ,value = "uploadFuzz" , author = "浅蓝")
@Rule()
public class UploadFuzzApp extends MVCPlugin {

    @Autowired
    private UploadFuzzInner uploadFuzzInner;

    @Override
    public void index() {
        model.addObject("language_choices",UploadFuzzInner.language_choices);
        model.addObject("middleware_choices",UploadFuzzInner.middleware_choices);
        model.addObject("os_choices",UploadFuzzInner.os_choices);
        model.setViewName("index");
    }

    @Function
    public void download(){
        uploadFuzzInner.setLanguage(param.getOrDefault("language","all").toString());
        uploadFuzzInner.setOs(param.getOrDefault("os","all").toString());
        uploadFuzzInner.setMiddleware(param.getOrDefault("middleware","all").toString());
        uploadFuzzInner.setAllow_suffix(param.getOrDefault("allow_suffix","jpg").toString());
        uploadFuzzInner.setUpload_file_name(param.getOrDefault("upload_file_name","1.jpg").toString());
        uploadFuzzInner.setDouble_suffix(param.getOrDefault("double_suffix","false").toString().equals("true")?true:false);
        AbstractPlugin<Collection<String>> executor = uploadFuzzInner.executor();
        StringBuffer buffer = new StringBuffer();
        Collection<String> result = executor.result();
        for (String s : result) {
            buffer.append(s+"\r\n");
        }
        response.setHeader("Content-Disposition","attachment;filename=fuzz.txt");
        write(buffer.toString());
    }

}
