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
    @Autowired
    private Metasploit metasploit;

    static{
        Properties props=System.getProperties();
        String os = props.getProperty("os.name");
        //识别操作系统类型
        Constant.TRACKRAY_SYSTEMOS = (os.contains("indows") ? Constant.WINDOWS : Constant.LINUX);

        //配置系统常量
        Constant.RESOURCES_INCLUDE_PATH = Constant.RESOURCES_PATH.concat("include");
        Constant.SYSTEM_ACCOUNT = PropertyUtil.getProperty("trackray.account");
        Constant.SYSTEM_PASSWORD= PropertyUtil.getProperty("trackray.password");
        Constant.CENSYS_APPID = PropertyUtil.getProperty("censys.appid");
        Constant.CENSYS_SECRET = PropertyUtil.getProperty("censys.secret");
        Constant.SQLMAP_HOST = PropertyUtil.getProperty("sqlmap.host");


    }
    @Value("${temp.dir}")
    private String tempdir;

    private  void createTempDirs() {
        File file = new File(tempdir);
        if (!file.exists()){
            file.mkdirs();
        }
    }


    private void check() {

        //创建临时目录
        createTempDirs();
        String includePath = Constant.RESOURCES_INCLUDE_PATH;

        try {
            //加载字典
            Payload.domainPayload = FileUtils.readLines(new File(includePath.concat("/dicts/domain.txt")));
            Payload.dirPayload = FileUtils.readLines(new File((includePath.concat("/dicts/dir.txt"))));
            Payload.xssPayload = FileUtils.readLines(new File(includePath.concat("/dicts/xss.txt")));
            Payload.chinesePasswordTOP100 = FileUtils.readLines(new File(includePath.concat("/dicts/chinese-pwd-top-100.txt")));
            Payload.simpleUsername = FileUtils.readLines(new File(includePath.concat("/dicts/simple-username.txt")));
            Payload.usernameTOP500 = FileUtils.readLines(new File(includePath.concat("/dicts/username-top-500.txt")));

        } catch (Exception e) {
            SysLog.error("payload加载异常 "+e.getMessage());
        }


        try {
            Requests.get(Constant.SQLMAP_HOST).send();
            SysLog.info("检测到SQLMAP API已开启");
            Constant.AVAILABLE_SQLMAP = true;
        } catch (Exception e) {
            SysLog.error("SQLMAP API 服务未开启");
        }

        if (metasploit.login()){
            Constant.AVAILABLE_METASPLOIT = true;
            SysLog.info("metasploit 服务正常");
        }else{
            SysLog.error("metasploit 登录失败");
        }

        Shell pyshell = new Shell();
        try {
            pyshell.target("python").exec("--version");
            if (!pyshell.readAll().contains("Python ")){
                SysLog.error("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
            }else{
                Constant.AVAILABLE_PYTHON = true;
                SysLog.info("已检测到python环境");
            }
        } catch (IOException e) {
            if (!pyshell.readAll().contains("Python ")){
                SysLog.error("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
            }
        }

        try {
            Shell shell = new Shell();
            shell.target("nmap").exec();
            if (!shell.readAll().contains("nmap.org")){
                SysLog.error("未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
            }else{
                SysLog.info("已检测到nmap");
                Constant.AVAILABLE_NMAP= true;
            }
        } catch (IOException e) {
            SysLog.error("未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
        }



        System.gc(); //垃圾回收

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String generate = banner.generate();

        System.out.println(generate);
    }

}
