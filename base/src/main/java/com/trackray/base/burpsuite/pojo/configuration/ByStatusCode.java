package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByStatusCode {

    @JsonProperty("show_2xx")
    private boolean show2xx;
    @JsonProperty("show_3xx")
    private boolean show3xx;
    @JsonProperty("show_4xx")
    private boolean show4xx;
    @JsonProperty("show_5xx")
    private boolean show5xx;
    public void setShow2xx(boolean show2xx) {
         this.show2xx = show2xx;
     }
     public boolean getShow2xx() {
         return show2xx;
     }

    public void setShow3xx(boolean show3xx) {
         this.show3xx = show3xx;
     }
     public boolean getShow3xx() {
         return show3xx;
     }

    public void setShow4xx(boolean show4xx) {
         this.show4xx = show4xx;
     }
     public boolean getShow4xx() {
         return show4xx;
     }

    public void setShow5xx(boolean show5xx) {
         this.show5xx = show5xx;
     }
     public boolean getShow5xx() {
         return show5xx;
     }

}