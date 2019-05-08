package com.trackray.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.enums.Language;
import com.trackray.base.enums.WEBServer;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class SystemInfo {

    @JSONField(name="端口")
    private Set<PortEntity> ports = new HashSet<>();

    @JSONField(name="操作系统")
    private SystemEntity systemOS;

    @JSONField(name="WEB服务器")
    private volatile WEBServer webServer = WEBServer.OTHER;

    @JSONField(name="开发语言")
    private volatile Language language = Language.OTHER;

    @JSONField(name="WEB指纹")
    private volatile FingerPrint finger = FingerPrint.unknown;

    @JSONField(name="敏感目录")
    private Map<String,Integer> dirs = new HashMap<>();

}
