package com.trackray.scanner.bean;

import com.trackray.scanner.enums.VulnLevel;
import com.trackray.scanner.enums.VulnType;

public class Vulnerable {

    private String payload = "";//漏洞的攻击载荷
    private Object response = "";//漏洞的响应内容
    private String message = "";//漏洞的解释内容
    private VulnInfo vulnInfo = new VulnInfo();

    public Vulnerable(String payload, Object response, String message, VulnInfo vulnInfo) {
        this.payload = payload;
        this.response = response;
        this.message = message;
        this.vulnInfo = vulnInfo;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VulnInfo getVulnInfo() {
        return vulnInfo;
    }

    public void setVulnInfo(VulnInfo vulnInfo) {
        this.vulnInfo = vulnInfo;
    }



    public static class VulnBuilder{
        private String payload = "";//漏洞的攻击载荷
        private Object response = "";//漏洞的响应内容
        private String message = "";//漏洞的解释内容

        private String aboutLink = "";
        private VulnType vulnType;
        private VulnLevel level;
        private String vulnID = "";

        public VulnBuilder level(VulnLevel val){
            level = val;
            return this;
        }
        public VulnBuilder response(String val){
            payload = val;
            return this;
        }
        public VulnBuilder payload(String val){
            response = val;
            return this;
        }
        public VulnBuilder message(String val){
            message = val;
            return this;
        }
        public VulnBuilder aboutLink(String val){
            aboutLink = val;
            return this;
        }
        public VulnBuilder vulnID(String val){
            vulnID = val;
            return this;
        }
        public VulnBuilder vulnType(VulnType type){
            vulnType = type;
            return this;
        }

        public Vulnerable build(){
            VulnInfo vulnInfo = new VulnInfo();
            vulnInfo.setVulnType(vulnType);
            vulnInfo.setAboutLink(aboutLink);
            vulnInfo.setVulnID(vulnID);
            vulnInfo.setVulnLevel(level);
            return new Vulnerable(payload,response,message,vulnInfo);
        }

    }

}
