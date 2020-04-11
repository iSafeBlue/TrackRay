package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class CookieJar {

    @JsonProperty("monitor_extender")
    private boolean monitorExtender;
    @JsonProperty("monitor_intruder")
    private boolean monitorIntruder;
    @JsonProperty("monitor_proxy")
    private boolean monitorProxy;
    @JsonProperty("monitor_repeater")
    private boolean monitorRepeater;
    @JsonProperty("monitor_scanner")
    private boolean monitorScanner;
    @JsonProperty("monitor_sequencer")
    private boolean monitorSequencer;
    public void setMonitorExtender(boolean monitorExtender) {
         this.monitorExtender = monitorExtender;
     }
     public boolean getMonitorExtender() {
         return monitorExtender;
     }

    public void setMonitorIntruder(boolean monitorIntruder) {
         this.monitorIntruder = monitorIntruder;
     }
     public boolean getMonitorIntruder() {
         return monitorIntruder;
     }

    public void setMonitorProxy(boolean monitorProxy) {
         this.monitorProxy = monitorProxy;
     }
     public boolean getMonitorProxy() {
         return monitorProxy;
     }

    public void setMonitorRepeater(boolean monitorRepeater) {
         this.monitorRepeater = monitorRepeater;
     }
     public boolean getMonitorRepeater() {
         return monitorRepeater;
     }

    public void setMonitorScanner(boolean monitorScanner) {
         this.monitorScanner = monitorScanner;
     }
     public boolean getMonitorScanner() {
         return monitorScanner;
     }

    public void setMonitorSequencer(boolean monitorSequencer) {
         this.monitorSequencer = monitorSequencer;
     }
     public boolean getMonitorSequencer() {
         return monitorSequencer;
     }

}