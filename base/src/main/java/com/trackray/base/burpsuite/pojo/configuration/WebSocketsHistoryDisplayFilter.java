package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class WebSocketsHistoryDisplayFilter {

    @JsonProperty("by_annotation")
    private ByAnnotation byAnnotation;
    @JsonProperty("by_listener")
    private ByListener byListener;
    @JsonProperty("by_request_type")
    private ByRequestType byRequestType;
    @JsonProperty("by_search")
    private BySearch bySearch;
    public void setByAnnotation(ByAnnotation byAnnotation) {
         this.byAnnotation = byAnnotation;
     }
     public ByAnnotation getByAnnotation() {
         return byAnnotation;
     }

    public void setByListener(ByListener byListener) {
         this.byListener = byListener;
     }
     public ByListener getByListener() {
         return byListener;
     }

    public void setByRequestType(ByRequestType byRequestType) {
         this.byRequestType = byRequestType;
     }
     public ByRequestType getByRequestType() {
         return byRequestType;
     }

    public void setBySearch(BySearch bySearch) {
         this.bySearch = bySearch;
     }
     public BySearch getBySearch() {
         return bySearch;
     }

}