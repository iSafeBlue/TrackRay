package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
@Data
public class BurpConfig {

    @JsonProperty("project_options")
    private ProjectOptions projectOptions;
    private Proxy proxy;
    private Repeater repeater;
    private Sequencer sequencer;
    private Target target;
    public void setProjectOptions(ProjectOptions projectOptions) {
         this.projectOptions = projectOptions;
     }
     public ProjectOptions getProjectOptions() {
         return projectOptions;
     }

    public void setProxy(Proxy proxy) {
         this.proxy = proxy;
     }
     public Proxy getProxy() {
         return proxy;
     }

    public void setRepeater(Repeater repeater) {
         this.repeater = repeater;
     }
     public Repeater getRepeater() {
         return repeater;
     }

    public void setSequencer(Sequencer sequencer) {
         this.sequencer = sequencer;
     }
     public Sequencer getSequencer() {
         return sequencer;
     }

    public void setTarget(Target target) {
         this.target = target;
     }
     public Target getTarget() {
         return target;
     }

}