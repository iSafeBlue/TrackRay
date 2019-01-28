package com.trackray.web.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class VulnDTO implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private String taskMd5;

    private Integer level;

    private String payload;

    private String message;

    private String vulnId;

    private String aboutLink;

    private Integer vulnType;

    private Date createTime = new Date();

    private String response;

    private String request;
}