package com.trackray.scanner.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultItem {


    private SystemInfo systemInfo = new SystemInfo();//操作系统和运行环境的基本信息
    private List<Vulnerable> vulns = new ArrayList<>();

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public List<Vulnerable> getVulns() {
        return vulns;
    }

    public void setVulns(List<Vulnerable> vulns) {
        this.vulns = vulns;
    }
}
