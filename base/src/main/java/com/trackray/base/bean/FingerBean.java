package com.trackray.base.bean;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
@Builder(toBuilder = true)
public class FingerBean {
    private String version; //指纹版本
    private String match;   //匹配表达式
    private String url; //指纹URL
    private String md5; //指纹MD5

    public FingerBean(String version, String match, String url) {
        this.version = version;
        this.match = match;
        this.url = url;
    }

    public FingerBean(String match, String url) {
        this.match = match;
        this.url = url;
    }


    public FingerBean(String version, String url,String match, String md5) {
        this.version = version;
        this.match = match;
        this.url = url;
        this.md5 = md5;
    }

    public boolean isMatch(){
        if (StringUtils.isNotBlank(match) && StringUtils.isBlank(md5))
            return true;
        return false;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
