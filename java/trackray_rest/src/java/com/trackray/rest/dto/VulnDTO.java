package com.trackray.rest.dto;

import java.util.Date;

public class VulnDTO {
    private Integer id;

    private String taskMd5;

    private Integer level;

    private String payload;

    private String message;

    private String vulnId;

    private String aboutLink;

    private Integer vulnType;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskMd5() {
        return taskMd5;
    }

    public void setTaskMd5(String taskMd5) {
        this.taskMd5 = taskMd5 == null ? null : taskMd5.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload == null ? null : payload.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getVulnId() {
        return vulnId;
    }

    public void setVulnId(String vulnId) {
        this.vulnId = vulnId == null ? null : vulnId.trim();
    }

    public String getAboutLink() {
        return aboutLink;
    }

    public void setAboutLink(String aboutLink) {
        this.aboutLink = aboutLink == null ? null : aboutLink.trim();
    }

    public Integer getVulnType() {
        return vulnType;
    }

    public void setVulnType(Integer vulnType) {
        this.vulnType = vulnType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}