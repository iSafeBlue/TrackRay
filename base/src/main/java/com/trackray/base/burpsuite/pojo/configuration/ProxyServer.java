package com.trackray.base.burpsuite.pojo.configuration;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/15 14:14
 */
public class ProxyServer {

    private String destination_host;
    private boolean enabled;
    private String proxy_host;
    private int proxy_port;
    public void setDestination_host(String destination_host) {
        this.destination_host = destination_host;
    }
    public String getDestination_host() {
        return destination_host;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean getEnabled() {
        return enabled;
    }

    public void setProxy_host(String proxy_host) {
        this.proxy_host = proxy_host;
    }
    public String getProxy_host() {
        return proxy_host;
    }

    public void setProxy_port(int proxy_port) {
        this.proxy_port = proxy_port;
    }
    public int getProxy_port() {
        return proxy_port;
    }

}
