package com.trackray.base.entity;

public class PortEntity {
    private Integer port;
    private String service;


    public PortEntity() {
    }

    public PortEntity(Integer port, String service) {
        this.port = port;
        this.service = service;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
