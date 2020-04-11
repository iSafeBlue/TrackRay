package com.trackray.base.handle;

import com.trackray.base.attack.Metasploit;
import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Banner;
import com.trackray.base.bean.Constant;
import com.trackray.base.utils.ExtractUtils;
import com.trackray.base.utils.PropertyUtil;
import com.trackray.base.utils.SysLog;
import net.dongliu.requests.Requests;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpURLRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 系统初始化类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class SystemInit
{

    public void init(){

        new Thread(){
            @Override
            public void run() {

                check();

            }
        }.start();

    }
    @Autowired
    private Banner banner;


    private void check() {

        System.gc(); //垃圾回收

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //String generate = banner.generate();

        //System.out.println(generate);
    }

}
