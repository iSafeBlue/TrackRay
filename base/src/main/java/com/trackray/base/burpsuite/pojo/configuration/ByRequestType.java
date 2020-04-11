package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByRequestType {

    @JsonProperty("hide_not_found_items")
    private boolean hideNotFoundItems;
    @JsonProperty("show_only_in_scope_items")
    private boolean showOnlyInScopeItems;
    @JsonProperty("show_only_parameterized_requests")
    private boolean showOnlyParameterizedRequests;
    @JsonProperty("show_only_requested_items")
    private boolean showOnlyRequestedItems;
    public void setHideNotFoundItems(boolean hideNotFoundItems) {
         this.hideNotFoundItems = hideNotFoundItems;
     }
     public boolean getHideNotFoundItems() {
         return hideNotFoundItems;
     }

    public void setShowOnlyInScopeItems(boolean showOnlyInScopeItems) {
         this.showOnlyInScopeItems = showOnlyInScopeItems;
     }
     public boolean getShowOnlyInScopeItems() {
         return showOnlyInScopeItems;
     }

    public void setShowOnlyParameterizedRequests(boolean showOnlyParameterizedRequests) {
         this.showOnlyParameterizedRequests = showOnlyParameterizedRequests;
     }
     public boolean getShowOnlyParameterizedRequests() {
         return showOnlyParameterizedRequests;
     }

    public void setShowOnlyRequestedItems(boolean showOnlyRequestedItems) {
         this.showOnlyRequestedItems = showOnlyRequestedItems;
     }
     public boolean getShowOnlyRequestedItems() {
         return showOnlyRequestedItems;
     }

}