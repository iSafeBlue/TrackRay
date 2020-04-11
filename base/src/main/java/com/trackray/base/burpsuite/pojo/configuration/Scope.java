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
public class Scope {

    @JsonProperty("advanced_mode")
    private boolean advancedMode;
    private List<String> exclude;
    private List<String> include;
    public void setAdvancedMode(boolean advancedMode) {
         this.advancedMode = advancedMode;
     }
     public boolean getAdvancedMode() {
         return advancedMode;
     }

    public void setExclude(List<String> exclude) {
         this.exclude = exclude;
     }
     public List<String> getExclude() {
         return exclude;
     }

    public void setInclude(List<String> include) {
         this.include = include;
     }
     public List<String> getInclude() {
         return include;
     }

}