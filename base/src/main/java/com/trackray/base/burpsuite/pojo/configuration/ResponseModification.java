package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ResponseModification {

    @JsonProperty("convert_https_links_to_http")
    private boolean convertHttpsLinksToHttp;
    @JsonProperty("enable_disabled_form_fields")
    private boolean enableDisabledFormFields;
    @JsonProperty("highlight_unhidden_fields")
    private boolean highlightUnhiddenFields;
    @JsonProperty("remove_all_javascript")
    private boolean removeAllJavascript;
    @JsonProperty("remove_input_field_length_limits")
    private boolean removeInputFieldLengthLimits;
    @JsonProperty("remove_javascript_form_validation")
    private boolean removeJavascriptFormValidation;
    @JsonProperty("remove_object_tags")
    private boolean removeObjectTags;
    @JsonProperty("remove_secure_flag_from_cookies")
    private boolean removeSecureFlagFromCookies;
    @JsonProperty("unhide_hidden_form_fields")
    private boolean unhideHiddenFormFields;
    public void setConvertHttpsLinksToHttp(boolean convertHttpsLinksToHttp) {
         this.convertHttpsLinksToHttp = convertHttpsLinksToHttp;
     }
     public boolean getConvertHttpsLinksToHttp() {
         return convertHttpsLinksToHttp;
     }

    public void setEnableDisabledFormFields(boolean enableDisabledFormFields) {
         this.enableDisabledFormFields = enableDisabledFormFields;
     }
     public boolean getEnableDisabledFormFields() {
         return enableDisabledFormFields;
     }

    public void setHighlightUnhiddenFields(boolean highlightUnhiddenFields) {
         this.highlightUnhiddenFields = highlightUnhiddenFields;
     }
     public boolean getHighlightUnhiddenFields() {
         return highlightUnhiddenFields;
     }

    public void setRemoveAllJavascript(boolean removeAllJavascript) {
         this.removeAllJavascript = removeAllJavascript;
     }
     public boolean getRemoveAllJavascript() {
         return removeAllJavascript;
     }

    public void setRemoveInputFieldLengthLimits(boolean removeInputFieldLengthLimits) {
         this.removeInputFieldLengthLimits = removeInputFieldLengthLimits;
     }
     public boolean getRemoveInputFieldLengthLimits() {
         return removeInputFieldLengthLimits;
     }

    public void setRemoveJavascriptFormValidation(boolean removeJavascriptFormValidation) {
         this.removeJavascriptFormValidation = removeJavascriptFormValidation;
     }
     public boolean getRemoveJavascriptFormValidation() {
         return removeJavascriptFormValidation;
     }

    public void setRemoveObjectTags(boolean removeObjectTags) {
         this.removeObjectTags = removeObjectTags;
     }
     public boolean getRemoveObjectTags() {
         return removeObjectTags;
     }

    public void setRemoveSecureFlagFromCookies(boolean removeSecureFlagFromCookies) {
         this.removeSecureFlagFromCookies = removeSecureFlagFromCookies;
     }
     public boolean getRemoveSecureFlagFromCookies() {
         return removeSecureFlagFromCookies;
     }

    public void setUnhideHiddenFormFields(boolean unhideHiddenFormFields) {
         this.unhideHiddenFormFields = unhideHiddenFormFields;
     }
     public boolean getUnhideHiddenFormFields() {
         return unhideHiddenFormFields;
     }

}