package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Status100Responses {

    @JsonProperty("remove_100_continue_responses")
    private boolean remove100ContinueResponses;
    @JsonProperty("understand_100_continue_responses")
    private boolean understand100ContinueResponses;
    public void setRemove100ContinueResponses(boolean remove100ContinueResponses) {
         this.remove100ContinueResponses = remove100ContinueResponses;
     }
     public boolean getRemove100ContinueResponses() {
         return remove100ContinueResponses;
     }

    public void setUnderstand100ContinueResponses(boolean understand100ContinueResponses) {
         this.understand100ContinueResponses = understand100ContinueResponses;
     }
     public boolean getUnderstand100ContinueResponses() {
         return understand100ContinueResponses;
     }

}