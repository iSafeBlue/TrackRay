package com.trackray.base.httpclient;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class ResponseStatus {

    private String encoding = "";

    private byte[] contentBytes;

    private String contentString ="";

    private int statusCode = 404;

    private String contentType = "";

    private String contentTypeString= "";

    private String cookie = "";

    private Header[] headers;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeString() {
        return this.contentTypeString;
    }

    public void setContentTypeString(String contenttypeString) {
        this.contentTypeString = contenttypeString;
    }

    public String getContent()  {
        return this.getContent(this.encoding);
    }

    public String getContent(String encoding)  {
        if (contentString!=null && !contentString.isEmpty()){
            return contentString;
        }

        try {
            if (contentBytes!=null )
                return new String(contentBytes, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getUTFContent() throws UnsupportedEncodingException {
        return this.getContent("UTF-8");
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }
}