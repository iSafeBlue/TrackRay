package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class CollaboratorServer {

    private String location;
    @JsonProperty("poll_over_unencrypted_http")
    private boolean pollOverUnencryptedHttp;
    @JsonProperty("polling_location")
    private String pollingLocation;
    private String type;
    public void setLocation(String location) {
         this.location = location;
     }
     public String getLocation() {
         return location;
     }

    public void setPollOverUnencryptedHttp(boolean pollOverUnencryptedHttp) {
         this.pollOverUnencryptedHttp = pollOverUnencryptedHttp;
     }
     public boolean getPollOverUnencryptedHttp() {
         return pollOverUnencryptedHttp;
     }

    public void setPollingLocation(String pollingLocation) {
         this.pollingLocation = pollingLocation;
     }
     public String getPollingLocation() {
         return pollingLocation;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

}