package com.trackray.base.configuration;

import com.trackray.base.attack.XRay;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/28 19:35
 */
@Configuration
public class XRayConfiguration {

    public static final Logger log = LoggerFactory.getLogger(XRayConfiguration.class);

    @Autowired
    private XRay xRay;

    @Autowired
    private ConcurrentHashMap<String,Object> sysVariable;

    @Value("${xray.remote.host}")
    private String host;

    @Value("${xray.remote.port}")
    private String port;


    @Bean("xRayExecutor")
    public DefaultExecutor initXRay(){
        if (TrackrayConfiguration.Function.xray.isEnable()) {

            XRay x = xRay.executor().result();

            if (x!=null && x.isPortUsing(host, Integer.parseInt(port))) {

                log.info("[xray] 端口被占用或已启动xray进程");

            } else {
                log.info("[xray] 正在启动xray被动代理 [" + (host + ":" + port) + "]");

                String filename = String.valueOf(System.currentTimeMillis());

                filename = "system_passive_" + filename;

                sysVariable.put("XRAY_REPORT", filename);

                x.setListen(host, Integer.parseInt(port));

                x.outputHTML(filename.concat(".html"));

                x.outputJSON(filename.concat(".json"));

                DefaultExecutor run = x.run();
                return run;
            }
        }else {
            log.info("[xray] 未开启选项");
        }
        return new DefaultExecutor();
    }

    @Autowired
    private DefaultExecutor xRayExecutor;

    @PreDestroy
    public void destroy() {
        if (xRayExecutor!=null&&xRayExecutor.getWatchdog()!=null) {
            log.info("[xray] 正在结束进程");

            xRayExecutor.getWatchdog().destroyProcess();
        }
    }
}
