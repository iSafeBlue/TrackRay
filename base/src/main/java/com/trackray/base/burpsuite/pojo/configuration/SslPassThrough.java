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
public class SslPassThrough {

    @JsonProperty("automatically_add_entries_on_client_ssl_negotiation_failure")
    private boolean automaticallyAddEntriesOnClientSslNegotiationFailure;
    private List<String> rules;
    public void setAutomaticallyAddEntriesOnClientSslNegotiationFailure(boolean automaticallyAddEntriesOnClientSslNegotiationFailure) {
         this.automaticallyAddEntriesOnClientSslNegotiationFailure = automaticallyAddEntriesOnClientSslNegotiationFailure;
     }
     public boolean getAutomaticallyAddEntriesOnClientSslNegotiationFailure() {
         return automaticallyAddEntriesOnClientSslNegotiationFailure;
     }

    public void setRules(List<String> rules) {
         this.rules = rules;
     }
     public List<String> getRules() {
         return rules;
     }

}