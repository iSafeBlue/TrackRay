package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Miscellaneous {

    @JsonProperty("disable_logging_to_history_and_site_map")
    private boolean disableLoggingToHistoryAndSiteMap;
    @JsonProperty("disable_out_of_scope_logging_to_history_and_site_map")
    private boolean disableOutOfScopeLoggingToHistoryAndSiteMap;
    @JsonProperty("disable_web_interface")
    private boolean disableWebInterface;
    @JsonProperty("remove_unsupported_encodings_from_accept_encoding_headers_in_incoming_requests")
    private boolean removeUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests;
    @JsonProperty("set_connection_close_header_on_requests")
    private boolean setConnectionCloseHeaderOnRequests;
    @JsonProperty("set_connection_close_header_on_responses")
    private boolean setConnectionCloseHeaderOnResponses;
    @JsonProperty("strip_proxy_headers_in_incoming_requests")
    private boolean stripProxyHeadersInIncomingRequests;
    @JsonProperty("strip_sec_websocket_extensions_headers_in_incoming_requests")
    private boolean stripSecWebsocketExtensionsHeadersInIncomingRequests;
    @JsonProperty("suppress_burp_error_messages_in_browser")
    private boolean suppressBurpErrorMessagesInBrowser;
    @JsonProperty("unpack_gzip_deflate_in_requests")
    private boolean unpackGzipDeflateInRequests;
    @JsonProperty("unpack_gzip_deflate_in_responses")
    private boolean unpackGzipDeflateInResponses;
    @JsonProperty("use_http_10_in_requests_to_server")
    private boolean useHttp10InRequestsToServer;
    @JsonProperty("use_http_10_in_responses_to_client")
    private boolean useHttp10InResponsesToClient;
    public void setDisableLoggingToHistoryAndSiteMap(boolean disableLoggingToHistoryAndSiteMap) {
         this.disableLoggingToHistoryAndSiteMap = disableLoggingToHistoryAndSiteMap;
     }
     public boolean getDisableLoggingToHistoryAndSiteMap() {
         return disableLoggingToHistoryAndSiteMap;
     }

    public void setDisableOutOfScopeLoggingToHistoryAndSiteMap(boolean disableOutOfScopeLoggingToHistoryAndSiteMap) {
         this.disableOutOfScopeLoggingToHistoryAndSiteMap = disableOutOfScopeLoggingToHistoryAndSiteMap;
     }
     public boolean getDisableOutOfScopeLoggingToHistoryAndSiteMap() {
         return disableOutOfScopeLoggingToHistoryAndSiteMap;
     }

    public void setDisableWebInterface(boolean disableWebInterface) {
         this.disableWebInterface = disableWebInterface;
     }
     public boolean getDisableWebInterface() {
         return disableWebInterface;
     }

    public void setRemoveUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests(boolean removeUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests) {
         this.removeUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests = removeUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests;
     }
     public boolean getRemoveUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests() {
         return removeUnsupportedEncodingsFromAcceptEncodingHeadersInIncomingRequests;
     }

    public void setSetConnectionCloseHeaderOnRequests(boolean setConnectionCloseHeaderOnRequests) {
         this.setConnectionCloseHeaderOnRequests = setConnectionCloseHeaderOnRequests;
     }
     public boolean getSetConnectionCloseHeaderOnRequests() {
         return setConnectionCloseHeaderOnRequests;
     }

    public void setSetConnectionCloseHeaderOnResponses(boolean setConnectionCloseHeaderOnResponses) {
         this.setConnectionCloseHeaderOnResponses = setConnectionCloseHeaderOnResponses;
     }
     public boolean getSetConnectionCloseHeaderOnResponses() {
         return setConnectionCloseHeaderOnResponses;
     }

    public void setStripProxyHeadersInIncomingRequests(boolean stripProxyHeadersInIncomingRequests) {
         this.stripProxyHeadersInIncomingRequests = stripProxyHeadersInIncomingRequests;
     }
     public boolean getStripProxyHeadersInIncomingRequests() {
         return stripProxyHeadersInIncomingRequests;
     }

    public void setStripSecWebsocketExtensionsHeadersInIncomingRequests(boolean stripSecWebsocketExtensionsHeadersInIncomingRequests) {
         this.stripSecWebsocketExtensionsHeadersInIncomingRequests = stripSecWebsocketExtensionsHeadersInIncomingRequests;
     }
     public boolean getStripSecWebsocketExtensionsHeadersInIncomingRequests() {
         return stripSecWebsocketExtensionsHeadersInIncomingRequests;
     }

    public void setSuppressBurpErrorMessagesInBrowser(boolean suppressBurpErrorMessagesInBrowser) {
         this.suppressBurpErrorMessagesInBrowser = suppressBurpErrorMessagesInBrowser;
     }
     public boolean getSuppressBurpErrorMessagesInBrowser() {
         return suppressBurpErrorMessagesInBrowser;
     }

    public void setUnpackGzipDeflateInRequests(boolean unpackGzipDeflateInRequests) {
         this.unpackGzipDeflateInRequests = unpackGzipDeflateInRequests;
     }
     public boolean getUnpackGzipDeflateInRequests() {
         return unpackGzipDeflateInRequests;
     }

    public void setUnpackGzipDeflateInResponses(boolean unpackGzipDeflateInResponses) {
         this.unpackGzipDeflateInResponses = unpackGzipDeflateInResponses;
     }
     public boolean getUnpackGzipDeflateInResponses() {
         return unpackGzipDeflateInResponses;
     }

    public void setUseHttp10InRequestsToServer(boolean useHttp10InRequestsToServer) {
         this.useHttp10InRequestsToServer = useHttp10InRequestsToServer;
     }
     public boolean getUseHttp10InRequestsToServer() {
         return useHttp10InRequestsToServer;
     }

    public void setUseHttp10InResponsesToClient(boolean useHttp10InResponsesToClient) {
         this.useHttp10InResponsesToClient = useHttp10InResponsesToClient;
     }
     public boolean getUseHttp10InResponsesToClient() {
         return useHttp10InResponsesToClient;
     }

}