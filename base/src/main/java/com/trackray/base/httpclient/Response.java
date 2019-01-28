package com.trackray.base.httpclient;

import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.Base64Utils;


public class Response implements Cloneable{

    private ResponseStatus status = new ResponseStatus();

    private String redirect = "";

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Document getRespFormatHtml(){
        return Jsoup.parse(status.getContent());
    }

    public JSONObject getRespFormatJson(){
        return JSONObject.fromObject(status.getContent());
    }

    public String getBodyToBase64(){
        return Base64Utils.encodeToString(status.getContentBytes());
    }

    @Override
    public Response clone()  {
        try {
            return (Response) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
