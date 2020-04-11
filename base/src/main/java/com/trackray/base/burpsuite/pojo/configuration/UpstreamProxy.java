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
public class UpstreamProxy {

    private List<ProxyServer> servers;
    @JsonProperty("use_user_options")
    private boolean useUserOptions;
    public void setServers(List<ProxyServer> servers) {
         this.servers = servers;
     }
    public List<ProxyServer> getServers() {
         return servers;
     }

    public void setUseUserOptions(boolean useUserOptions) {
         this.useUserOptions = useUserOptions;
     }
    public boolean getUseUserOptions() {
         return useUserOptions;
     }

}