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
public class StreamingResponses {

    @JsonProperty("scope_advanced_mode")
    private boolean scopeAdvancedMode;
    private boolean store;
    @JsonProperty("strip_chunked_encoding_metadata")
    private boolean stripChunkedEncodingMetadata;
    private List<String> urls;
    public void setScopeAdvancedMode(boolean scopeAdvancedMode) {
         this.scopeAdvancedMode = scopeAdvancedMode;
     }
     public boolean getScopeAdvancedMode() {
         return scopeAdvancedMode;
     }

    public void setStore(boolean store) {
         this.store = store;
     }
     public boolean getStore() {
         return store;
     }

    public void setStripChunkedEncodingMetadata(boolean stripChunkedEncodingMetadata) {
         this.stripChunkedEncodingMetadata = stripChunkedEncodingMetadata;
     }
     public boolean getStripChunkedEncodingMetadata() {
         return stripChunkedEncodingMetadata;
     }

    public void setUrls(List<String> urls) {
         this.urls = urls;
     }
     public List<String> getUrls() {
         return urls;
     }

}