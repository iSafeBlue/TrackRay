package com.trackray.base.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class ResultItem {


    private SystemInfo systemInfo = new SystemInfo();//操作系统和运行环境的基本信息
    private List<Vulnerable> vulns = new ArrayList<>();

    private Map<String,Object> temp = new HashMap<>();

}
