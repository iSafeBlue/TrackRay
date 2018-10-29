package com.trackray.rest.dto;

public class FingerDTO {
    private Integer id;

    private String enumKey;

    private String fingerName;

    private String url;

    private String regex;

    private String md5;

    private Integer fingerType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnumKey() {
        return enumKey;
    }

    public void setEnumKey(String enumKey) {
        this.enumKey = enumKey == null ? null : enumKey.trim();
    }

    public String getFingerName() {
        return fingerName;
    }

    public void setFingerName(String fingerName) {
        this.fingerName = fingerName == null ? null : fingerName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex == null ? null : regex.trim();
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public Integer getFingerType() {
        return fingerType;
    }

    public void setFingerType(Integer fingerType) {
        this.fingerType = fingerType;
    }
}