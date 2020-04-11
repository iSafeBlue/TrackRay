package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Sequencer {

    @JsonProperty("live_capture")
    private LiveCapture liveCapture;
    @JsonProperty("token_analysis")
    private TokenAnalysis tokenAnalysis;
    @JsonProperty("token_handling")
    private TokenHandling tokenHandling;
    public void setLiveCapture(LiveCapture liveCapture) {
         this.liveCapture = liveCapture;
     }
     public LiveCapture getLiveCapture() {
         return liveCapture;
     }

    public void setTokenAnalysis(TokenAnalysis tokenAnalysis) {
         this.tokenAnalysis = tokenAnalysis;
     }
     public TokenAnalysis getTokenAnalysis() {
         return tokenAnalysis;
     }

    public void setTokenHandling(TokenHandling tokenHandling) {
         this.tokenHandling = tokenHandling;
     }
     public TokenHandling getTokenHandling() {
         return tokenHandling;
     }

}