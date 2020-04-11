package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByAnnotation {

    @JsonProperty("show_only_commented_items")
    private boolean showOnlyCommentedItems;
    @JsonProperty("show_only_highlighted_items")
    private boolean showOnlyHighlightedItems;
    public void setShowOnlyCommentedItems(boolean showOnlyCommentedItems) {
         this.showOnlyCommentedItems = showOnlyCommentedItems;
     }
     public boolean getShowOnlyCommentedItems() {
         return showOnlyCommentedItems;
     }

    public void setShowOnlyHighlightedItems(boolean showOnlyHighlightedItems) {
         this.showOnlyHighlightedItems = showOnlyHighlightedItems;
     }
     public boolean getShowOnlyHighlightedItems() {
         return showOnlyHighlightedItems;
     }

}