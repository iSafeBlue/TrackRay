package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class TokenHandling {

    @JsonProperty("base_64_decode_before_analyzing")
    private boolean base64DecodeBeforeAnalyzing;
    @JsonProperty("pad_short_tokens_at")
    private String padShortTokensAt;
    @JsonProperty("pad_with")
    private String padWith;
    public void setBase64DecodeBeforeAnalyzing(boolean base64DecodeBeforeAnalyzing) {
         this.base64DecodeBeforeAnalyzing = base64DecodeBeforeAnalyzing;
     }
     public boolean getBase64DecodeBeforeAnalyzing() {
         return base64DecodeBeforeAnalyzing;
     }

    public void setPadShortTokensAt(String padShortTokensAt) {
         this.padShortTokensAt = padShortTokensAt;
     }
     public String getPadShortTokensAt() {
         return padShortTokensAt;
     }

    public void setPadWith(String padWith) {
         this.padWith = padWith;
     }
     public String getPadWith() {
         return padWith;
     }

}