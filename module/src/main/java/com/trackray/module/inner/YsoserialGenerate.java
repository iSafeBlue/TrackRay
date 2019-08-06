package com.trackray.module.inner;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.handle.Shell;
import com.trackray.base.plugin.InnerPlugin;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/13 18:29
 */
@Plugin(title = "ysoserial payload 生成" , author = "浅蓝")
public class YsoserialGenerate extends InnerPlugin<File>{

    public static String PATH = BASE + "/ysoserial/ysoserial.jar";

    @Value("${temp.dir}")
    private String temp;

    private String payload ;

    private String command ;

    public YsoserialGenerate payload(String payload){
        this.payload = payload;
        return this;
    }

    public YsoserialGenerate command(String command){
        this.command = command;
        return this;
    }

    @Override
    public void process() {
        String uuid = UUID.randomUUID().toString();
        String tempname = temp.concat("ysoserial_"+uuid);

        try {
            Shell shell = shell().block(true);

            shell.exec(String.format("java -jar %s %s %s > %s",PATH,payload,command,tempname));

            String s = shell.readAll();


            result = new File(tempname);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }


    }
}
