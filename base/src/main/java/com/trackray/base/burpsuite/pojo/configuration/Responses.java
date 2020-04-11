package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Responses {

    @JsonProperty("all_tools")
    private String allTools;
    private String extender;
    private String intruder;
    private String proxy;
    private String repeater;
    private String scanner;
    private String sequencer;
    public void setAllTools(String allTools) {
         this.allTools = allTools;
     }
     public String getAllTools() {
         return allTools;
     }

    public void setExtender(String extender) {
         this.extender = extender;
     }
     public String getExtender() {
         return extender;
     }

    public void setIntruder(String intruder) {
         this.intruder = intruder;
     }
     public String getIntruder() {
         return intruder;
     }

    public void setProxy(String proxy) {
         this.proxy = proxy;
     }
     public String getProxy() {
         return proxy;
     }

    public void setRepeater(String repeater) {
         this.repeater = repeater;
     }
     public String getRepeater() {
         return repeater;
     }

    public void setScanner(String scanner) {
         this.scanner = scanner;
     }
     public String getScanner() {
         return scanner;
     }

    public void setSequencer(String sequencer) {
         this.sequencer = sequencer;
     }
     public String getSequencer() {
         return sequencer;
     }

}