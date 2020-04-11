package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Repeater {

    @JsonProperty("follow_redirections")
    private String followRedirections;
    @JsonProperty("process_cookies_in_redirections")
    private boolean processCookiesInRedirections;
    @JsonProperty("unpack_gzip_deflate")
    private boolean unpackGzipDeflate;
    @JsonProperty("update_content_length")
    private boolean updateContentLength;
    public void setFollowRedirections(String followRedirections) {
         this.followRedirections = followRedirections;
     }
     public String getFollowRedirections() {
         return followRedirections;
     }

    public void setProcessCookiesInRedirections(boolean processCookiesInRedirections) {
         this.processCookiesInRedirections = processCookiesInRedirections;
     }
     public boolean getProcessCookiesInRedirections() {
         return processCookiesInRedirections;
     }

    public void setUnpackGzipDeflate(boolean unpackGzipDeflate) {
         this.unpackGzipDeflate = unpackGzipDeflate;
     }
     public boolean getUnpackGzipDeflate() {
         return unpackGzipDeflate;
     }

    public void setUpdateContentLength(boolean updateContentLength) {
         this.updateContentLength = updateContentLength;
     }
     public boolean getUpdateContentLength() {
         return updateContentLength;
     }

}