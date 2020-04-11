package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByMimeType {

    @JsonProperty("show_css")
    private boolean showCss;
    @JsonProperty("show_flash")
    private boolean showFlash;
    @JsonProperty("show_html")
    private boolean showHtml;
    @JsonProperty("show_images")
    private boolean showImages;
    @JsonProperty("show_other_binary")
    private boolean showOtherBinary;
    @JsonProperty("show_other_text")
    private boolean showOtherText;
    @JsonProperty("show_script")
    private boolean showScript;
    @JsonProperty("show_xml")
    private boolean showXml;
    public void setShowCss(boolean showCss) {
         this.showCss = showCss;
     }
     public boolean getShowCss() {
         return showCss;
     }

    public void setShowFlash(boolean showFlash) {
         this.showFlash = showFlash;
     }
     public boolean getShowFlash() {
         return showFlash;
     }

    public void setShowHtml(boolean showHtml) {
         this.showHtml = showHtml;
     }
     public boolean getShowHtml() {
         return showHtml;
     }

    public void setShowImages(boolean showImages) {
         this.showImages = showImages;
     }
     public boolean getShowImages() {
         return showImages;
     }

    public void setShowOtherBinary(boolean showOtherBinary) {
         this.showOtherBinary = showOtherBinary;
     }
     public boolean getShowOtherBinary() {
         return showOtherBinary;
     }

    public void setShowOtherText(boolean showOtherText) {
         this.showOtherText = showOtherText;
     }
     public boolean getShowOtherText() {
         return showOtherText;
     }

    public void setShowScript(boolean showScript) {
         this.showScript = showScript;
     }
     public boolean getShowScript() {
         return showScript;
     }

    public void setShowXml(boolean showXml) {
         this.showXml = showXml;
     }
     public boolean getShowXml() {
         return showXml;
     }

}