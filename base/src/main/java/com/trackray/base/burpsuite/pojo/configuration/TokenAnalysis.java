package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class TokenAnalysis {

    private boolean compression;
    private boolean correlation;
    private boolean count;
    @JsonProperty("fips_long_run")
    private boolean fipsLongRun;
    @JsonProperty("fips_monobit")
    private boolean fipsMonobit;
    @JsonProperty("fips_poker")
    private boolean fipsPoker;
    @JsonProperty("fips_runs")
    private boolean fipsRuns;
    private boolean spectral;
    private boolean transitions;
    public void setCompression(boolean compression) {
         this.compression = compression;
     }
     public boolean getCompression() {
         return compression;
     }

    public void setCorrelation(boolean correlation) {
         this.correlation = correlation;
     }
     public boolean getCorrelation() {
         return correlation;
     }

    public void setCount(boolean count) {
         this.count = count;
     }
     public boolean getCount() {
         return count;
     }

    public void setFipsLongRun(boolean fipsLongRun) {
         this.fipsLongRun = fipsLongRun;
     }
     public boolean getFipsLongRun() {
         return fipsLongRun;
     }

    public void setFipsMonobit(boolean fipsMonobit) {
         this.fipsMonobit = fipsMonobit;
     }
     public boolean getFipsMonobit() {
         return fipsMonobit;
     }

    public void setFipsPoker(boolean fipsPoker) {
         this.fipsPoker = fipsPoker;
     }
     public boolean getFipsPoker() {
         return fipsPoker;
     }

    public void setFipsRuns(boolean fipsRuns) {
         this.fipsRuns = fipsRuns;
     }
     public boolean getFipsRuns() {
         return fipsRuns;
     }

    public void setSpectral(boolean spectral) {
         this.spectral = spectral;
     }
     public boolean getSpectral() {
         return spectral;
     }

    public void setTransitions(boolean transitions) {
         this.transitions = transitions;
     }
     public boolean getTransitions() {
         return transitions;
     }

}