package com.trackray.module.mvc;

import com.trackray.base.annotation.Function;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.bean.Constant;
import com.trackray.base.plugin.MVCPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/8/10 18:16
 */
@Plugin(value = "loggerApp", title = "系统日志" , author = "浅蓝" )
@Rule(auth = true)
public class LoggerApp extends MVCPlugin {

    @Value("${logging.file}")
    private String logfile;

    @Override
    public void index() {
        String logger = getLogger();
        model.addObject("log",logger);
        model.setViewName("index");
    }

    @Function
    public void clear(){
        if (StringUtils.isNotEmpty(logfile)) {
            File file = new File(logfile);
            try {
                FileUtils.writeStringToFile(file,"");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            write("删除成功");
        }
        write("删除失败");
    }

    private String getLogger(){

        String content = "";
        String userdir = Constant.USER_DIR;
        File file = null;
        if (StringUtils.isNotEmpty(logfile)){
            file = new File(logfile);
            if (!file.exists())
                file = new File(userdir,logfile);
            if (file.exists())
                try {
                    content = FileUtils.readFileToString(file);
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
        }
        return content;
    }

}
