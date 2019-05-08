package com.trackray.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class PortEntity {
    @JSONField(name="端口号")
    private Integer port;
    @JSONField(name="服务类型")
    private String service;
    @JSONField(name="服务版本")
    private String version;
    @JSONField(name="服务厂商")
    private String product;
    @JSONField(name="banner")
    private Map<String,String> banner;
    @JSONField(name="协议")
    private String protocol = "tcp";
}
