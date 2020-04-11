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
public class ClientCertificates {

    private List<String> certificates;
    @JsonProperty("use_user_options")
    private boolean useUserOptions;
    public void setCertificates(List<String> certificates) {
         this.certificates = certificates;
     }
     public List<String> getCertificates() {
         return certificates;
     }

    public void setUseUserOptions(boolean useUserOptions) {
         this.useUserOptions = useUserOptions;
     }
     public boolean getUseUserOptions() {
         return useUserOptions;
     }

}