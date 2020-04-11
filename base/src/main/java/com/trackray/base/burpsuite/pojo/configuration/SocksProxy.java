package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class SocksProxy {

    @JsonProperty("dns_over_socks")
    private boolean dnsOverSocks;
    private String host;
    private String password;
    private int port;
    @JsonProperty("use_proxy")
    private boolean useProxy;
    @JsonProperty("use_user_options")
    private boolean useUserOptions;
    private String username;
    public void setDnsOverSocks(boolean dnsOverSocks) {
         this.dnsOverSocks = dnsOverSocks;
     }
     public boolean getDnsOverSocks() {
         return dnsOverSocks;
     }

    public void setHost(String host) {
         this.host = host;
     }
     public String getHost() {
         return host;
     }

    public void setPassword(String password) {
         this.password = password;
     }
     public String getPassword() {
         return password;
     }

    public void setPort(int port) {
         this.port = port;
     }
     public int getPort() {
         return port;
     }

    public void setUseProxy(boolean useProxy) {
         this.useProxy = useProxy;
     }
     public boolean getUseProxy() {
         return useProxy;
     }

    public void setUseUserOptions(boolean useUserOptions) {
         this.useUserOptions = useUserOptions;
     }
     public boolean getUseUserOptions() {
         return useUserOptions;
     }

    public void setUsername(String username) {
         this.username = username;
     }
     public String getUsername() {
         return username;
     }

}