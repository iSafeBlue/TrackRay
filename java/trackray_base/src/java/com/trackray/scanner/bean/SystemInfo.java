package com.trackray.scanner.bean;

import com.trackray.scanner.entity.FingerEntity;
import com.trackray.scanner.entity.PortEntity;
import com.trackray.scanner.entity.SystemEntity;
import com.trackray.scanner.enums.Language;
import com.trackray.scanner.enums.WEBServer;

import java.util.*;

public class SystemInfo {

    private Set<PortEntity> ports = new HashSet<>();

    private SystemEntity systemOS;

    private WEBServer webServer;

    private Language language = Language.OTHER;

    private FingerEntity finger ;

    private Map<String,Integer> dirs = new HashMap<>();

    public Map<String, Integer> getDirs() {
        return dirs;
    }

    public void setDirs(Map<String, Integer> dirs) {
        this.dirs = dirs;
    }

    public Set<PortEntity> getPorts() {
        return ports;
    }

    public void setPorts(Set<PortEntity> ports) {
        this.ports = ports;
    }

    public SystemEntity getSystemOS() {
        return systemOS;
    }

    public void setSystemOS(SystemEntity systemOS) {
        this.systemOS = systemOS;
    }

    public FingerEntity getFinger() {
        return finger;
    }

    public void setFinger(FingerEntity finger) {
        this.finger = finger;
    }

    public WEBServer getWebServer() {
        return webServer;
    }

    public void setWebServer(WEBServer webServer) {
        this.webServer = webServer;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
