package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Negotiation {

    @JsonProperty("allow_unsafe_renegotiation")
    private boolean allowUnsafeRenegotiation;
    @JsonProperty("automatically_select_compatible_ssl_parameters_on_failure")
    private boolean automaticallySelectCompatibleSslParametersOnFailure;
    @JsonProperty("disable_ssl_session_resume")
    private boolean disableSslSessionResume;
    @JsonProperty("enabled_ciphers")
    private List<String> enabledCiphers;
    @JsonProperty("enabled_protocols")
    private List<String> enabledProtocols;
    @JsonProperty("use_platform_default_protocols_and_ciphers")
    private boolean usePlatformDefaultProtocolsAndCiphers;
    public void setAllowUnsafeRenegotiation(boolean allowUnsafeRenegotiation) {
         this.allowUnsafeRenegotiation = allowUnsafeRenegotiation;
     }
     public boolean getAllowUnsafeRenegotiation() {
         return allowUnsafeRenegotiation;
     }

    public void setAutomaticallySelectCompatibleSslParametersOnFailure(boolean automaticallySelectCompatibleSslParametersOnFailure) {
         this.automaticallySelectCompatibleSslParametersOnFailure = automaticallySelectCompatibleSslParametersOnFailure;
     }
     public boolean getAutomaticallySelectCompatibleSslParametersOnFailure() {
         return automaticallySelectCompatibleSslParametersOnFailure;
     }

    public void setDisableSslSessionResume(boolean disableSslSessionResume) {
         this.disableSslSessionResume = disableSslSessionResume;
     }
     public boolean getDisableSslSessionResume() {
         return disableSslSessionResume;
     }

    public void setEnabledCiphers(List<String> enabledCiphers) {
         this.enabledCiphers = enabledCiphers;
     }
     public List<String> getEnabledCiphers() {
         return enabledCiphers;
     }

    public void setEnabledProtocols(List<String> enabledProtocols) {
         this.enabledProtocols = enabledProtocols;
     }
     public List<String> getEnabledProtocols() {
         return enabledProtocols;
     }

    public void setUsePlatformDefaultProtocolsAndCiphers(boolean usePlatformDefaultProtocolsAndCiphers) {
         this.usePlatformDefaultProtocolsAndCiphers = usePlatformDefaultProtocolsAndCiphers;
     }
     public boolean getUsePlatformDefaultProtocolsAndCiphers() {
         return usePlatformDefaultProtocolsAndCiphers;
     }

}