package com.trackray.base.burpsuite.pojo.configuration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Proxy {

    @JsonProperty("http_history_display_filter")
    private HttpHistoryDisplayFilter httpHistoryDisplayFilter;
    @JsonProperty("intercept_client_requests")
    private InterceptClientRequests interceptClientRequests;
    @JsonProperty("intercept_server_responses")
    private InterceptServerResponses interceptServerResponses;
    @JsonProperty("intercept_web_sockets_messages")
    private InterceptWebSocketsMessages interceptWebSocketsMessages;
    @JsonProperty("match_replace_rules")
    private List<MatchReplaceRules> matchReplaceRules;
    private Miscellaneous miscellaneous;
    @JsonProperty("request_listeners")
    private List<RequestListeners> requestListeners;
    @JsonProperty("response_modification")
    private ResponseModification responseModification;
    @JsonProperty("ssl_pass_through")
    private SslPassThrough sslPassThrough;
    @JsonProperty("web_sockets_history_display_filter")
    private WebSocketsHistoryDisplayFilter webSocketsHistoryDisplayFilter;
    public void setHttpHistoryDisplayFilter(HttpHistoryDisplayFilter httpHistoryDisplayFilter) {
         this.httpHistoryDisplayFilter = httpHistoryDisplayFilter;
     }
     public HttpHistoryDisplayFilter getHttpHistoryDisplayFilter() {
         return httpHistoryDisplayFilter;
     }

    public void setInterceptClientRequests(InterceptClientRequests interceptClientRequests) {
         this.interceptClientRequests = interceptClientRequests;
     }
     public InterceptClientRequests getInterceptClientRequests() {
         return interceptClientRequests;
     }

    public void setInterceptServerResponses(InterceptServerResponses interceptServerResponses) {
         this.interceptServerResponses = interceptServerResponses;
     }
     public InterceptServerResponses getInterceptServerResponses() {
         return interceptServerResponses;
     }

    public void setInterceptWebSocketsMessages(InterceptWebSocketsMessages interceptWebSocketsMessages) {
         this.interceptWebSocketsMessages = interceptWebSocketsMessages;
     }
     public InterceptWebSocketsMessages getInterceptWebSocketsMessages() {
         return interceptWebSocketsMessages;
     }

    public void setMatchReplaceRules(List<MatchReplaceRules> matchReplaceRules) {
         this.matchReplaceRules = matchReplaceRules;
     }
     public List<MatchReplaceRules> getMatchReplaceRules() {
         return matchReplaceRules;
     }

    public void setMiscellaneous(Miscellaneous miscellaneous) {
         this.miscellaneous = miscellaneous;
     }
     public Miscellaneous getMiscellaneous() {
         return miscellaneous;
     }

    public void setRequestListeners(List<RequestListeners> requestListeners) {
         this.requestListeners = requestListeners;
     }
     public List<RequestListeners> getRequestListeners() {
         return requestListeners;
     }

    public void setResponseModification(ResponseModification responseModification) {
         this.responseModification = responseModification;
     }
     public ResponseModification getResponseModification() {
         return responseModification;
     }

    public void setSslPassThrough(SslPassThrough sslPassThrough) {
         this.sslPassThrough = sslPassThrough;
     }
     public SslPassThrough getSslPassThrough() {
         return sslPassThrough;
     }

    public void setWebSocketsHistoryDisplayFilter(WebSocketsHistoryDisplayFilter webSocketsHistoryDisplayFilter) {
         this.webSocketsHistoryDisplayFilter = webSocketsHistoryDisplayFilter;
     }
     public WebSocketsHistoryDisplayFilter getWebSocketsHistoryDisplayFilter() {
         return webSocketsHistoryDisplayFilter;
     }

}