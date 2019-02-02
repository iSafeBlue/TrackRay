package com.trackray.base.handle;

import com.trackray.base.attack.Payload;
import com.trackray.base.attack.SQLMap;
import com.trackray.base.bean.Constant;
import com.trackray.base.httpclient.HttpClient;
import com.trackray.base.httpclient.ResponseStatus;
import com.trackray.base.utils.PropertyUtil;
import com.trackray.base.utils.SysLog;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 系统初始化类
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class SystemInit
{

    public static String LINE = System.getProperty("line.separator");

    public void init(){

        new Thread(){
            @Override
            public void run() {

                check();

            }
        }.start();

    }

    private void check() {
        String domain = PropertyUtil.getProperty("dict.domain");
        String dir = PropertyUtil.getProperty("dict.dir");

        Constant.CENSYS_APPID = PropertyUtil.getProperty("censys.appid");
        Constant.CENSYS_SECRET = PropertyUtil.getProperty("censys.secret");
        SQLMap.API = PropertyUtil.getProperty("sqlmap.root");

        try {
            SysLog.info("正在检测sqlmap api 服务");
            ResponseStatus resp = new HttpClient().get(SQLMap.API);
        } catch (Exception e) {
            SysLog.warn("检测到sqlmap api 服务未开启");
        }

        Shell pyshell = new Shell();
        try {
            SysLog.info("正在检测python环境");
            pyshell.target("python").exec("--version");
            if (!pyshell.readAll().contains("Python ")){
                SysLog.warn("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
            }
        } catch (IOException e) {
            if (!pyshell.readAll().contains("Python ")){
                SysLog.warn("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
            }
        }

        try {
            SysLog.info("正在检测nmap");
            Shell shell = new Shell();
            shell.target("nmap").exec();
            if (!shell.readAll().contains("nmap.org")){
                SysLog.warn("未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
            }
        } catch (IOException e) {
            SysLog.warn("未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
        }


        Constant.NmapComm.NMAP_DIR = PropertyUtil.getProperty("nmap.path");

        try {
            String filepath = new File(this.getClass().getClassLoader().getResource(PropertyUtil.getProperty("include.path")).getPath()).getPath();
            Constant.RESOURCES_PATH = filepath;
            Payload.domainPayload = FileUtils.readLines(new File(this.getClass().getClassLoader().getResource(domain).getFile()));
            Payload.dirPayload = FileUtils.readLines(new File(this.getClass().getClassLoader().getResource(dir).getFile()));
        } catch (Exception e) {

        }

        String p = "'';!--\"<XSS>=&{()}\n" +
                "\\\";alert('XSS');//\n" +
                "<script>alert(1)</script>\n"+
                "<h1>XSS</h1>";

        Payload.xssPayload = Arrays.asList(p.split("\n"));

        System.gc(); //垃圾回收
    }


}
