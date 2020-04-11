package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByFileExtension {

    @JsonProperty("hide_items")
    private List<String> hideItems;
    @JsonProperty("hide_specific")
    private boolean hideSpecific;
    @JsonProperty("show_items")
    private List<String> showItems;
    @JsonProperty("show_only_specific")
    private boolean showOnlySpecific;
    public void setHideItems(List<String> hideItems) {
         this.hideItems = hideItems;
     }
     public List<String> getHideItems() {
         return hideItems;
     }

    public void setHideSpecific(boolean hideSpecific) {
         this.hideSpecific = hideSpecific;
     }
     public boolean getHideSpecific() {
         return hideSpecific;
     }

    public void setShowItems(List<String> showItems) {
         this.showItems = showItems;
     }
     public List<String> getShowItems() {
         return showItems;
     }

    public void setShowOnlySpecific(boolean showOnlySpecific) {
         this.showOnlySpecific = showOnlySpecific;
     }
     public boolean getShowOnlySpecific() {
         return showOnlySpecific;
     }

}