package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Rules {

    @JsonProperty("boolean_operator")
    private String booleanOperator;
    private boolean enabled;
    @JsonProperty("match_condition")
    private String matchCondition;
    @JsonProperty("match_relationship")
    private String matchRelationship;
    @JsonProperty("match_type")
    private String matchType;
    public void setBooleanOperator(String booleanOperator) {
         this.booleanOperator = booleanOperator;
     }
     public String getBooleanOperator() {
         return booleanOperator;
     }

    public void setEnabled(boolean enabled) {
         this.enabled = enabled;
     }
     public boolean getEnabled() {
         return enabled;
     }

    public void setMatchCondition(String matchCondition) {
         this.matchCondition = matchCondition;
     }
     public String getMatchCondition() {
         return matchCondition;
     }

    public void setMatchRelationship(String matchRelationship) {
         this.matchRelationship = matchRelationship;
     }
     public String getMatchRelationship() {
         return matchRelationship;
     }

    public void setMatchType(String matchType) {
         this.matchType = matchType;
     }
     public String getMatchType() {
         return matchType;
     }

}