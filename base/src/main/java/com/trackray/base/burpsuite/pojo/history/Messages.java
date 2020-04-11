/**
  * Copyright 2020 bejson.com 
  */
package com.trackray.base.burpsuite.pojo.history;
import java.util.List;

/**
 * Auto-generated: 2020-01-09 19:5:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Messages {

    private int code;
    private String host;
    private int port;
    private String protocol;
    private String url;
    private int statusCode;
    private String request;
    private String response;
    private String method;
    private List<String> responseHeaders;
    private List<String> cookies;
    private List<String> parameters;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setHost(String host) {
         this.host = host;
     }
     public String getHost() {
         return host;
     }

    public void setPort(int port) {
         this.port = port;
     }
     public int getPort() {
         return port;
     }

    public void setProtocol(String protocol) {
         this.protocol = protocol;
     }
     public String getProtocol() {
         return protocol;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setStatusCode(int statusCode) {
         this.statusCode = statusCode;
     }
     public int getStatusCode() {
         return statusCode;
     }

    public void setRequest(String request) {
         this.request = request;
     }
     public String getRequest() {
         return request;
     }

    public void setResponse(String response) {
         this.response = response;
     }
     public String getResponse() {
         return response;
     }

    public void setMethod(String method) {
         this.method = method;
     }
     public String getMethod() {
         return method;
     }

    public void setResponseHeaders(List<String> responseHeaders) {
         this.responseHeaders = responseHeaders;
     }
     public List<String> getResponseHeaders() {
         return responseHeaders;
     }

    public void setCookies(List<String> cookies) {
         this.cookies = cookies;
     }
     public List<String> getCookies() {
         return cookies;
     }

    public void setParameters(List<String> parameters) {
         this.parameters = parameters;
     }
     public List<String> getParameters() {
         return parameters;
     }

}