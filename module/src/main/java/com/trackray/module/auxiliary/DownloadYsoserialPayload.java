package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.base.utils.SysLog;
import com.trackray.module.inner.YsoserialGenerate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/13 21:49
 */
@Rule(params = {
        @Param(key = "command" , desc = "载荷命令" ,defaultValue = "calc"),
        @Param(key = "payload" , desc = "攻击载荷" , defaultValue = "CommonsCollections4")
} , filename = "payload.ser" , charset = CommonPlugin.Charset.UTF8, auth = true)
@Plugin(title = "ysoserial payload在线生成接口" ,author = "浅蓝")
public class DownloadYsoserialPayload extends CommonPlugin<Object> {
    @Autowired
    private YsoserialGenerate ysoserialGenerate;
    private File payloadFile ;
    @Override
    public boolean check(Map param) {
        AbstractPlugin<File> executor = ysoserialGenerate.payload(param.get("payload").toString())
                .command(param.get("command").toString()).executor();

        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                break;
            }
            payloadFile = executor.result();
            if (payloadFile.exists() && payloadFile.length()>0)
                return true;
        }
        errorMsg = "生成失败，文件不存在";
        return false;
    }

    @Override
    public Object start() {
        try {

            FileInputStream  fis = new FileInputStream(payloadFile);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            ServletOutputStream out =response.getOutputStream();
            out.write(b);
            out.flush();
            out.close();

        } catch (Exception e) {
            SysLog.error(e);
        }

        return null;
    }
}
