package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Ssl {

    @JsonProperty("client_certificates")
    private ClientCertificates clientCertificates;
    private Negotiation negotiation;
    public void setClientCertificates(ClientCertificates clientCertificates) {
         this.clientCertificates = clientCertificates;
     }
     public ClientCertificates getClientCertificates() {
         return clientCertificates;
     }

    public void setNegotiation(Negotiation negotiation) {
         this.negotiation = negotiation;
     }
     public Negotiation getNegotiation() {
         return negotiation;
     }

}