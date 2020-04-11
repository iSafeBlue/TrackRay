package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Connections {

    @JsonProperty("hostname_resolution")
    private List<String> hostnameResolution;
    @JsonProperty("out_of_scope_requests")
    private OutOfScopeRequests outOfScopeRequests;
    @JsonProperty("platform_authentication")
    private PlatformAuthentication platformAuthentication;
    @JsonProperty("socks_proxy")
    private SocksProxy socksProxy;
    private Timeouts timeouts;
    @JsonProperty("upstream_proxy")
    private UpstreamProxy upstreamProxy;
    public void setHostnameResolution(List<String> hostnameResolution) {
         this.hostnameResolution = hostnameResolution;
     }
     public List<String> getHostnameResolution() {
         return hostnameResolution;
     }

    public void setOutOfScopeRequests(OutOfScopeRequests outOfScopeRequests) {
         this.outOfScopeRequests = outOfScopeRequests;
     }
     public OutOfScopeRequests getOutOfScopeRequests() {
         return outOfScopeRequests;
     }

    public void setPlatformAuthentication(PlatformAuthentication platformAuthentication) {
         this.platformAuthentication = platformAuthentication;
     }
     public PlatformAuthentication getPlatformAuthentication() {
         return platformAuthentication;
     }

    public void setSocksProxy(SocksProxy socksProxy) {
         this.socksProxy = socksProxy;
     }
     public SocksProxy getSocksProxy() {
         return socksProxy;
     }

    public void setTimeouts(Timeouts timeouts) {
         this.timeouts = timeouts;
     }
     public Timeouts getTimeouts() {
         return timeouts;
     }

    public void setUpstreamProxy(UpstreamProxy upstreamProxy) {
         this.upstreamProxy = upstreamProxy;
     }
     public UpstreamProxy getUpstreamProxy() {
         return upstreamProxy;
     }

}