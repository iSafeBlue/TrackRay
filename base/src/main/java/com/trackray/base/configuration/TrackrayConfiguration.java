package com.trackray.base.configuration;

import com.trackray.base.attack.Payload;
import com.trackray.base.bean.Banner;
import com.trackray.base.bean.Constant;
import com.trackray.base.handle.Shell;
import com.trackray.base.utils.PropertyUtil;
import com.trackray.base.utils.SysLog;
import net.dongliu.requests.Requests;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/28 17:34
 */
@Configuration
public class TrackrayConfiguration {

    public enum Function{
        awvs(true),
        burpsuite(true),
        sqlmap(true),
        msf(true),
        xray(true),
        ;
        private boolean enable;
        Function(boolean b) {
            enable = b;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public static String [] names(){
            HashSet<String> set = new HashSet<>();
            for (Function function : values()) {
                set.add(function.name());
            }
            return set.toArray(new String[]{});
        }

    }

    static{
        Properties props=System.getProperties();
        String os = props.getProperty("os.name");
        //识别操作系统类型
        Constant.TRACKRAY_SYSTEMOS = (os.contains("indows") ? Constant.WINDOWS : Constant.LINUX);


    }


    public static ConcurrentHashMap<String,Object> sysVariable = new ConcurrentHashMap<String,Object>();

    @Value("${temp.dir}")
    private String tempdir;

    private  void createTempDirs() {
        File file = new File(tempdir);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    @Bean
    public void systemInitialize(){
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
                SysLog.info("[sqlmap] 检测到SQLMAP API已开启");
                Constant.AVAILABLE_SQLMAP = true;
            } catch (Exception e) {
                SysLog.warn("[sqlmap] SQLMAP API 服务未开启");
            }

            Shell pyshell = new Shell();
            try {
                pyshell.target("python --version").exec();
                if (!pyshell.readAll().contains("Python ")){
                    SysLog.warn("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
                }else{
                    Constant.AVAILABLE_PYTHON = true;
                    SysLog.info("python环境正常");
                }
            } catch (IOException e) {
                if (!pyshell.readAll().contains("Python ")){
                    SysLog.warn("未检测到python环境，本程序部分插件依赖于python，请保证已安装python。");
                }
            }

            try {
                Shell shell = new Shell();
                shell.target("nmap").exec();
                if (!shell.readAll().contains("nmap.org")){
                    SysLog.warn("[namp] 未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
                }else{
                    SysLog.info("[nmap] 已检测到nmap");
                    Constant.AVAILABLE_NMAP= true;
                }
            } catch (IOException e) {
                SysLog.warn("[nmap] 未检测到NMAP，本程序部分功能依赖于NMAP，请保证NMAP存在于系统变量中。");
            }

    }

    @Bean("sysVariable")
    public ConcurrentHashMap<String,Object> sysVariable(){
        return sysVariable;
    }


}
