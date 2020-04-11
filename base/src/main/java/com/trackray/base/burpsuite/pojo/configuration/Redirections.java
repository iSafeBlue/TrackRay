package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:18
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Redirections {

    @JsonProperty("understand_3xx_status_code")
    private boolean understand3xxStatusCode;
    @JsonProperty("understand_any_status_code_with_location_header")
    private boolean understandAnyStatusCodeWithLocationHeader;
    @JsonProperty("understand_javascript_driven")
    private boolean understandJavascriptDriven;
    @JsonProperty("understand_meta_refresh_tag")
    private boolean understandMetaRefreshTag;
    @JsonProperty("understand_refresh_header")
    private boolean understandRefreshHeader;
    public void setUnderstand3xxStatusCode(boolean understand3xxStatusCode) {
         this.understand3xxStatusCode = understand3xxStatusCode;
     }
     public boolean getUnderstand3xxStatusCode() {
         return understand3xxStatusCode;
     }

    public void setUnderstandAnyStatusCodeWithLocationHeader(boolean understandAnyStatusCodeWithLocationHeader) {
         this.understandAnyStatusCodeWithLocationHeader = understandAnyStatusCodeWithLocationHeader;
     }
     public boolean getUnderstandAnyStatusCodeWithLocationHeader() {
         return understandAnyStatusCodeWithLocationHeader;
     }

    public void setUnderstandJavascriptDriven(boolean understandJavascriptDriven) {
         this.understandJavascriptDriven = understandJavascriptDriven;
     }
     public boolean getUnderstandJavascriptDriven() {
         return understandJavascriptDriven;
     }

    public void setUnderstandMetaRefreshTag(boolean understandMetaRefreshTag) {
         this.understandMetaRefreshTag = understandMetaRefreshTag;
     }
     public boolean getUnderstandMetaRefreshTag() {
         return understandMetaRefreshTag;
     }

    public void setUnderstandRefreshHeader(boolean understandRefreshHeader) {
         this.understandRefreshHeader = understandRefreshHeader;
     }
     public boolean getUnderstandRefreshHeader() {
         return understandRefreshHeader;
     }

}