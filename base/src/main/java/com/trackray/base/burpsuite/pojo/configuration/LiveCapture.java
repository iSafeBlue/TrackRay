package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class LiveCapture {

    @JsonProperty("ignore_abnormal_length_tokens")
    private boolean ignoreAbnormalLengthTokens;
    @JsonProperty("max_length_deviation")
    private int maxLengthDeviation;
    @JsonProperty("num_threads")
    private int numThreads;
    private int throttle;
    public void setIgnoreAbnormalLengthTokens(boolean ignoreAbnormalLengthTokens) {
         this.ignoreAbnormalLengthTokens = ignoreAbnormalLengthTokens;
     }
     public boolean getIgnoreAbnormalLengthTokens() {
         return ignoreAbnormalLengthTokens;
     }

    public void setMaxLengthDeviation(int maxLengthDeviation) {
         this.maxLengthDeviation = maxLengthDeviation;
     }
     public int getMaxLengthDeviation() {
         return maxLengthDeviation;
     }

    public void setNumThreads(int numThreads) {
         this.numThreads = numThreads;
     }
     public int getNumThreads() {
         return numThreads;
     }

    public void setThrottle(int throttle) {
         this.throttle = throttle;
     }
     public int getThrottle() {
         return throttle;
     }

}