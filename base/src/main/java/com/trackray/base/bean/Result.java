package com.trackray.base.bean;

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

    private HostInfo hostInfo = new HostInfo();   //主机基本信息

    private Assets assets = new Assets();       //目标的所有资产

    private SenseInfo senseInfo = new SenseInfo();  //敏感信息

    private SystemInfo systemInfo = new SystemInfo();   //操作系统和运行环境的基本信息

    private Map<String,ResultItem> items = new HashMap<>();

    private Map<String,String> additional = new HashMap<>();    //附加信息


}
