package com.trackray.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class Result {

    @JSONField(name="主机信息")
    private HostInfo hostInfo = new HostInfo();   //主机基本信息

    @JSONField(name="网络资产")
    private Assets assets = new Assets();       //目标的所有资产

    @JSONField(name="敏感信息")
    private SenseInfo senseInfo = new SenseInfo();  //敏感信息

    @JSONField(name="系统信息")
    private SystemInfo systemInfo = new SystemInfo();   //操作系统和运行环境的基本信息

    @JSONField(name="附加信息")
    private Map<String,Object> additional = new HashMap<>();    //附加信息

    @JSONField(serialize = false)
    private transient Map<String , Object> variables = new HashMap<>(); // 变量信息

}
