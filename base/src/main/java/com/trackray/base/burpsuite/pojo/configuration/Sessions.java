package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Sessions {

    @JsonProperty("cookie_jar")
    private CookieJar cookieJar;
    private Macros macros;
    @JsonProperty("session_handling_rules")
    private SessionHandlingRules sessionHandlingRules;
    public void setCookieJar(CookieJar cookieJar) {
         this.cookieJar = cookieJar;
     }
     public CookieJar getCookieJar() {
         return cookieJar;
     }

    public void setMacros(Macros macros) {
         this.macros = macros;
     }
     public Macros getMacros() {
         return macros;
     }

    public void setSessionHandlingRules(SessionHandlingRules sessionHandlingRules) {
         this.sessionHandlingRules = sessionHandlingRules;
     }
     public SessionHandlingRules getSessionHandlingRules() {
         return sessionHandlingRules;
     }

}