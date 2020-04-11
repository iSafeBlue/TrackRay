package com.trackray.base.configuration;

import com.trackray.base.bean.Constant;
import com.trackray.base.burpsuite.pojo.BurpSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/9 20:46
 */
@Configuration
public class BurpSuiteConfiguration {

    @Autowired
    private BurpSuite burpSuite;

    public enum Mode{
        LOCAL,
        REMOTE,
    }

    public static Mode mode = Mode.LOCAL;

    public static final Logger log = LoggerFactory.getLogger(BurpSuiteConfiguration.class);

    @Value("${burp.remote.host}")
    public String burp_remote_host;

    @Value("${burp.remote.port}")
    public String burp_remote_port;

    @Value("${burp.local.loader}")
    public String burp_local_loader;

    @Value("${burp.local.headless}")
    public String burp_local_headless;


    @Bean
    public void initBurpsuite(){

        if (TrackrayConfiguration.Function.burpsuite.isEnable() && !burpSuite.checkActive()){

            log.info("[burpsuite] 正在启动burpsuite，非无头模式启动成功情况下请点击弹出框的任意按钮否则功能无法使用，如有特殊配置需要请在页面中操作关闭再重新配置后开启");
            BurpSuite.BurpSuiteOption option = burpSuite.option();
            option.workDir(new File(Constant.RESOURCES_INCLUDE_PATH+"/burpsuite/"));

            if (mode == Mode.LOCAL){
                option.headlessMode(Boolean.parseBoolean(burp_local_headless));
                option.needLoader(Boolean.parseBoolean(burp_local_loader));
                option.serverAddress("127.0.0.1");
                option.serverPort(8090);
                burpSuite.open();
            }else if (mode == Mode.REMOTE){
                option.serverAddress(burp_remote_host);
                option.serverPort(Integer.parseInt(burp_remote_port));
            }

        }else{
            log.info("[burpsuite] 未开启选项");
        }
    }

}
