package com.trackray.base.entity;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class PortEntity {
    private Integer port;
    private String service;
    private String version;
    private String product;
    private Map<String,String> banner;
    private String protocol = "tcp";
}
