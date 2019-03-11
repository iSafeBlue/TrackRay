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

    private IPInfo ipInfo = new IPInfo();//IP和域名的一些基本信息
    private Assets assets = new Assets();//目标的所有资产
    private SenseInfo senseInfo = new SenseInfo();//敏感信息

    private Map<String,ResultItem> items = new HashMap<>();


}
