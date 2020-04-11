package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class InterceptWebSocketsMessages {

    @JsonProperty("client_to_server_messages")
    private boolean clientToServerMessages;
    @JsonProperty("server_to_client_messages")
    private boolean serverToClientMessages;
    public void setClientToServerMessages(boolean clientToServerMessages) {
         this.clientToServerMessages = clientToServerMessages;
     }
     public boolean getClientToServerMessages() {
         return clientToServerMessages;
     }

    public void setServerToClientMessages(boolean serverToClientMessages) {
         this.serverToClientMessages = serverToClientMessages;
     }
     public boolean getServerToClientMessages() {
         return serverToClientMessages;
     }

}