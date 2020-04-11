package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByListener {

    @JsonProperty("listener_port")
    private String listenerPort;
    public void setListenerPort(String listenerPort) {
         this.listenerPort = listenerPort;
     }
     public String getListenerPort() {
         return listenerPort;
     }

}