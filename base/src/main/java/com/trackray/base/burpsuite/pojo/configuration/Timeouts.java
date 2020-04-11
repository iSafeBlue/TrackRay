package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Timeouts {

    @JsonProperty("domain_name_resolution_timeout")
    private int domainNameResolutionTimeout;
    @JsonProperty("failed_domain_name_resolution_timeout")
    private int failedDomainNameResolutionTimeout;
    @JsonProperty("normal_timeout")
    private int normalTimeout;
    @JsonProperty("open_ended_response_timeout")
    private int openEndedResponseTimeout;
    public void setDomainNameResolutionTimeout(int domainNameResolutionTimeout) {
         this.domainNameResolutionTimeout = domainNameResolutionTimeout;
     }
     public int getDomainNameResolutionTimeout() {
         return domainNameResolutionTimeout;
     }

    public void setFailedDomainNameResolutionTimeout(int failedDomainNameResolutionTimeout) {
         this.failedDomainNameResolutionTimeout = failedDomainNameResolutionTimeout;
     }
     public int getFailedDomainNameResolutionTimeout() {
         return failedDomainNameResolutionTimeout;
     }

    public void setNormalTimeout(int normalTimeout) {
         this.normalTimeout = normalTimeout;
     }
     public int getNormalTimeout() {
         return normalTimeout;
     }

    public void setOpenEndedResponseTimeout(int openEndedResponseTimeout) {
         this.openEndedResponseTimeout = openEndedResponseTimeout;
     }
     public int getOpenEndedResponseTimeout() {
         return openEndedResponseTimeout;
     }

}