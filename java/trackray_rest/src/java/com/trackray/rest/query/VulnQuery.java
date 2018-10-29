package com.trackray.rest.query;

import javax.validation.constraints.NotNull;

public class VulnQuery {
    @NotNull
    private String md5 = "";
    private int level = 0;
    private String payload = "";
    private String response = "";
    private String message = "";
    private String vulnid = "";
    private String aboutLink = "";
    private int vulnType = 0;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVulnid() {
        return vulnid;
    }

    public void setVulnid(String vulnid) {
        this.vulnid = vulnid;
    }

    public String getAboutLink() {
        return aboutLink;
    }

    public void setAboutLink(String aboutLink) {
        this.aboutLink = aboutLink;
    }

    public int getVulnType() {
        return vulnType;
    }

    public void setVulnType(int vulnType) {
        this.vulnType = vulnType;
    }
}
