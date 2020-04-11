package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class PlatformAuthentication {

    private List<String> credentials;
    @JsonProperty("do_platform_authentication")
    private boolean doPlatformAuthentication;
    @JsonProperty("prompt_on_authentication_failure")
    private boolean promptOnAuthenticationFailure;
    @JsonProperty("use_user_options")
    private boolean useUserOptions;
    public void setCredentials(List<String> credentials) {
         this.credentials = credentials;
     }
     public List<String> getCredentials() {
         return credentials;
     }

    public void setDoPlatformAuthentication(boolean doPlatformAuthentication) {
         this.doPlatformAuthentication = doPlatformAuthentication;
     }
     public boolean getDoPlatformAuthentication() {
         return doPlatformAuthentication;
     }

    public void setPromptOnAuthenticationFailure(boolean promptOnAuthenticationFailure) {
         this.promptOnAuthenticationFailure = promptOnAuthenticationFailure;
     }
     public boolean getPromptOnAuthenticationFailure() {
         return promptOnAuthenticationFailure;
     }

    public void setUseUserOptions(boolean useUserOptions) {
         this.useUserOptions = useUserOptions;
     }
     public boolean getUseUserOptions() {
         return useUserOptions;
     }

}