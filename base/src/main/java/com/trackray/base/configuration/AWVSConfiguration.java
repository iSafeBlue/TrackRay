package com.trackray.base.configuration;

import com.trackray.base.attack.Awvs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/28 19:35
 */
@Configuration
public class AWVSConfiguration {

    @Autowired
    private Awvs awvs;

    public static final Logger log = LoggerFactory.getLogger(AWVSConfiguration.class);

    @Bean
    public void initAWVS(){

        if (TrackrayConfiguration.Function.awvs.isEnable()){
            log.info("[awvs] 您开启了awvs，正在初始化检查");
            awvs.initCheck();
        }else{
            log.info("[awvs] 未开启选项");
        }

    }

}
