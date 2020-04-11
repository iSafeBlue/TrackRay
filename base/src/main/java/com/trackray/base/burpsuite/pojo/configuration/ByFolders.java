package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ByFolders {

    @JsonProperty("hide_empty_folders")
    private boolean hideEmptyFolders;
    public void setHideEmptyFolders(boolean hideEmptyFolders) {
         this.hideEmptyFolders = hideEmptyFolders;
     }
     public boolean getHideEmptyFolders() {
         return hideEmptyFolders;
     }

}