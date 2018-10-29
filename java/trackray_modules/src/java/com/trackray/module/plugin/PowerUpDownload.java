package com.trackray.module.plugin;

import com.trackray.scanner.annotation.Plugin;
import com.trackray.scanner.annotation.Rule;
import com.trackray.scanner.handle.Shell;
import com.trackray.scanner.plugin.AbstractPlugin;
import com.trackray.scanner.plugin.CommonPlugin;
import com.trackray.scanner.utils.ReUtils;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Plugin(value = "powerupTools",title = "Windows 后渗透工具包" , author = "blue")
@Rule(type = AbstractPlugin.Type.HTML , filename = "tools.zip")
public class PowerUpDownload extends CommonPlugin<String> {
    public static final String PATH = BASE +"/poweruptools/tools.zip";

    @Override
    public boolean check(Map<String, String> param) {
        return true;
    }
    @Override
    public String start()
    {
        try {
            FileInputStream  fis = new FileInputStream(new File(PATH));
            byte[] b = new byte[fis.available()];
            fis.read(b);
            ServletOutputStream  out =response.getOutputStream();
            //输出
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
