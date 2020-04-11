package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class MatchReplaceRules {

    private String comment;
    private boolean enabled;
    @JsonProperty("is_simple_match")
    private boolean isSimpleMatch;
    @JsonProperty("rule_type")
    private String ruleType;
    @JsonProperty("string_match")
    private String stringMatch;
    @JsonProperty("string_replace")
    private String stringReplace;
    public void setComment(String comment) {
         this.comment = comment;
     }
     public String getComment() {
         return comment;
     }

    public void setEnabled(boolean enabled) {
         this.enabled = enabled;
     }
     public boolean getEnabled() {
         return enabled;
     }

    public void setIsSimpleMatch(boolean isSimpleMatch) {
         this.isSimpleMatch = isSimpleMatch;
     }
     public boolean getIsSimpleMatch() {
         return isSimpleMatch;
     }

    public void setRuleType(String ruleType) {
         this.ruleType = ruleType;
     }
     public String getRuleType() {
         return ruleType;
     }

    public void setStringMatch(String stringMatch) {
         this.stringMatch = stringMatch;
     }
     public String getStringMatch() {
         return stringMatch;
     }

    public void setStringReplace(String stringReplace) {
         this.stringReplace = stringReplace;
     }
     public String getStringReplace() {
         return stringReplace;
     }

}