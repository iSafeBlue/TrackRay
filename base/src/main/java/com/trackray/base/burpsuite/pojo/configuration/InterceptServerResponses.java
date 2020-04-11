package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class InterceptServerResponses {

    @JsonProperty("automatically_update_content_length_header_when_the_response_is_edited")
    private boolean automaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited;
    @JsonProperty("do_intercept")
    private boolean doIntercept;
    private List<Rules> rules;
    public void setAutomaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited(boolean automaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited) {
         this.automaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited = automaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited;
     }
     public boolean getAutomaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited() {
         return automaticallyUpdateContentLengthHeaderWhenTheResponseIsEdited;
     }

    public void setDoIntercept(boolean doIntercept) {
         this.doIntercept = doIntercept;
     }
     public boolean getDoIntercept() {
         return doIntercept;
     }

    public void setRules(List<Rules> rules) {
         this.rules = rules;
     }
     public List<Rules> getRules() {
         return rules;
     }

}