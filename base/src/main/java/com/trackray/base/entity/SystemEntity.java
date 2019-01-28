package com.trackray.base.entity;

import com.trackray.base.enums.SystemOS;

public class SystemEntity {

    private SystemOS os;
    private String version;

    public SystemEntity() {
    }

    public SystemEntity(SystemOS os, String version) {
        this.os = os;
        this.version = version;
    }

    public SystemOS getOs() {
        return os;
    }

    public void setOs(SystemOS os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
