package com.trackray.base.configuration;

import com.trackray.base.attack.Metasploit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/28 19:53
 */
@Configuration
public class MSFConfiguration {

    @Autowired
    private Metasploit metasploit;

    public static final Logger log = LoggerFactory.getLogger(MSFConfiguration.class);


    @Bean
    public void initMetasploit(){

        if (TrackrayConfiguration.Function.msf.isEnable()){
            log.info("[msf] 您开启了msf配置项，正在登录msf接口");
            metasploit.login();
        }else {
            log.info("[msf] 未开启选项");
        }

    }


}
