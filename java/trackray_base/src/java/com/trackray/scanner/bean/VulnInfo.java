package com.trackray.scanner.bean;

import com.trackray.scanner.enums.VulnLevel;
import com.trackray.scanner.enums.VulnType;

public class VulnInfo{
    private String aboutLink = "";
    private VulnType vulnType;
    private VulnLevel vulnLevel;
    private String vulnID = "";

    public VulnInfo() {
    }

    public VulnLevel getVulnLevel() {
        return vulnLevel;
    }

    public void setVulnLevel(VulnLevel vulnLevel) {
        this.vulnLevel = vulnLevel;
    }

    public VulnType getVulnType() {
        return vulnType;
    }

    public void setVulnType(VulnType vulnType) {
        this.vulnType = vulnType;
    }

    public String getAboutLink() {
        return aboutLink;
    }

    public void setAboutLink(String aboutLink) {
        this.aboutLink = aboutLink;
    }

    public String getVulnID() {
        return vulnID;
    }

    public void setVulnID(String vulnID) {
        this.vulnID = vulnID;
    }
}