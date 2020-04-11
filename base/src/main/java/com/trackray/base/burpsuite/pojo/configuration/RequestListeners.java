package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class RequestListeners {

    @JsonProperty("certificate_mode")
    private String certificateMode;
    @JsonProperty("listen_mode")
    private String listenMode;
    @JsonProperty("listener_port")
    private int listenerPort;
    private boolean running;
    public void setCertificateMode(String certificateMode) {
         this.certificateMode = certificateMode;
     }
     public String getCertificateMode() {
         return certificateMode;
     }

    public void setListenMode(String listenMode) {
         this.listenMode = listenMode;
     }
     public String getListenMode() {
         return listenMode;
     }

    public void setListenerPort(int listenerPort) {
         this.listenerPort = listenerPort;
     }
     public int getListenerPort() {
         return listenerPort;
     }

    public void setRunning(boolean running) {
         this.running = running;
     }
     public boolean getRunning() {
         return running;
     }

}