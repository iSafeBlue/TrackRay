package com.trackray.base.httpclient;

import com.trackray.base.enums.HttpMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

public class Request implements Cloneable{

    private String url;

    private Header[] httpHeaders = new Header[]{
            new BasicHeader("Accept-Encoding", "gzip,deflate"),
            new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0")
    };

    private String cookie;

    private HttpMethod httpMethod = HttpMethod.GET;

    private String charset = "UTF-8";

    private String paramStr = "";

    private int timeout = 2000;

    private Map<String,String> paramMap = new HashMap<>();

    public static enum header{
        //Content-Type: application/x-www-form-urlencoded
        ContentTypePOST("Content-Type","application/x-www-form-urlencoded"),
        ContentTypeJSON("Content-Type","application/json"),
        ContentTypeXML("Content-Type","application/xml"),
        AcceptEncoding("Accept-Encoding","gzip, deflate"),
        Cookie("Cookie",""),
        UserAgent("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0"),
        HOST("Host",""),
        Accept("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        private String key;
        private String defV;

        private header(String key , String vul) {
            this.key = key;
            this.defV = vul;
        }

        public String getKey() {
            return key;
        }

        public String getDefV() {
            return defV;
        }
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String,String> getCookieMap(){
        HashMap<String, String> map = new HashMap<>();
        if (cookie!=null && cookie.contains("=") && cookie.contains(";")){
            String value = cookie;
            if (value.contains(";")){
                String[] split = value.split(";");
                for (String s : split) {
                    if (s.contains("=")){
                        String[] sp = s.split("=");
                        String k = sp[0];
                        String v = sp[1];
                        map.put(k,v);
                    }
                }
            }
        }
        return map;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Header[] getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Header[] httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
    public void addHttpHeader(String header){
        String key = header.substring(0,header.indexOf(":"));
        String vul = header.substring(header.indexOf(":")+1,header.length());
        addHttpHeader(key,vul);
    }
    public void addHttpHeader(String k , String v){
        BasicHeader basicHeader = new BasicHeader(k, v);
        addHttpHeader(basicHeader);
    }
    public void addHttpHeader(Header header){
        if (httpHeaders.length==0)
        {
            httpHeaders[0] = header;
        }else{
            Header[] add = ArrayUtils.add(httpHeaders, header);
            setHttpHeaders(add);
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getParamStr() {
        return paramStr;
    }

    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public Request clone()  {
        try {
            return (Request) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
