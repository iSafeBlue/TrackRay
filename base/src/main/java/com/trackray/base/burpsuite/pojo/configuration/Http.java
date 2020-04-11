package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Http {

    private Redirections redirections;
    @JsonProperty("status_100_responses")
    private Status100Responses status100Responses;
    @JsonProperty("streaming_responses")
    private StreamingResponses streamingResponses;
    public void setRedirections(Redirections redirections) {
         this.redirections = redirections;
     }
     public Redirections getRedirections() {
         return redirections;
     }

    public void setStatus100Responses(Status100Responses status100Responses) {
         this.status100Responses = status100Responses;
     }
     public Status100Responses getStatus100Responses() {
         return status100Responses;
     }

    public void setStreamingResponses(StreamingResponses streamingResponses) {
         this.streamingResponses = streamingResponses;
     }
     public StreamingResponses getStreamingResponses() {
         return streamingResponses;
     }

}