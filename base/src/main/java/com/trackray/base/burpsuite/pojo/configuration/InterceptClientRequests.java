package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class InterceptClientRequests {

    @JsonProperty("automatically_fix_missing_or_superfluous_new_lines_at_end_of_request")
    private boolean automaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest;
    @JsonProperty("automatically_update_content_length_header_when_the_request_is_edited")
    private boolean automaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited;
    @JsonProperty("do_intercept")
    private boolean doIntercept;
    private List<Rules> rules;
    public void setAutomaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest(boolean automaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest) {
         this.automaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest = automaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest;
     }
     public boolean getAutomaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest() {
         return automaticallyFixMissingOrSuperfluousNewLinesAtEndOfRequest;
     }

    public void setAutomaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited(boolean automaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited) {
         this.automaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited = automaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited;
     }
     public boolean getAutomaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited() {
         return automaticallyUpdateContentLengthHeaderWhenTheRequestIsEdited;
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