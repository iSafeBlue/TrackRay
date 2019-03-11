package com.trackray.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations= {"classpath:trackray-*-*.xml"})
public class XmlConfig {

}
