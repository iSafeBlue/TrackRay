package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Actions {

    private boolean enabled;
    @JsonProperty("match_cookies")
    private String matchCookies;
    private String type;
    public void setEnabled(boolean enabled) {
         this.enabled = enabled;
     }
     public boolean getEnabled() {
         return enabled;
     }

    public void setMatchCookies(String matchCookies) {
         this.matchCookies = matchCookies;
     }
     public String getMatchCookies() {
         return matchCookies;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

}